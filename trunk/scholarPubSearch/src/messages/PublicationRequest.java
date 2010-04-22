/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PublicationRequest complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="PublicationRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="titleKeywordList" type="{http://www.example.org/propose/}StringAttributeList" minOccurs="0"/>
 *         &lt;element name="abstractKeywordList" type="{http://www.example.org/propose/}StringAttributeList" minOccurs="0"/>
 *         &lt;element name="authorList" type="{http://www.example.org/propose/}StringAttributeList" minOccurs="0"/>
 *         &lt;element name="fromYear" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="toYear" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="journalName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subjectArea" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hasFulltextLink" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PublicationRequest", propOrder = {
    "titleKeywordList",
    "abstractKeywordList",
    "authorList",
    "fromYear",
    "toYear",
    "journalName",
    "subjectArea",
    "hasFulltextLink"
})
public class PublicationRequest {

    protected StringAttributeList titleKeywordList;
    protected StringAttributeList abstractKeywordList;
    protected StringAttributeList authorList;
    protected Integer fromYear;
    protected Integer toYear;
    protected String journalName;
    protected String subjectArea;
    protected Boolean hasFulltextLink;

    public PublicationRequest() {
        titleKeywordList = new StringAttributeList();
        abstractKeywordList = new StringAttributeList();
        authorList = new StringAttributeList();
    }

    /**
     * Gets the value of the titleKeywordList property.
     *
     * @return
     *     possible object is
     *     {@link StringAttributeList }
     *
     */
    public StringAttributeList getTitleKeywordList() {
        return titleKeywordList;
    }

    /**
     * Sets the value of the titleKeywordList property.
     *
     * @param value
     *     allowed object is
     *     {@link StringAttributeList }
     *
     */
    public void setTitleKeywordList(StringAttributeList value) {
        this.titleKeywordList = value;
    }

    /**
     * Gets the value of the abstractKeywordList property.
     *
     * @return
     *     possible object is
     *     {@link StringAttributeList }
     *
     */
    public StringAttributeList getAbstractKeywordList() {
        return abstractKeywordList;
    }

    /**
     * Sets the value of the abstractKeywordList property.
     *
     * @param value
     *     allowed object is
     *     {@link StringAttributeList }
     *
     */
    public void setAbstractKeywordList(StringAttributeList value) {
        this.abstractKeywordList = value;
    }

    /**
     * Gets the value of the authorList property.
     *
     * @return
     *     possible object is
     *     {@link StringAttributeList }
     *
     */
    public StringAttributeList getAuthorList() {
        return authorList;
    }

    /**
     * Sets the value of the authorList property.
     *
     * @param value
     *     allowed object is
     *     {@link StringAttributeList }
     *
     */
    public void setAuthorList(StringAttributeList value) {
        this.authorList = value;
    }

    /**
     * Gets the value of the fromYear property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getFromYear() {
        return fromYear;
    }

    /**
     * Sets the value of the fromYear property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setFromYear(Integer value) {
        this.fromYear = value;
    }

    /**
     * Gets the value of the toYear property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getToYear() {
        return toYear;
    }

    /**
     * Sets the value of the toYear property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setToYear(Integer value) {
        this.toYear = value;
    }

    /**
     * Gets the value of the journalName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getJournalName() {
        return journalName;
    }

    /**
     * Sets the value of the journalName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setJournalName(String value) {
        this.journalName = value;
    }

    /**
     * Gets the value of the subjectArea property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSubjectArea() {
        return subjectArea;
    }

    /**
     * Sets the value of the subjectArea property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSubjectArea(String value) {
        this.subjectArea = value;
    }

    /**
     * Gets the value of the hasFulltextLink property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isHasFulltextLink() {
        return hasFulltextLink;
    }

    /**
     * Sets the value of the hasFulltextLink property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setHasFulltextLink(Boolean value) {
        this.hasFulltextLink = value;
    }

}

