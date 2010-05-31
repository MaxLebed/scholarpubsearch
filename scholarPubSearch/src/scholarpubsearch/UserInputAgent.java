package scholarpubsearch;

import preferences.UserPreferenceAgent;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import messages.CFP;
import messages.Propose;
import messages.Publications;
import messages.Inform;
import main.Util;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import java.io.File;
import java.io.FileOutputStream;
import javax.xml.bind.JAXBException;

public class UserInputAgent extends GuiAgent {

    //public static final String defaultPreferenceFile = "userpref.xml";
    public static final String SERVICE_NAME = "UserInput";
    public static final String PREFERENCE_CONVERSATION_ID= "preference";
    public static final String INFORM_CONVERSATION_ID= "inform";
    public static final String CFP_CONVERSATION_ID= "cfp";
    public static final String PROPOSE_CONVERSATION_ID = "propose";
    //GUI Events
    public static final int CFP_EVENT = 0;
    public static final int INFORM_EVENT = 1;
    public static final int CLOSING_EVENT = 2;


    private AID userPreferenceAgent;
    private String preferenceURI;
    private CFP currentCfp;
    private Inform currentInform;
    private Publications propose;
    private ScholarPubSearchView gui;
    private SearchForm activeSearchForm;

    @Override
    protected void setup() {
        Util.DFRegister(this, SERVICE_NAME);
        System.out.println(SERVICE_NAME + "Agent " + getAID().getName() + " is ready.");

        ScholarPubSearchApp.launch(ScholarPubSearchApp.class, new String[0]);
        ScholarPubSearchApp appl = ScholarPubSearchApp.getApplication();
        gui = new ScholarPubSearchView(appl, this);
        appl.show(gui);


        addBehaviour(new InitialBehaviour());
    }

    @Override
    protected void takeDown() {
        System.out.println(SERVICE_NAME + "Agent " + getAID().getName() + " is terminating.");
        Util.DFDeregister(this);
    }

