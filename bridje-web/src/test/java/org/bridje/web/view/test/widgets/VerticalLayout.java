
package org.bridje.web.view.test.widgets;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.bridje.web.view.widgets.Widget;

@XmlRootElement(name = "vlayout")
@XmlAccessorType(XmlAccessType.FIELD)
public class VerticalLayout extends BaseContainer
{
    @XmlAnyElement(lax = true)
    private List<Widget> children;

    public List<Widget> getChildren()
    {
        return children;
    }

    public void setChildren(List<Widget> children)
    {
        this.children = children;
    }

    @Override
    public List<? extends Widget> childs()
    {
        return getChildren();
    }
}
