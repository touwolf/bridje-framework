<#ftl encoding="UTF-8">

package ${enum.package};

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import javax.annotation.Generated;

/**
 * This object represents the ${enum.name} enumerator for the ${uisuite.name} Theme.
 */
@Generated(value = "org.bridje.web.srcgen.WebSourceGenerator", date = "${.now?string("yyyy-MM-dd")}", comments = "Generated by Bridje Web API")
@XmlType
@XmlEnum
public enum ${enum.name}
{
    <#list enum.constants![] as ct>
    @XmlEnumValue("${ct.value}")
    ${ct.name}<#sep>, </#sep>
    </#list>;
}
