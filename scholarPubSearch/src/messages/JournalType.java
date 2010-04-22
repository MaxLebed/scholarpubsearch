/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for JournalType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="JournalType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="volume" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pages" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JournalType", propOrder = {
    "name",
    "volume",
    "number",
    "pages"
})
public class JournalType {

    @XmlElement(required = true)
    protected String name;
    protected String volume;
    protected String number;
    protected String pages;

    /**
     * Gets the value of the name property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the volume property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getVolume() {
        return volume;
    }

    /**
     * Sets the value of the volume property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setVolume(String value) {
        this.volume = value;
    }

    /**
     * Gets the value of the number property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the value of the number property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNumber(String value) {
        this.number = value;
    }

    /**
     * Gets the value of the pages property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPages() {
        return pages;
    }

    /**
     * Sets the value of the pages property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPages(String value) {
        this.pages = value;
    }

}
