package preferences;

import classifier.ClassifierAgent;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.proto.ContractNetInitiator;
import javax.xml.bind.JAXBException;
import messages.CFP;
import messages.CfpQuery;
import messages.Propose;
import messages.Publications;
import messages.Inform;
import messages.InformMessage;
import scholarpubsearch.UserInputAgent;
import main.Util;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

public class UserPreferenceAgent extends Agent {
    public static final String SERVICE_NAME = "UserPreference";
    public static final int TIME_FOR_RESPOND = 5000;
    private AID userInputAgent;
    private Preference preference;
    private Publications currentResult;
    private AID currentClassifier;
    private ACLMessage classifierMsg;
    private int resultsCount;



    @Override
    protected void setup() {
        Util.DFRegister(this, SERVICE_NAME);
        System.out.println(SERVICE_NAME + "Agent " + getAID().getName() +
                " is ready.");
        addBehaviour(new UserPreferenceLoader());
    }

    @Override
    protected void takeDown() {
        System.out.println(SERVICE_NAME + "Agent " + getAID().getName() +
                " is terminating.");
        Util.DFDeregister(this);
    }

    protected void resetTask() {
        classifierMsg = null;
        currentClassifier = null;
        resultsCount = 0;
    }

    private class UserPreferenceLoader extends Behaviour {

        private boolean preferenceLoaded = false;

        public void action() {
            MessageTemplate behaviourRequestedTemplate = MessageTemplate.and(
                    MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                    MessageTemplate.MatchConversationId(
                    UserInputAgent.PREFERENCE_CONVERSATION_ID));
            ACLMessage msg = myAgent.receive(behaviourRequestedTemplate);
            if (msg != null) {
                userInputAgent = msg.getSender();
                preference = new Preference();
                //load behaviour from msg.Content
                try {
                    preference.setUserPreference(
                            Preference.unmarshal(msg.getContent()));
                } catch (JAXBException je) {
                    System.out.println("Preference file incorrect");
                } catch(NullPointerException ne) {
                ne.printStackTrace();
                }
                preferenceLoaded = true;
                addBehaviour(new RecieveUserRequest());
            } else {
                block();
            }
        }

        public boolean done() {
            return preferenceLoaded;
        }
    }

    private class RecieveUserRequest extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate cfpTemplate = 
                MessageTemplate.or(
                    MessageTemplate.and(
                        MessageTemplate.MatchPerformative(ACLMessage.CFP),
                        MessageTemplate.MatchConversationId(
                            UserInputAgent.CFP_CONVERSATION_ID)),
                    MessageTemplate.and(
                        MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                        MessageTemplate.or(
                            MessageTemplate.MatchConversationId(
                                UserInputAgent.INFORM_CONVERSATION_ID),
                            MessageTemplate.MatchConversationId(
                                UserInputAgent.PREFERENCE_CONVERSATION_ID))));
            ACLMessage msg = myAgent.receive(cfpTemplate);
            if (msg != null) {
                try {
                    String msgContent = msg.getContent();
                    classifierMsg = new ACLMessage(msg.getPerformative());
                    if (msg.getPerformative() == ACLMessage.CFP) {
                        CfpQuery cfpQuery = CFP.unmarshal(msgContent);
                        resultsCount =  cfpQuery.getResultsNumber();
                        preference.change(cfpQuery);
                        CFP cfp = new CFP();
                        cfp.setCFPQuery(cfpQuery);
                        classifierMsg.setContent(cfp.marshal());
                        getClassifier();
                    } else {
                        if(msg.getConversationId().equals(
                                UserInputAgent.INFORM_CONVERSATION_ID)) {
                            InformMessage sp = Inform.unmarshal(msgContent);
                            preference.change(sp, currentResult);
                        } else
                            if(msg.getConversationId().equals(
                                UserInputAgent.PREFERENCE_CONVERSATION_ID)) {
                                //send preferences to input agent
                                preference.refreshUserPreference();
                                ACLMessage reply = msg.createReply();
                                reply.setContent(preference.marshal());
                                myAgent.send(reply);
                            }
                    }
                } catch (JAXBException je) {
                    je.printStackTrace();
                }
            } else {
                block();
            }
        }

        private void getClassifier() {
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType(ClassifierAgent.SERVICE_NAME);
            template.addServices(sd);
            ACLMessage getClassifier = new ACLMessage(ACLMessage.CFP);
            try {
                DFAgentDescription[] classifiers =
                        DFService.search(myAgent, template);
                for (DFAgentDescription existingClass : classifiers) {
                    getClassifier.addReceiver(existingClass.getName());
                }
            } catch (FIPAException fe) {
                fe.printStackTrace();
            }
            getClassifier.setProtocol(
                    FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);

            getClassifier.setReplyByDate(new Date(System.currentTimeMillis() +
                    TIME_FOR_RESPOND));

            addBehaviour(new ContractNetInitiator(myAgent, getClassifier) {

                @Override
                protected void handlePropose(ACLMessage propose, Vector acceptances) {
                    if (currentClassifier == null) {
                        //this is the first replied classifier
                        currentClassifier = propose.getSender();
                        System.out.println(SERVICE_NAME + getAID().getName() +
                                ": Current classifier is " +
                                currentClassifier.getLocalName());
                    }

                }

                @Override
                protected void handleAllResponses(Vector responses,
                        Vector acceptances) {
                    Enumeration e = responses.elements();
                    while (e.hasMoreElements()) {
                        ACLMessage msg = (ACLMessage) e.nextElement();
                        if (msg.getPerformative() == ACLMessage.PROPOSE) {
                            ACLMessage reply = msg.createReply();
                            if (msg.getSender() == currentClassifier) {
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
                    addBehaviour(new SendUserRequest());
                }
            });
        }
    }

    private class SendUserRequest extends OneShotBehaviour {

        @Override
        public void action() {
            classifierMsg.addReceiver(currentClassifier);
            classifierMsg.setConversationId(UserInputAgent.CFP_CONVERSATION_ID);
            myAgent.send(classifierMsg);
            addBehaviour(new RecievePropose());
        }
    }
    private class RecievePropose extends Behaviour {
        private boolean proposeRecieved = false;

        @Override
        public void action() {
        MessageTemplate proposeTemplate =
                MessageTemplate.and(
                    MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
                    MessageTemplate.MatchConversationId(
                        UserInputAgent.PROPOSE_CONVERSATION_ID));
                ACLMessage msg = myAgent.receive(proposeTemplate);
                if(msg != null) {
                    try{
                    Publications publs = Propose.unmarshal(msg.getContent());
                    if(publs != null)
                        preference.range(publs, resultsCount);
                    Propose propose = new Propose();
                    propose.setPublications(publs);
                    currentResult = publs;
                    String rangedPropose = propose.marshal();
                    proposeRecieved = true;
                    ACLMessage proposeMsg = new ACLMessage(ACLMessage.PROPOSE);
                    proposeMsg.setConversationId(
                            UserInputAgent.PROPOSE_CONVERSATION_ID);
                    proposeMsg.setContent(rangedPropose);
                    proposeMsg.addReceiver(userInputAgent);
                    myAgent.send(proposeMsg);
                    resetTask();
                    addBehaviour(new RecieveUserRequest());
                    }
                    catch(JAXBException je) {
                        je.printStackTrace();
                    }
                }
                else
                {
                    block();
                }
        }

        @Override
        public boolean done() {
            return proposeRecieved;
        }
    }
}
