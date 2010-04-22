package main;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Util {

    public static void DFRegister(Agent a, String SERVICE_NAME)
    {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(a.getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(SERVICE_NAME);
        sd.setName(a.getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(a, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }
}
