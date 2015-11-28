
package org.bridje.maven.plugin.hmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HBooleanField extends HBooleanFieldBase
{
    @Override
    public String getJavaType()
    {
        return "Boolean";
    }

    @Override
    public String getXmlType()
    {
        return "boolean";
    }

    @Override
    public String getDefaultValueExp()
    {
        return String.valueOf(getDefaultValue());
    }
}
