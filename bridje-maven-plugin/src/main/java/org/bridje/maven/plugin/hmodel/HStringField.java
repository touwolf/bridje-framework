
package org.bridje.maven.plugin.hmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HStringField extends HStringFieldBase
{

    @Override
    public String getJavaType()
    {
        return "String";
    }

    @Override
    public String getXmlType()
    {
        return "string";
    }

    @Override
    public String getDefaultValueExp()
    {
        return "\"" + getDefaultValue() + "\"";
    }
}
