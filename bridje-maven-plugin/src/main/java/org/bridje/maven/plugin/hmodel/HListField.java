
package org.bridje.maven.plugin.hmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HListField extends HListFieldBase
{
    @Override
    public String getJavaType()
    {
        return "java.util.List<" + getOf() + ">";
    }

    @Override
    public boolean getIsList()
    {
        return true;
    }
}
