/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package preferences;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.example.userpreference package.
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

    private final static QName _Preference_QNAME = new QName("http://www.example.org/UserPreference", "preference");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.example.userpreference
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RatingElem }
     *
     */
    public RatingElem createRatingElem() {
        return new RatingElem();
    }

    /**
     * Create an instance of {@link Rating }
     *
     */
    public Rating createRating() {
        return new Rating();
    }

    /**
     * Create an instance of {@link UserPreference }
     *
     */
    public UserPreference createUserPreference() {
        return new UserPreference();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserPreference }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.example.org/UserPreference", name = "preference")
    public JAXBElement<UserPreference> createPreference(UserPreference value) {
        return new JAXBElement<UserPreference>(_Preference_QNAME, UserPreference.class, null, value);
    }

}
