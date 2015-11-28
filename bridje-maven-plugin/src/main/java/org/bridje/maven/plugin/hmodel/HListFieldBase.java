
package org.bridje.maven.plugin.hmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.Unmarshaller;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class HListFieldBase extends HField
{
    @XmlAttribute
    private String of;
    
    @XmlAttribute
    private Boolean wrapper;
    
    @XmlElements(
    {
        @XmlElement(name = "element", type = HListElement.class)
    })
    private java.util.List<HListElement> elements;
    
    public String getOf()
    {
        return this.of;
    }

    public void setOf(String of)
    {
        this.of = of;
    }

    public Boolean getWrapper()
    {
        return this.wrapper;
    }

    public void setWrapper(Boolean wrapper)
    {
        this.wrapper = wrapper;
    }

    public java.util.List<HListElement> getElements()
    {
        return this.elements;
    }

    public void setElements(java.util.List<HListElement> elements)
    {
        this.elements = elements;
    }

}