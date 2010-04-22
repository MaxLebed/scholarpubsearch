/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package preferences;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Rating complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Rating">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ratingElem" type="{http://www.example.org/UserPreference}RatingElem" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Rating", propOrder = {
    "ratingElem"
})
public class Rating {

    protected List<RatingElem> ratingElem;

    /**
     * Gets the value of the ratingElem property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ratingElem property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRatingElem().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RatingElem }
     *
     *
     */
    public List<RatingElem> getRatingElem() {
        if (ratingElem == null) {
            ratingElem = new ArrayList<RatingElem>();
        }
        return this.ratingElem;
    }

}
