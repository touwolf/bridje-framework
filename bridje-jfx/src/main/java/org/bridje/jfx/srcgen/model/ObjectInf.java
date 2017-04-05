
package org.bridje.jfx.srcgen.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public class ObjectInf
{
    @XmlID
    @XmlAttribute
    private String name;

    @XmlElements(
    {
        @XmlElement(name = "key", type = PropertyInf.class)
    })
    private PropertyInf keyProperty;

    @XmlElements(
    {
        @XmlElement(name = "toString", type = PropertyInf.class)
    })
    private PropertyInf toStringProperty;
    
    @XmlElementWrapper(name = "properties")
    @XmlElements(
    {
        @XmlElement(name = "observableList", type = ObListPropertyInf.class),
        @XmlElement(name = "property", type = PropertyInf.class)
    })
    private List<PropertyInf> properties;

    @XmlElementWrapper(name = "includes")
    @XmlElements(
    {
        @XmlElement(name = "include", type = IncludeInf.class)
    })
    private List<IncludeInf> includes;

    @XmlTransient
    private ModelInf model;
    
    @XmlTransient
    private List<PropertyInf> allProperties;
    
    @XmlElementWrapper(name = "components")
    @XmlElements(
    {
        @XmlElement(name = "table", type = TableComponent.class)
    })
    private List<JFxComponent> components;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<PropertyInf> getProperties()
    {
        return properties;
    }

    public void setProperties(List<PropertyInf> properties)
    {
        this.properties = properties;
    }

    public ModelInf getModel()
    {
        return model;
    }

    public String getPackage()
    {
        return model.getPackage();
    }

    public String getFullName()
    {
        return getPackage() + "." + getName();
    }

    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        model = (ModelInf) parent;
    }

    public PropertyInf getKeyProperty()
    {
        return keyProperty;
    }

    public void setKeyProperty(PropertyInf keyProperty)
    {
        this.keyProperty = keyProperty;
    }

    public List<IncludeInf> getIncludes()
    {
        return includes;
    }

    public void setIncludes(List<IncludeInf> includes)
    {
        this.includes = includes;
    }

    public PropertyInf getToStringProperty()
    {
        if(toStringProperty == null) return keyProperty;
        return toStringProperty;
    }

    public void setToStringProperty(PropertyInf toStringProperty)
    {
        this.toStringProperty = toStringProperty;
    }

    public List<JFxComponent> getComponents()
    {
        return components;
    }

    public void setComponents(List<JFxComponent> components)
    {
        this.components = components;
    }

    public List<PropertyInf> getAllProperties()
    {
        if(allProperties == null)
        {
            allProperties = new ArrayList<>();
            if(keyProperty != null) allProperties.add(keyProperty);
            allProperties.addAll(properties);
            if(toStringProperty != null) allProperties.add(toStringProperty);
        }
        return allProperties;
    }
}
