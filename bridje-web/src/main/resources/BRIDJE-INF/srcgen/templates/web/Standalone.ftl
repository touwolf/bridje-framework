<#ftl encoding="UTF-8">

package ${uisuite.package};

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import org.bridje.web.view.*;
import org.bridje.web.view.controls.Control;

/**
 * Defines a view that will not extend from any layout, an standalone view.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ${uisuite.name}Standalone implements ViewDefinition, Standalone
{
    <#if uisuite.standalone?? && uisuite.standalone.content??>
    @XmlElements(
    {
        <#list uisuite.standalone.content![] as c>
        @XmlElement( name = "${c.name}", type = ${c.type}.class ),
        </#list>
    })
    <#else>
    @XmlTransient
    </#if>
    private Control root;

    @Override
    public Control findRoot()
    {
        return root;
    }
}
