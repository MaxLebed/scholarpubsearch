/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package preferences;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RatingElem complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="RatingElem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RatingElem", propOrder = {
    "key",
    "value"
})
public class RatingElem {

    @XmlElement(required = true)
    protected String key;
    @XmlElement(required = true)
    protected int value;

    /**
     * Gets the value of the key property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the value of the key property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setKey(String value) {
        this.key = value;
    }

    /**
     * Gets the value of the value property.
     *
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     */
    public void setValue(int value) {
        this.value = value;
    }

}
