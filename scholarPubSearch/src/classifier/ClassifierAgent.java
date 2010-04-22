/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier;

import messages.CFP;
import messages.CfpQuery;
import messages.Propose;
import messages.Publication;
import messages.Publications;
import messages.StringAttributeList;
import scholarpubsearch.UserInputAgent;
import main.Util;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetInitiator;
import jade.proto.ContractNetResponder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import javax.xml.bind.JAXBException;

/**
 *
 * @author user
 */
public class ClassifierAgent extends Agent {

    public static final String SERVICE_NAME = "Classifier";
    public static final int TIME_FOR_RESPOND = 5000;
    private AID userPreferenceAgent;
    private ArrayList<AID> currentDataSources;
    private ACLMessage dataSourceMsg;

    @Override
    protected void setup() {
        Util.DFRegister(this, SERVICE_NAME);
        System.out.println(SERVICE_NAME + "Agent " + getAID().getName() + " is ready.");
        currentDataSources = new ArrayList<AID>();

        addBehaviour(new DictionaryLoader());

        respondUPA();

    }

    private void resetTask() {
        userPreferenceAgent = null;
        currentDataSources = new ArrayList<AID>();
        dataSourceMsg = null;
    }

    @Override
    protected void takeDown() {
        System.out.println(SERVICE_NAME + "Agent " + getAID().getName() + " is terminating.");
    }

    private class DictionaryLoader extends OneShotBehaviour {

        @Override
        public void action() {
            //TODO: load the dictionary from filesystem
        }
    }

    private class RecieveUserRequest extends Behaviour {

        private boolean requestRecieved = false;

        @Override
        public void action() {
            MessageTemplate cfpTemplate =
                    MessageTemplate.and(
                    MessageTemplate.MatchPerformative(ACLMessage.CFP),
                    MessageTemplate.MatchConversationId(UserInputAgent.CFP_CONVERSATION_ID));
            ACLMessage msg = myAgent.receive(cfpTemplate);
            if (msg != null) {
                try {
                    String msgContent = msg.getContent();
                    dataSourceMsg = new ACLMessage(msg.getPerformative());
                    CfpQuery cfpQuery = CFP.unmarshal(msgContent);
                    CFP cfp = new CFP();
                    cfp.setCFPQuery(cfpQuery);
                    classify(cfp);
                    dataSourceMsg.setContent(cfp.marshal());
                    requestRecieved = true;
                    getAllDataSources(cfp.getCFPQuery().getSourceList());
                    myAgent.addBehaviour(
                            new DistributeRequest(
                                cfp.getCFPQuery().getSourceList()));
                } catch (JAXBException je) {
                    je.printStackTrace();
                }
            } else {
                block();
            }
        }

        private void classify(CFP cfp) {
        }


        @Override
        public boolean done() {
            return requestRecieved;
        }

        private void getAllDataSources(StringAttributeList sourceList) {
            if (sourceList == null) {
                return;
            }
            for (String source : sourceList.getAttribute()) {
                getDataSource(source);
            }
        }

        private void getDataSource(String sourceType) {
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType(sourceType);
            template.addServices(sd);
            ACLMessage getDataSource = new ACLMessage(ACLMessage.CFP);
            try {
                DFAgentDescription[] dataSources = DFService.search(myAgent, template);
                for (DFAgentDescription existingSource : dataSources) {
                    getDataSource.addReceiver(existingSource.getName());
                }
            } catch (FIPAException fe) {
                fe.printStackTrace();
            }
            getDataSource.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
            // reply in 5 secs
            getDataSource.setReplyByDate(new Date(System.currentTimeMillis() + TIME_FOR_RESPOND));

            addBehaviour(new ContractNetInitiator(myAgent, getDataSource) {

                private AID currentSource;
                @Override
                protected void handlePropose(ACLMessage propose, Vector acceptances) {
                    if (currentSource == null) {
                        currentSource = propose.getSender();
                        currentDataSources.add(currentSource);
                        System.out.println(SERVICE_NAME + getAID().getName()
                                + ": Current dataSource is " + currentSource.getLocalName());
                    }
                }

                @Override
                protected void handleAllResponses(Vector responses, Vector acceptances) {
                    Enumeration e = responses.elements();
                    while (e.hasMoreElements()) {
                        ACLMessage msg = (ACLMessage) e.nextElement();
                        if (msg.getPerformative() == ACLMessage.PROPOSE) {
                            ACLMessage reply = msg.createReply();
                            if (msg.getSender() == currentSource) {
                                reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                            } else {
                                reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                            }
                            acceptances.addElement(reply);
                        }
                    }
                }

                @Override
                protected void handleInform(ACLMessage inform) {
                    //myAgent.addBehaviour(new SendUserRequest());
                    //addBehaviour(new SendUserRequest());
                }
            });
        }
    }

