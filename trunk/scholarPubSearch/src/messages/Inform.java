package messages;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringWriter;
import java.io.StringReader;

public class Inform {
    private static final String packageName =
            Inform.class.getPackage().getName();

    private ObjectFactory of;
    private InformMessage inform;

    public Inform() {
        of = new ObjectFactory();
        inform = of.createInformMessage();
    }

    public void setInformMessage(InformMessage infMsg) {
        inform = infMsg;
    }

    public String marshal() {
        try {
            JAXBElement<InformMessage> jel = of.createInformMessage(inform);
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
    public static InformMessage unmarshal(String req) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(packageName);
        Unmarshaller u = jc.createUnmarshaller();
        StringReader reader = new StringReader(req);
        JAXBElement<InformMessage> jel = (JAXBElement<InformMessage>) u.unmarshal(reader);
        return jel.getValue();
    }

}
