
package org.bridje.data.hmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HElementField extends HElementFieldBase
{

    @Override
    public String getJavaType()
    {
        return getType();
    }

    @Override
    public boolean getIsList()
    {
        return false;
    }
}