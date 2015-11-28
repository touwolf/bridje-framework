
package org.bridje.maven.plugin.hmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class HField extends HFieldBase
{
    public abstract String getJavaType();
    
    public abstract boolean getIsList();
    
    public boolean getIsNullable()
    {
        return true;
    }
}