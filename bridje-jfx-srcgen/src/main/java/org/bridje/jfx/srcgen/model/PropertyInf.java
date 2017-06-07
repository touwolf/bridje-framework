
package org.bridje.jfx.srcgen.model;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;

/**
 * Information for a property of a data object.
 */
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

    /**
     * The java name for the property.
     * 
     * @return The java name for the property.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The java name for the property.
     * 
     * @param name The java name for the property.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The type for the property.
     * 
     * @return The type for the property.
     */
    public String getType()
    {
        return type;
    }

    /**
     * The type for the property.
     * 
     * @param type The type for the property.
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * The description of the property.
     * 
     * @return The description of the property.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * The description of the property.
     * 
     * @param description The description of the property.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * The java type of the property.
     * 
     * @return The java type of the property.
     */
    public String getJavaType()
    {
        return getType();
    }

    /**
     * The declaration code for this property.
     * 
     * @return The declaration code for this property.
     */
    public String getPropertyDec()
    {
        String propClasses = getPropertiesClasses().get(getJavaType());
        if(propClasses != null)
        {
            return propClasses;
        }
        return "SimpleObjectProperty<" + getJavaType() + ">";
    }

    /**
     * The instantation code for this property.
     * 
     * @return The instantation code for this property.
     */
    public String getPropertyDimDec()
    {
        String propClasses = getPropertiesClasses().get(getJavaType());
        if(propClasses != null)
        {
            return propClasses;
        }
        return "SimpleObjectProperty<>";
    }

    /**
     * The code for the property convertion to object.
     * 
     * @return The code for the property convertion to object.
     */
    public String getConvertProp()
    {
        if(isNumber()) return "asObject";
        return null;
    }

    /**
     * A map with de properties that must be use for each java raw type.
     * 
     * @return A map with de properties that must be use for each java raw type.
     */
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

    /**
     * Determines when ever this property es a number property.
     * 
     * @return Determines when ever this property es a number property.
     */
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
