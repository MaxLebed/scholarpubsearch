package dataSources;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.bind.JAXBException;
import main.Util;
import messages.CFP;
import messages.CfpQuery;
import messages.Propose;
import scholarpubsearch.UserInputAgent;

/**
 *
 * @author Aleksandr Dzyuba
 */
public abstract class DataSourceAgent extends Agent{
    private AID classifierAgent;

    public abstract String getServiceName();
    public abstract Propose RequestDataSource(CfpQuery cfp);
    public abstract String getDefaultHref();

     @Override
    protected void setup() {
            Util.DFRegister(this, getServiceName());
            System.out.println(getServiceName() + "Agent "
                    + getAID().getName() + " is ready.");

            respondClassifier();
    }

    @Override
    protected void takeDown() {
        System.out.println(getServiceName() + "Agent "
                + getAID().getName() + " is terminating.");
    }

    private void resetTask() {
        classifierAgent = null;
    }

    public boolean testConnection() {
        URL url;
        URLConnection conn = null;
        try {
            url = new URL(getDefaultHref());
            conn = url.openConnection();
            conn.setDoOutput(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.close();
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }

    private void respondClassifier() {
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
                MessageTemplate.MatchPerformative(ACLMessage.CFP));

        addBehaviour(new ContractNetResponder(this, template) {

            @Override
            protected ACLMessage prepareResponse(ACLMessage cfp) throws NotUnderstoodException, RefuseException {
                if (classifierAgent == null && testConnection()) {
                    ACLMessage propose = cfp.createReply();
                    propose.setPerformative(ACLMessage.PROPOSE);
                    return propose;
                } else {
                    throw new RefuseException("");
                }
            }

            @Override
            protected ACLMessage prepareResultNotification(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
                classifierAgent = cfp.getSender();
                System.out.println(getServiceName() + getLocalName() +
                        ": Current Classifier is " + classifierAgent.getLocalName());
                ACLMessage inform = accept.createReply();
                inform.setPerformative(ACLMessage.INFORM);
                addBehaviour(new RecieveRequests());
                return inform;
            }
        });
    }

    private class RecieveRequests extends Behaviour {
        private boolean requestRecieved = false;

        @Override
        public void action() {
            MessageTemplate cfpTemplate =
                    MessageTemplate.and(
                    MessageTemplate.MatchPerformative(ACLMessage.CFP),
                    MessageTemplate.MatchConversationId(
                    UserInputAgent.CFP_CONVERSATION_ID));
            ACLMessage msg = myAgent.receive(cfpTemplate);
            if (msg != null) {
                try {
                    ACLMessage proposition = new ACLMessage(ACLMessage.PROPOSE);
                    proposition.addReceiver(classifierAgent);
                    proposition.setConversationId(UserInputAgent.PROPOSE_CONVERSATION_ID);

                    CfpQuery cfp = CFP.unmarshal(msg.getContent());
                    Propose propose = RequestDataSource(cfp);
                    proposition.setContent(propose.marshal());

                    myAgent.send(proposition);
                    resetTask();
                    requestRecieved = true;
                    respondClassifier();
                } catch (JAXBException je) {
                    je.printStackTrace();
                }
            } else {
                block();
            }
        }

        @Override
        public boolean done() {
            return requestRecieved;
        }
    }
}
