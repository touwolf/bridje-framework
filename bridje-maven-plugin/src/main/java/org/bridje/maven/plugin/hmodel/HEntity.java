
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
public class HEntity extends HEntityData
{
    private HParentField parent;
    
    public HParentField getParent()
    {
        return this.parent;
    }

    public void setParent(HParentField parent)
    {
        this.parent = parent;
    }

    @XmlTransient
    private HModel model;

    public HModel getModel()
    {
        return this.model;
    }

    void setModel(HModel model)
    {
        this.model = model;
    }

    public void afterUnmarshal(Unmarshaller unmarshaller, Object parent)
    {
        if(parent instanceof HModel)
        {
            setModel((HModel)parent);
        }
    }

}