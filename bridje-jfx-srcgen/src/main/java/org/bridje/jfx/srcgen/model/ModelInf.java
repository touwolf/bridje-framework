
package org.bridje.jfx.srcgen.model;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<ObjectInf> getObjects()
    {
        return objects;
    }

    public String getPackage()
    {
        return packageName;
    }

    public void setPackage(String packageName)
    {
        this.packageName = packageName;
    }

    public List<IncludeInf> getIncludes()
    {
        return includes;
    }

    public void setIncludes(List<IncludeInf> includes)
    {
        this.includes = includes;
    }
    
    public String getFullName()
    {
        return getPackage() + "." + getName();
    }

    public List<PropertyInf> getProperties()
    {
        return properties;
    }

    public void setProperties(List<PropertyInf> properties)
    {
        this.properties = properties;
    }

    public List<IconDef> getIcons()
    {
        return icons;
    }

    public void setIcons(List<IconDef> icons)
    {
        this.icons = icons;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
