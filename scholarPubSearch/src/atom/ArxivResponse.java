/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package atom;

import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author user
 */
public class ArxivResponse {
    private static final String packageName =
            ArxivResponse.class.getPackage().getName();

    ObjectFactory of;

    public ArxivResponse() {
        of = new ObjectFactory();
    }

    public static FeedType unmarshal(String response) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(packageName);
        Unmarshaller u = jc.createUnmarshaller();
        StringReader reader = new StringReader(response);
        JAXBElement<FeedType> jel = (JAXBElement<FeedType>) u.unmarshal(reader);
        return jel.getValue();
    }
}
