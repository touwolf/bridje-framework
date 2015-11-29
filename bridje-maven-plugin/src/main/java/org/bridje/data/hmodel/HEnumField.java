
package org.bridje.data.hmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HEnumField extends HEnumFieldBase
{

    @Override
    public String getXmlType()
    {
        return "enum";
    }

    @Override
    public String getJavaType()
    {
        return getType();
    }

    @Override
    public String getDefaultValueExp()
    {
        return null;
    }
}