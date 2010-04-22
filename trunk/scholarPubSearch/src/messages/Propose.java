package messages;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringWriter;
import java.io.StringReader;
import java.util.ArrayList;

public class Propose {
    private static final String packageName =
            Propose.class.getPackage().getName();

    private ObjectFactory of;
    private Publications publications;

    public Propose() {
        of = new ObjectFactory();
        publications = of.createPublications();
        publications.publication = new ArrayList<Publication>();
    }

    public void addPublication(Publication p) {
        publications.getPublication().add(p);
    }
    public void setPublications(Publications p) {
        publications = p;
    }

//    public void addPublicationList(List<Publication> plist) {
//        publications.getPublication().addAll(plist);
//    }

    public String marshal() {
        try {
            JAXBElement<Publications> jel = of.createPublications(publications);
            JAXBContext jc = JAXBContext.newInstance(packageName);
            Marshaller m = jc.createMarshaller();
            StringWriter sw = new StringWriter();
            m.marshal(jel, sw);
            return sw.getBuffer().toString();
        } catch (JAXBException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static Publications unmarshal(String request) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(packageName);
        Unmarshaller u = jc.createUnmarshaller();
        StringReader reader = new StringReader(request);
        JAXBElement<Publications> jel =
                (JAXBElement<Publications>) u.unmarshal(reader);
        return jel.getValue();
    }
}
