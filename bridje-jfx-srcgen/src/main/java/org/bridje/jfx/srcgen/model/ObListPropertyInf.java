
package org.bridje.jfx.srcgen.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * An observable list property.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ObListPropertyInf extends PropertyInf
{
    /**
     * The real java type for this property.
     * 
     * @return The real java type for this property.
     */
    @Override
    public String getJavaType()
    {
        return "ObservableList<" + getType() + ">";
    }
}
