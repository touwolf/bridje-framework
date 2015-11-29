<#include "utils.ftl"/>

package ${package};

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 */
@XmlRootElement(name = "${name?lower_case}")
@XmlAccessorType(XmlAccessType.FIELD)
public class ${name} extends ${name}Base
{
}