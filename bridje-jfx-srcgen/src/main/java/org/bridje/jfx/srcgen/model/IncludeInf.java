
package org.bridje.jfx.srcgen.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Java class include information.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class IncludeInf
{
    @XmlAttribute
    private String fullName;

    /**
     * Gets the full java class name for this include.
     * 
     * @return The full java class name for this include.
     */
    public String getFullName()
    {
        return fullName;
    }

    /**
     * Sets the full java class name for this include.
     * 
     * @param fullName The full java class name for this include.
     */
    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }
}