    @Override
    protected void onGuiEvent(GuiEvent ge) {
        if (ge.getType() == CFP_EVENT) {
            currentCfp = (CFP) ge.getParameter(0);
            addBehaviour(new CFPBehaviour());
        }
        if (ge.getType() == INFORM_EVENT) {
            currentInform = (Inform) ge.getParameter(0);
            addBehaviour(new InformBehaviour());
        }
        if(ge.getType() == CLOSING_EVENT) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(userPreferenceAgent);
            msg.setConversationId(PREFERENCE_CONVERSATION_ID);
            send(msg);
            addBehaviour(new RecievePreferences());
        }
    }

    public Publications getPropose() {
        return propose;
    }

    public ScholarPubSearchView getMainFrame() {
        return gui;
    }

    public void startNewUserPreferenceAgent(String name) {
        AgentContainer ac = this.getContainerController();
        try {
            ac.createNewAgent(name, "preferences.UserPreferenceAgent", new Object[0]);
            ac.getAgent(name).start();
            //
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType(UserPreferenceAgent.SERVICE_NAME);
            template.addServices(sd);
            DFAgentDescription[] result = null;
            while(result == null || result.length == 0) {
                result = DFService.search(this, template);
                for (DFAgentDescription dfad : result) {
                    if(dfad.getName().getLocalName().equals(name))
                        userPreferenceAgent = dfad.getName();
                        System.out.println("Current "
                                + UserPreferenceAgent.SERVICE_NAME + " is "
                                +userPreferenceAgent.getLocalName());
                        System.out.println("Search is available");
                }
            }
        } catch (StaleProxyException ex) {
            ex.printStackTrace();
        }
        catch (ControllerException ex) {
            ex.printStackTrace();
        }
        catch (FIPAException fe) {
                fe.printStackTrace();
        }
    }

    public void setActiveSearchForm(SearchForm form) {
        activeSearchForm = form;
    }

    private void sendProposeToGUI() {
        if(activeSearchForm!= null) {
            activeSearchForm.setPublications(propose);
        }
        else {
            gui.setPublications(propose);
        }
    }

    //Create a UserPreferenceAgent AID
    private class InitialBehaviour extends OneShotBehaviour {

        private final long startTime = System.currentTimeMillis();
        private AID someAgent;

        @Override
        public void action() {
            Object[] arg = myAgent.getArguments();
            String UPName = "";
            if (arg.length > 0 && arg[0]!=null) {
                UPName = arg[0].toString();
            } else {
                System.out.println("UserPreferenceAgent is not specified");
                UPName = myAgent.getName()+"PreferenceAgent";
            }
            startNewUserPreferenceAgent(UPName);
            myAgent.addBehaviour(new SendPreferences());
        }
    }

    //Send Preferences to UserPreferenceAgent
    private class SendPreferences extends OneShotBehaviour {

        @Override
        public void action() {
            Object[] arg = myAgent.getArguments();
            if (arg.length > 1 && arg[1]!=null) {
                preferenceURI = arg[1].toString();
            }

            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setConversationId(PREFERENCE_CONVERSATION_ID);
            msg.addReceiver(userPreferenceAgent);

            try {
                String prefString = "";
                String temp = null;
                FileReader prefReader = new FileReader(preferenceURI);
                BufferedReader br = new BufferedReader(prefReader);
                while ((temp = br.readLine()) != null) {
                    prefString += temp;
                }
                br.close();
                prefReader.close();
                msg.setContent(prefString);
            } catch (FileNotFoundException fe) {
                System.out.println("Preferenece file is not specified");
                fe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            }
            myAgent.send(msg);
        }

    }

    //Request publications
    private class CFPBehaviour extends OneShotBehaviour {

        @Override
        public void action() {
            ACLMessage msg = new ACLMessage(ACLMessage.CFP);
            msg.setConversationId(CFP_CONVERSATION_ID);
            msg.setContent(currentCfp.marshal());
            msg.addReceiver(userPreferenceAgent);
            myAgent.send(msg);
            addBehaviour(new RecievePropose());
        }
    }

    //Inform UPA about preferences
    private class InformBehaviour extends OneShotBehaviour {

        @Override
        public void action() {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setConversationId(INFORM_CONVERSATION_ID);
            msg.setContent(currentInform.marshal());
            msg.addReceiver(userPreferenceAgent);
            myAgent.send(msg);
            addBehaviour(new RecievePropose());
        }
    }

    //Recieve proposition from UBA
    private class RecievePropose extends Behaviour {

        private boolean proposeRecieved = false;

        @Override
        public void action() {
            MessageTemplate proposeTemplate =
                    MessageTemplate.and(
                    MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
                    MessageTemplate.MatchConversationId(PROPOSE_CONVERSATION_ID));
            ACLMessage msg = myAgent.receive(proposeTemplate);
            if (msg != null) {
                System.out.println("finish");
                try {
                    propose = Propose.unmarshal(msg.getContent());
                    sendProposeToGUI();
                } catch (JAXBException ex) {
                    ex.printStackTrace();
                }
                proposeRecieved = true;
            } else {
                block();
            }
        }

        @Override
        public boolean done() {
            return proposeRecieved;
        }
    }
    //recieve preferences from UPA after closing serch window
    private class RecievePreferences extends Behaviour {
        boolean recieved = false;

        @Override
        public void action() {
            MessageTemplate behaviourRequestedTemplate = MessageTemplate.and(
                    MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                    MessageTemplate.MatchConversationId(
                    UserInputAgent.PREFERENCE_CONVERSATION_ID));
            ACLMessage msg = myAgent.receive(behaviourRequestedTemplate);
            if (msg != null) {
                recieved = true;
            try {
                //write preferences in file
                FileOutputStream outFile;
                File f = new File(preferenceURI);
                if (!f.exists()) {
                    f.createNewFile();
                }
                outFile = new FileOutputStream(preferenceURI);
                outFile.write(msg.getContent().getBytes());
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException io) {
                io.printStackTrace();
            }
            }
        }

        @Override
        public boolean done() {
            return recieved;
        }
    }
}
