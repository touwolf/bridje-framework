
package org.bridje.maven.plugin.hmodel;

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
public abstract class HBooleanFieldBase extends HBasicField
{
    @XmlAttribute
    private Boolean defaultValue;
    
    public Boolean getDefaultValue()
    {
        return this.defaultValue;
    }

    public void setDefaultValue(Boolean defaultValue)
    {
        this.defaultValue = defaultValue;
    }

}