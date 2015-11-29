<#include "utils.ftl"/>
<@javaDocLicense license />

package ${package};

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * ${description!""}
 */
@XmlRootElement(name = "${name?lower_case}")
@XmlAccessorType(XmlAccessType.FIELD)
public class ${name} extends ${name}Base
{
}