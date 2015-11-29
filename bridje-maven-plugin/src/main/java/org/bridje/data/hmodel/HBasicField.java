
package org.bridje.data.hmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class HBasicField extends HBasicFieldBase
{
    public abstract String getXmlType();

    public abstract String getDefaultValueExp();
    
    @Override
    public boolean getIsList()
    {
        return false;
    }
}