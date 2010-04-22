/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringWriter;
import java.io.StringReader;

public class CFP {
    private static final String packageName = CFP.class.getPackage().getName();

    private ObjectFactory of;
    private CfpQuery cfp;

    public CFP() {
        of = new ObjectFactory();
        cfp = of.createCfpQuery();
    }

    public void setCFPQuery(CfpQuery c) {
        cfp = c;
    }
    public CfpQuery getCFPQuery() {
        return cfp;
    }
    
    public String marshal() {
        try {
            JAXBElement<CfpQuery> jel = of.createCfpQuery(cfp);
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
    public static CfpQuery unmarshal(String cfp) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(packageName);
        Unmarshaller u = jc.createUnmarshaller();
        StringReader reader = new StringReader(cfp);
        JAXBElement<CfpQuery> jel = (JAXBElement<CfpQuery>) u.unmarshal(reader);
        return jel.getValue();
    }
}
