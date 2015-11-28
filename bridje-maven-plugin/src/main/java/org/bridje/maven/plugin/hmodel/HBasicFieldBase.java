
package org.bridje.maven.plugin.hmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.Unmarshaller;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class HBasicFieldBase extends HField
{
    @XmlAttribute
    private HFieldAccess access;
    
    public HFieldAccess getAccess()
    {
        return this.access;
    }

    public void setAccess(HFieldAccess access)
    {
        this.access = access;
    }

}