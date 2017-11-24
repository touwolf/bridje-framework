
package org.bridje.jfx.srcgen.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class ObListPropertyInf extends PropertyInf
{
    @Override
    public String getJavaType()
    {
        return "ObservableList<" + getType() + ">";
    }
}
