package main;

import jade.core.Runtime;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

public class Main {

    AgentContainer mainContainer;

    public static void main(String[] args) {
        if(args.length > 0)
            configAgents(args[0]);
        else
            configAgents("examples\\config.xml");
    }

    private static void configAgents(String path)
    {
        try
        {
            Runtime rt = Runtime.instance();
            rt.setCloseVM(true);
            ProfileImpl profile = new ProfileImpl();
            AgentContainer ac = rt.createMainContainer(profile);
            //Import
            AgentImport importSystem = new AgentImport(path);
            for(String arxiv:importSystem.getArxivOrgAgents())
            {
                ac.createNewAgent(arxiv, dataSources.ArxivOrgAgent.class.getName(), new Object[0]);
                ac.getAgent(arxiv).start();

            }
            for(String arxiv:importSystem.getGoogleScholarAgents())
            {
                ac.createNewAgent(arxiv, dataSources.GoogleScholarAgent.class.getName(), new Object[0]);
                ac.getAgent(arxiv).start();

            }
            for(String clas:importSystem.getClassifiers())
            {
                ac.createNewAgent(clas, classifier.ClassifierAgent.class.getName(), new Object[0]);
                ac.getAgent(clas).start();
            }
            for(String up:importSystem.getUserPreferenceAgents())
            {
                ac.createNewAgent(up, preferences.UserPreferenceAgent.class.getName(), new Object[0]);
                ac.getAgent(up).start();
            }
            for(String ui:importSystem.getUserInputAgents())
            {
                Object [] arg = new Object[2];
                arg[0] = importSystem.getPref(ui);
                arg[1] = importSystem.getFile(ui);
                ac.createNewAgent(ui, scholarpubsearch.UserInputAgent.class.getName(), arg);
                ac.getAgent(ui).start();
            }
            ac.createNewAgent("rma", jade.tools.rma.rma.class.getName(), new Object[0]);
            ac.getAgent("rma").start();
        }
        catch (StaleProxyException be)
        {
            System.err.println(be);
        }
        catch (ControllerException be)
        {
            System.err.println(be);
        }
    }

}
