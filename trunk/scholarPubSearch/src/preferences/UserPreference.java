/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package preferences;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UserPreference complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="UserPreference">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="subjectAreas" type="{http://www.example.org/UserPreference}Rating"/>
 *         &lt;element name="sites" type="{http://www.example.org/UserPreference}Rating"/>
 *         &lt;element name="journals" type="{http://www.example.org/UserPreference}Rating"/>
 *         &lt;element name="authors" type="{http://www.example.org/UserPreference}Rating"/>
 *         &lt;element name="defaultSubject" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="defaultSite" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="defaultJournal" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="defaultAuthor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserPreference", propOrder = {
    "subjectAreas",
    "sites",
    "journals",
    "authors",
    "defaultSubject",
    "defaultSite",
    "defaultJournal",
    "defaultAuthor"
})
public class UserPreference {

    protected Rating subjectAreas;
    protected Rating sites;
    protected Rating journals;
    protected Rating authors;
    protected String defaultSubject;
    protected String defaultSite;
    protected String defaultJournal;
    protected String defaultAuthor;

    /**
     * Gets the value of the subjectAreas property.
     *
     * @return
     *     possible object is
     *     {@link Rating }
     *
     */
    public Rating getSubjectAreas() {
        return subjectAreas;
    }

    /**
     * Sets the value of the subjectAreas property.
     *
     * @param value
     *     allowed object is
     *     {@link Rating }
     *
     */
    public void setSubjectAreas(Rating value) {
        this.subjectAreas = value;
    }

    /**
     * Gets the value of the sites property.
     *
     * @return
     *     possible object is
     *     {@link Rating }
     *
     */
    public Rating getSites() {
        return sites;
    }

    /**
     * Sets the value of the sites property.
     *
     * @param value
     *     allowed object is
     *     {@link Rating }
     *
     */
    public void setSites(Rating value) {
        this.sites = value;
    }

    /**
     * Gets the value of the journals property.
     *
     * @return
     *     possible object is
     *     {@link Rating }
     *
     */
    public Rating getJournals() {
        return journals;
    }

    /**
     * Sets the value of the journals property.
     *
     * @param value
     *     allowed object is
     *     {@link Rating }
     *
     */
    public void setJournals(Rating value) {
        this.journals = value;
    }

    /**
     * Gets the value of the authors property.
     *
     * @return
     *     possible object is
     *     {@link Rating }
     *
     */
    public Rating getAuthors() {
        return authors;
    }

    /**
     * Sets the value of the authors property.
     *
     * @param value
     *     allowed object is
     *     {@link Rating }
     *
     */
    public void setAuthors(Rating value) {
        this.authors = value;
    }

    /**
     * Gets the value of the defaultSubject property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDefaultSubject() {
        return defaultSubject;
    }

    /**
     * Sets the value of the defaultSubject property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDefaultSubject(String value) {
        this.defaultSubject = value;
    }

    /**
     * Gets the value of the defaultSite property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDefaultSite() {
        return defaultSite;
    }

    /**
     * Sets the value of the defaultSite property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDefaultSite(String value) {
        this.defaultSite = value;
    }

    /**
     * Gets the value of the defaultJournal property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDefaultJournal() {
        return defaultJournal;
    }

    /**
     * Sets the value of the defaultJournal property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDefaultJournal(String value) {
        this.defaultJournal = value;
    }

    /**
     * Gets the value of the defaultAuthor property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDefaultAuthor() {
        return defaultAuthor;
    }

    /**
     * Sets the value of the defaultAuthor property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDefaultAuthor(String value) {
        this.defaultAuthor = value;
    }

}

