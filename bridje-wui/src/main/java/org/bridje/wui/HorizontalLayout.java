
package org.bridje.wui;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.bridje.web.view.comp.WebComponent;

@XmlRootElement(name = "hlayout")
@XmlAccessorType(XmlAccessType.FIELD)
public class HorizontalLayout extends WebComponent
{
    @XmlAnyElement(lax = true)
    private List<WebComponent> children;

    public List<WebComponent> getChildren()
    {
        return children;
    }

    public void setChildren(List<WebComponent> children)
    {
        this.children = children;
    }

    @Override
    public List<? extends WebComponent> childs()
    {
        return getChildren();
    }
}