    private class DistributeRequest extends Behaviour {
        private long startTime = System.currentTimeMillis();
        private StringAttributeList sources;

        public DistributeRequest(StringAttributeList sources) {
            this.sources = sources;
        }

        @Override
        public void action() {
            //
        }

        @Override
        public boolean done() {
            boolean allSourcedResponded = 
                    currentDataSources.size() == sources.getAttribute().size();
            boolean timeIsOver =
                    (System.currentTimeMillis() - startTime) > TIME_FOR_RESPOND;

            return allSourcedResponded || timeIsOver;
        }

        @Override
        public int onEnd() {
            myAgent.addBehaviour(new SendUserRequest());
            return 0;
        }
    }

    private class SendUserRequest extends OneShotBehaviour {

        @Override
        public void action() {
            for (AID source : currentDataSources) {
                dataSourceMsg.addReceiver(source);
            }
            dataSourceMsg.setConversationId(UserInputAgent.CFP_CONVERSATION_ID);
            myAgent.send(dataSourceMsg);
            addBehaviour(new RecievePropose());
        }
    }

    private class RecievePropose extends Behaviour {

        private boolean allPropositionsRecieved = false;
        private ArrayList<Publication> allPropositions =
                new ArrayList<Publication>();

        @Override
        public void action() {
            MessageTemplate proposeTemplate =
                    MessageTemplate.and(
                    MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
                    MessageTemplate.MatchConversationId(UserInputAgent.PROPOSE_CONVERSATION_ID));
            ACLMessage msg = myAgent.receive(proposeTemplate);
            if (msg != null) {
                try {
                    //proposeRecieved = true;
                    Publications pubs = Propose.unmarshal(msg.getContent());
                    if(pubs != null)
                        allPropositions.addAll(pubs.getPublication());
                } catch (JAXBException ex) {
                    ex.printStackTrace();
                }
                currentDataSources.remove(msg.getSender());
                if(currentDataSources.isEmpty()) {
                    allPropositionsRecieved = true;
                    ACLMessage proposeMsg = new ACLMessage(ACLMessage.PROPOSE);
                    proposeMsg.setConversationId(UserInputAgent.PROPOSE_CONVERSATION_ID);
                    Propose propose = new Propose();
                    Publications pubs = new Publications();
                    pubs.setPublication(allPropositions);
                    propose.setPublications(pubs);
                    proposeMsg.setContent(propose.marshal());
                    proposeMsg.addReceiver(userPreferenceAgent);
                    myAgent.send(proposeMsg);
                    resetTask();
                    respondUPA();

                }
            } else {
                block();
            }
        }

        @Override
        public boolean done() {
            return allPropositionsRecieved;
        }
    }

    private void respondUPA() {
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
                MessageTemplate.MatchPerformative(ACLMessage.CFP));

        addBehaviour(new ContractNetResponder(this, template) {

            @Override
            protected ACLMessage prepareResponse(ACLMessage cfp) throws NotUnderstoodException, RefuseException {
                if (userPreferenceAgent == null) {
                    ACLMessage propose = cfp.createReply();
                    propose.setPerformative(ACLMessage.PROPOSE);
                    return propose;
                } else {
                    throw new RefuseException("");
                }
            }

            @Override
            protected ACLMessage prepareResultNotification(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
                userPreferenceAgent = cfp.getSender();
                System.out.println(SERVICE_NAME + getLocalName()
                        + ": Current UserPreferenceAgent is " + userPreferenceAgent.getLocalName());
                ACLMessage inform = accept.createReply();
                inform.setPerformative(ACLMessage.INFORM);
                addBehaviour(new RecieveUserRequest());
                return inform;
            }
        });
    }
}
