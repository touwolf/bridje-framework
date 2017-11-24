
package org.bridje.jfx.srcgen.model;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;

@XmlAccessorType(XmlAccessType.FIELD)
public class PropertyInf
{
    private static Map<String,String> PROPERTIES_CLASSES;

    @XmlID
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String type;
    
    @XmlAttribute
    private String description;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getJavaType()
    {
        return getType();
    }

    public String getPropertyDec()
    {
        String propClasses = getPropertiesClasses().get(getJavaType());
        if(propClasses != null)
        {
            return propClasses;
        }
        return "SimpleObjectProperty<" + getJavaType() + ">";
    }

    public String getPropertyDimDec()
    {
        String propClasses = getPropertiesClasses().get(getJavaType());
        if(propClasses != null)
        {
            return propClasses;
        }
        return "SimpleObjectProperty<>";
    }

    public String getConvertProp()
    {
        if(isNumber())
        {
            return "asObject";
        }
        return null;
    }

    public static Map<String,String> getPropertiesClasses()
    {
        if(PROPERTIES_CLASSES == null)
        {
            PROPERTIES_CLASSES = new HashMap<>();
            PROPERTIES_CLASSES.put("Boolean", "SimpleBooleanProperty");
            PROPERTIES_CLASSES.put("Integer", "SimpleIntegerProperty");
            PROPERTIES_CLASSES.put("Long", "SimpleLongProperty");
            PROPERTIES_CLASSES.put("Double", "SimpleDoubleProperty");
            PROPERTIES_CLASSES.put("String", "SimpleStringProperty");
        }
        return PROPERTIES_CLASSES;
    }

    private boolean isNumber()
    {
        switch(getJavaType())
        {
            case "Byte":
            case "Short":
            case "Integer":
            case "Long":
            case "Float":
            case "Double":
                return true;
        }
        return false;
    }
}
