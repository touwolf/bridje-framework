<#include "utils.ftl"/>
/**
 <@javaDocContent license!"" />
 */

package ${packageName};

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 <@javaDocContent description!"" true />
 */
@XmlRootElement(name = "${name?lower_case}")
@XmlAccessorType(XmlAccessType.FIELD)
public class ${name} extends ${name}Base
{
}