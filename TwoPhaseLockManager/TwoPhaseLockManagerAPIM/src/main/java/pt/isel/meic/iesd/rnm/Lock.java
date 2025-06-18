
package pt.isel.meic.iesd.rnm;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for lock complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="lock">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="vectorId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="element" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lock", propOrder = {
    "vectorId",
    "element"
})
public class Lock {

    protected String vectorId;
    protected int element;

    /**
     * Gets the value of the vectorId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVectorId() {
        return vectorId;
    }

    /**
     * Sets the value of the vectorId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVectorId(String value) {
        this.vectorId = value;
    }

    /**
     * Gets the value of the element property.
     * 
     */
    public int getElement() {
        return element;
    }

    /**
     * Sets the value of the element property.
     * 
     */
    public void setElement(int value) {
        this.element = value;
    }

}
