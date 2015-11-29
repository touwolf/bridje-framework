
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
public class HEntityData
{
    @XmlAttribute
    private String name;
    
    @XmlAttribute
    private String extendsFrom;
    
    @XmlAttribute
    private Boolean customizable;
    
    @XmlElements(
    {
        @XmlElement(name = "string", type = HStringField.class), 
        @XmlElement(name = "list", type = HListField.class), 
        @XmlElement(name = "enum", type = HEnumField.class), 
        @XmlElement(name = "boolean", type = HBooleanField.class), 
        @XmlElement(name = "element", type = HElementField.class)
    })
    private java.util.List<HField> fields;
    
    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getExtendsFrom()
    {
        return this.extendsFrom;
    }

    public void setExtendsFrom(String extendsFrom)
    {
        this.extendsFrom = extendsFrom;
    }

    public Boolean getCustomizable()
    {
        if(this.customizable == null)
        {
            this.customizable = false;
        }
        return this.customizable;
    }

    public void setCustomizable(Boolean customizable)
    {
        this.customizable = customizable;
    }

    public java.util.List<HField> getFields()
    {
        return this.fields;
    }

    public void setFields(java.util.List<HField> fields)
    {
        this.fields = fields;
    }

}