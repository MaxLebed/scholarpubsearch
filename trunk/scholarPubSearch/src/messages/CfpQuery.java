/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package messages;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="publicationRequest" type="{http://www.example.org/cfp/}PublicationRequest"/>
 *         &lt;element name="sourceList" type="{http://www.example.org/propose/}StringAttributeList"/>
 *         &lt;element name="resultsNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "publicationRequest",
    "sourceList",
    "resultsNumber"
})
@XmlRootElement(name = "cfpQuery")
public class CfpQuery {

    @XmlElement(required = true)
    protected PublicationRequest publicationRequest;
    @XmlElement(required = true)
    protected StringAttributeList sourceList;
    protected int resultsNumber;

    /**
     * Gets the value of the publicationRequest property.
     *
     * @return
     *     possible object is
     *     {@link PublicationRequest }
     *
     */
    public PublicationRequest getPublicationRequest() {
        return publicationRequest;
    }

    /**
     * Sets the value of the publicationRequest property.
     *
     * @param value
     *     allowed object is
     *     {@link PublicationRequest }
     *
     */
    public void setPublicationRequest(PublicationRequest value) {
        this.publicationRequest = value;
    }

    /**
     * Gets the value of the sourceList property.
     *
     * @return
     *     possible object is
     *     {@link StringAttributeList }
     *
     */
    public StringAttributeList getSourceList() {
        return sourceList;
    }

    /**
     * Sets the value of the sourceList property.
     *
     * @param value
     *     allowed object is
     *     {@link StringAttributeList }
     *
     */
    public void setSourceList(StringAttributeList value) {
        this.sourceList = value;
    }

    /**
     * Gets the value of the resultsNumber property.
     *
     */
    public int getResultsNumber() {
        return resultsNumber;
    }

    /**
     * Sets the value of the resultsNumber property.
     *
     */
    public void setResultsNumber(int value) {
        this.resultsNumber = value;
    }

}
