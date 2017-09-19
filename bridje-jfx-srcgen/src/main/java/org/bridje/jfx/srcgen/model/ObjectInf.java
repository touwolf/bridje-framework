
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

    @XmlAttribute
    private String key;
    
    @XmlAttribute
    private String toString;
    
    @XmlTransient
    private PropertyInf keyProperty;

    @XmlTransient
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

    @XmlAttribute
    private String base;

    @XmlElementWrapper(name = "mappings")
    @XmlElements(
    {
        @XmlElement(name = "mapping", type = ObjectInfMapping.class)
    })
    private List<ObjectInfMapping> mappings;

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
        if(properties == null)
        {
            properties = new ArrayList<>();
        }
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
        if(keyProperty == null && key != null)
        {
            keyProperty = findProperty(key);
        }
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
        if(toString == null && key != null)
        {
            toString = key;
        }
        if(toStringProperty == null && toString != null) 
        {
            toStringProperty = findProperty(toString);
        }
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
     * The base object.
     * 
     * @return The base object.
     */
    public String getBase()
    {
        return base;
    }

    /**
     * The base object.
     * 
     * @param base The base object.
     */
    public void setBase(String base)
    {
        this.base = base;
    }

    /**
     * The list of mappings for this object.
     * 
     * @return The list of mappings for this object.
     */
    public List<ObjectInfMapping> getMappings()
    {
        return mappings;
    }

    /**
     * The list of mappings for this object.
     * 
     * @param mappings The list of mappings for this object.
     */
    public void setMappings(List<ObjectInfMapping> mappings)
    {
        this.mappings = mappings;
    }
    
    /**
     * 
     * @param propName
     * @return 
     */
    public PropertyInf findProperty(String propName)
    {
        for (PropertyInf property : getProperties())
        {
            if(property.getName().equals(propName))
            {
                return property;
            }
        }
        return null;
    }

    /**
     * 
     * @param propName
     * @return 
     */
    public boolean isList(String propName)
    {
        PropertyInf prop = findProperty(propName);
        if(prop != null) return prop.getIsList();
        return false;
    }

    /**
     * 
     * @param propName
     * @return 
     */
    public boolean isObject(String propName)
    {
        PropertyInf prop = findProperty(propName);
        if(prop != null) return model.isObject(prop.getType());
        return false;
    }

    /**
     * 
     * @param propName
     * @return 
     */
    public ObjectInf findObject(String propName)
    {
        PropertyInf prop = findProperty(propName);
        if(prop != null) return model.findObject(prop.getType());
        return null;
    }
}
