/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package messages;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Publication complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Publication">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="authorList" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="author" type="{http://www.example.org/propose/}PersonType" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="year" type="{http://www.w3.org/2005/Atom}int" minOccurs="0"/>
 *         &lt;element name="journal" type="{http://www.example.org/propose/}JournalType" minOccurs="0"/>
 *         &lt;element name="summary" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fulltext" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="bibtex" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subjectArea" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pagesCount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="source" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ratingPosition" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Publication", propOrder = {
    "id",
    "title",
    "authorList",
    "year",
    "journal",
    "summary",
    "fulltext",
    "bibtex",
    "subjectArea",
    "pagesCount",
    "source",
    "ratingPosition"
})
public class Publication {

    @XmlSchemaType(name = "anyURI")
    protected String id;
    @XmlElement(required = true)
    protected String title;
    protected Publication.AuthorList authorList;
    protected Integer year;
    protected JournalType journal;
    protected String summary;
    @XmlSchemaType(name = "anyURI")
    protected String fulltext;
    protected String bibtex;
    protected String subjectArea;
    protected String pagesCount;
    @XmlElement(required = true)
    protected String source;
    protected int ratingPosition;

    /**
     * Gets the value of the id property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the title property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the authorList property.
     *
     * @return
     *     possible object is
     *     {@link Publication.AuthorList }
     *
     */
    public Publication.AuthorList getAuthorList() {
        if (authorList == null) {
                authorList = new Publication.AuthorList();
            }
        return authorList;
    }

    /**
     * Sets the value of the authorList property.
     *
     * @param value
     *     allowed object is
     *     {@link Publication.AuthorList }
     *
     */
    public void setAuthorList(Publication.AuthorList value) {
        this.authorList = value;
    }

    /**
     * Gets the value of the year property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getYear() {
        return year;
    }

    /**
     * Sets the value of the year property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setYear(Integer value) {
        this.year = value;
    }

    /**
     * Gets the value of the journal property.
     *
     * @return
     *     possible object is
     *     {@link JournalType }
     *
     */
    public JournalType getJournal() {
        return journal;
    }

    /**
     * Sets the value of the journal property.
     *
     * @param value
     *     allowed object is
     *     {@link JournalType }
     *
     */
    public void setJournal(JournalType value) {
        this.journal = value;
    }

    /**
     * Gets the value of the summary property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Sets the value of the summary property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSummary(String value) {
        this.summary = value;
    }

    /**
     * Gets the value of the fulltext property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFulltext() {
        return fulltext;
    }

    /**
     * Sets the value of the fulltext property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFulltext(String value) {
        this.fulltext = value;
    }

    /**
     * Gets the value of the bibtex property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBibtex() {
        return bibtex;
    }

    /**
     * Sets the value of the bibtex property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBibtex(String value) {
        this.bibtex = value;
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
     * Gets the value of the pagesCount property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPagesCount() {
        return pagesCount;
    }

    /**
     * Sets the value of the pagesCount property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPagesCount(String value) {
        this.pagesCount = value;
    }

    /**
     * Gets the value of the source property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSource(String value) {
        this.source = value;
    }

    /**
     * Gets the value of the ratingPosition property.
     *
     */
    public int getRatingPosition() {
        return ratingPosition;
    }

    /**
     * Sets the value of the ratingPosition property.
     *
     */
    public void setRatingPosition(int value) {
        this.ratingPosition = value;
    }


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
     *         &lt;element name="author" type="{http://www.example.org/propose/}PersonType" maxOccurs="unbounded" minOccurs="0"/>
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
        "author"
    })
    public static class AuthorList {

        protected List<PersonType> author;

        /**
         * Gets the value of the author property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the author property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAuthor().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link PersonType }
         *
         *
         */
        public List<PersonType> getAuthor() {
            if (author == null) {
                author = new ArrayList<PersonType>();
            }
            return this.author;
        }

        public void setAuthor(List<PersonType> authorList) {
            author = authorList;
        }

        public void addAuthor(PersonType auth) {
            if (author == null) {
                author = new ArrayList<PersonType>();
            }
            author.add(auth);
        }
        
    }

}