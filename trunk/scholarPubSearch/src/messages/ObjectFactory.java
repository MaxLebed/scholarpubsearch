/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package messages;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.example.cfp package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    //CFP Onjects
     private final static QName _CfpQuery_QNAME = new QName("http://www.example.org/cfp", "cfpQuery");
    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.example.cfp
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CfpQuery }
     *
     */
    public CfpQuery createCfpQuery() {
        return new CfpQuery();
    }

    /**
     * Create an instance of {@link PublicationRequest }
     *
     */
    public PublicationRequest createPublicationRequest() {
        return new PublicationRequest();
    }

     /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CfpQuery }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.example.org/cfp", name = "cfpQuery")
    public JAXBElement<CfpQuery> createCfpQuery(CfpQuery value) {
        return new JAXBElement<CfpQuery>(_CfpQuery_QNAME, CfpQuery.class, null, value);
    }


    //Propose onjects
    private final static QName _Publications_QNAME = new QName("http://www.example.org/propose/", "publications");

     /**
     * Create an instance of {@link PersonType }
     *
     */
    public PersonType createPersonType() {
        return new PersonType();
    }

    /**
     * Create an instance of {@link JournalType }
     *
     */
    public JournalType createJournalType() {
        return new JournalType();
    }

    /**
     * Create an instance of {@link Publications }
     *
     */
    public Publications createPublications() {
        return new Publications();
    }

    /**
     * Create an instance of {@link StringAttributeList }
     *
     */
    public StringAttributeList createStringAttributeList() {
        return new StringAttributeList();
    }

    /**
     * Create an instance of {@link Publication }
     *
     */
    public Publication createPublication() {
        return new Publication();
    }

    /**
     * Create an instance of {@link Publication.AuthorList }
     *
     */
    public Publication.AuthorList createPublicationAuthorList() {
        return new Publication.AuthorList();
    }
    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Publications }{@code >}}
     *
    */
    @XmlElementDecl(namespace = "http://www.example.org/propose/", name = "publications")
    public JAXBElement<Publications> createPublications(Publications value) {
        return new JAXBElement<Publications>(_Publications_QNAME, Publications.class, null, value);
    }

    private final static QName _InformMessage_QNAME = new QName("http://www.example.org/inform", "inform");


    //Inform Objects
    /**
     * Create an instance of {@link InformMessage }
     *
     */
    public InformMessage createInformMessage() {
        return new InformMessage();
    }

     /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InformMessage }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.example.org/inform", name = "inform")
    public JAXBElement<InformMessage> createInformMessage(InformMessage value) {
        return new JAXBElement<InformMessage>(_InformMessage_QNAME, InformMessage.class, null, value);
    }
}
