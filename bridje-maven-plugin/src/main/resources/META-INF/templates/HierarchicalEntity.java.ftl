<#include "utils.ftl" />

package ${model.package};

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ${name}<#if extends??> extends ${extends}</#if>
{
    <@entityContent />
}