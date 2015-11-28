
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
public abstract class HFieldBase
{
    @XmlAttribute
    private String name;
    
    @XmlAttribute
    private Boolean required;
    
    @XmlAttribute
    private Boolean readonly;
    
    @XmlAttribute
    private Boolean isTransient;
    
    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Boolean getRequired()
    {
        if(this.required == null)
        {
            this.required = false;
        }
        return this.required;
    }

    public void setRequired(Boolean required)
    {
        this.required = required;
    }

    public Boolean getReadonly()
    {
        if(this.readonly == null)
        {
            this.readonly = false;
        }
        return this.readonly;
    }

    public void setReadonly(Boolean readonly)
    {
        this.readonly = readonly;
    }

    public Boolean getIsTransient()
    {
        if(this.isTransient == null)
        {
            this.isTransient = false;
        }
        return this.isTransient;
    }

    public void setIsTransient(Boolean isTransient)
    {
        this.isTransient = isTransient;
    }

}