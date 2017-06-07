
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

/**
 * Data object information for a JavaFX model.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ObjectInf
{
    @XmlID
    @XmlAttribute
    private String name;

    private String description;

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
        @XmlElement(name = "table", type = TableComponent.class),
        @XmlElement(name = "treeTable", type = TreeTableComponent.class)
    })
    private List<JFxComponent> components;

    /**
     * The java class name of the object.
     * 
     * @return The java class name of the object.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The java class name of the object.
     * 
     * @param name The java class name of the object.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * A description of this object.
     * 
     * @return A description of this object.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * A description of this object.
     * 
     * @param description A description of this object.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * The properties of this object.
     * 
     * @return The properties of this object.
     */
    public List<PropertyInf> getProperties()
    {
        return properties;
    }

    /**
     * The properties of this object.
     * 
     * @param properties The properties of this object.
     */
    public void setProperties(List<PropertyInf> properties)
    {
        this.properties = properties;
    }

    /**
     * The parent JavaFx model of this object.
     * 
     * @return The parent JavaFx model of this object.
     */
    public ModelInf getModel()
    {
        return model;
    }

    /**
     * The java package of this object.
     * 
     * @return The java package of this object.
     */
    public String getPackage()
    {
        return model.getPackage();
    }

    /**
     * The full java class name for this object.
     * 
     * @return The full java class name for this object.
     */
    public String getFullName()
    {
        return getPackage() + "." + getName();
    }

    /**
     * Called by JAXB.
     * 
     * @param u The unmarshaller.
     * @param parent The parent.
     */
    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        model = (ModelInf) parent;
    }

    /**
     * The key property for this object.
     * 
     * @return The key property for this object.
     */
    public PropertyInf getKeyProperty()
    {
        return keyProperty;
    }

    /**
     * The key property for this object.
     * 
     * @param keyProperty The key property for this object.
     */
    public void setKeyProperty(PropertyInf keyProperty)
    {
        this.keyProperty = keyProperty;
    }

    /**
     * The list of java classes to include.
     * 
     * @return The list of java classes to include.
     */
    public List<IncludeInf> getIncludes()
    {
        return includes;
    }

    /**
     * The list of java classes to include.
     * 
     * @param includes The list of java classes to include.
     */
    public void setIncludes(List<IncludeInf> includes)
    {
        this.includes = includes;
    }

    /**
     * The toString property.
     * 
     * @return The toString property.
     */
    public PropertyInf getToStringProperty()
    {
        if(toStringProperty == null) return keyProperty;
        return toStringProperty;
    }

    /**
     * The toString property.
     * 
     * @param toStringProperty The toString property.
     */
    public void setToStringProperty(PropertyInf toStringProperty)
    {
        this.toStringProperty = toStringProperty;
    }

    /**
     * The components that must be generated for this object.
     * 
     * @return The components that must be generated for this object.
     */
    public List<JFxComponent> getComponents()
    {
        return components;
    }

    /**
     * The components that must be generated for this object.
     * 
     * @param components The components that must be generated for this object.
     */
    public void setComponents(List<JFxComponent> components)
    {
        this.components = components;
    }

    /**
     * All properties for this object including the key and the toString properties.
     * 
     * @return All properties for this object including the key and the toString properties.
     */
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
