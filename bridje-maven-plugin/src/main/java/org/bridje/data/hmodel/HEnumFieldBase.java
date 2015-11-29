
package org.bridje.data.hmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.Unmarshaller;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class HEnumFieldBase extends HBasicField
{
    @XmlAttribute
    private String type;
    
    public String getType()
    {
        return this.type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

}