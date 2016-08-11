
package org.bridje.wui.comp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import org.bridje.web.view.comp.WebComponent;

/**
 * A region for the border layout, each region contains one component.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BorderLayoutRegion
{
    @XmlAnyElement(lax = true)
    private WebComponent component;

    /**
     * Gets the component of this region.
     * 
     * @return The component of this region.
     */
    public WebComponent getComponent()
    {
        return component;
    }
}
