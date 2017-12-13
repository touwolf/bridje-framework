
package org.bridje.jfx.srcgen.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A JavaFX application model.
 */
@XmlRootElement(name = "model")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModelInf
{
    @XmlID
    @XmlAttribute
    private String name;

    @XmlAttribute(name = "package")
    private String packageName;

    private String description;

    @XmlElementWrapper(name = "objects")
    @XmlElements(
            {
                @XmlElement(name = "object", type = ObjectInf.class)
            })
    private List<ObjectInf> objects;

    @XmlElementWrapper(name = "icons")
    @XmlElements(
            {
                @XmlElement(name = "icon", type = IconDef.class)
            })
    private List<IconDef> icons;

    @XmlElementWrapper(name = "properties")
    @XmlElements(
            {
                @XmlElement(name = "observableList", type = ObListPropertyInf.class)
                ,
        @XmlElement(name = "property", type = PropertyInf.class)
            })
    private List<PropertyInf> properties;

    @XmlElementWrapper(name = "includes")
    @XmlElements(
            {
                @XmlElement(name = "include", type = IncludeInf.class)
            })
    private List<IncludeInf> includes;

    /**
     * The name of the model.
     *
     * @return The name of the model.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The name of the model.
     *
     * @param name The name of the model.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The objects for this model.
     *
     * @return The objects for this model.
     */
    public List<ObjectInf> getObjects()
    {
        return objects;
    }

    /**
     * The package for this model.
     *
     * @return The package for this model.
     */
    public String getPackage()
    {
        return packageName;
    }

    /**
     * The package for this model.
     *
     * @param packageName The package for this model.
     */
    public void setPackage(String packageName)
    {
        this.packageName = packageName;
    }

    /**
     * A list of java classes to include.
     *
     * @return A list of java classes to include.
     */
    public List<IncludeInf> getIncludes()
    {
        return includes;
    }

    /**
     * A list of java classes to include.
     *
     * @param includes A list of java classes to include.
     */
    public void setIncludes(List<IncludeInf> includes)
    {
        this.includes = includes;
    }

    /**
     * Gets the full name for the model class.
     *
     * @return The full name for the model class.
     */
    public String getFullName()
    {
        return getPackage() + "." + getName();
    }

    /**
     * The properties of the model class.
     *
     * @return The properties of the model class.
     */
    public List<PropertyInf> getProperties()
    {
        return properties;
    }

    /**
     * The properties of the model class.
     *
     * @param properties The properties of the model class.
     */
    public void setProperties(List<PropertyInf> properties)
    {
        this.properties = properties;
    }

    /**
     * The icons for this model.
     *
     * @return The icons for this model.
     */
    public List<IconDef> getIcons()
    {
        if (icons == null)
        {
            icons = new ArrayList<>();
        }
        return icons;
    }

    /**
     * The icons for this model.
     *
     * @param icons The icons for this model.
     */
    public void setIcons(List<IconDef> icons)
    {
        this.icons = icons;
    }

    /**
     * The description of the model.
     *
     * @return The description of the model.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * The description of the model.
     *
     * @param description The description of the model.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Find an object by its name.
     *
     * @param name The name of the object.
     *
     * @return The object found, or null if it does not exists.
     */
    public ObjectInf findObject(String name)
    {
        for (ObjectInf obj : getObjects())
        {
            if (obj.getName().equals(name))
            {
                return obj;
            }
        }
        return null;
    }

    /**
     * Determines if the given name is an object.
     *
     * @param name The name of the object.
     *
     * @return true the given name is an object, false otherwise.
     */
    public boolean isObject(String name)
    {
        return findObject(name) != null;
    }
}
