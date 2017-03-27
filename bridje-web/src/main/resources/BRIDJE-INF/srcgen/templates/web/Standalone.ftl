<#ftl encoding="UTF-8">

package ${uisuite.package};

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import org.bridje.web.view.*;
import org.bridje.web.view.controls.Control;

/**
 * Defines a view that will not extend from any layout, an standalone view.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ${uisuite.name}Standalone implements ViewDefinition, Standalone
{
    @XmlAnyElement(lax = true)
    private Control root;

    @Override
    public Control findRoot()
    {
        return root;
    }
}
