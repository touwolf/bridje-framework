
package org.bridje.web.view.test.widgets;

import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import org.bridje.web.view.Defines;
import org.bridje.web.view.widgets.Widget;
import org.bridje.web.view.widgets.WidgetPlaceHolder;

@XmlRootElement(name = "vlayout")
@XmlAccessorType(XmlAccessType.FIELD)
public class VerticalLayout extends BaseContainer
{
    @XmlElements(
    {
        @XmlElement(name = "placeholder", type = WidgetPlaceHolder.class),
        @XmlElement(name = "header", type = Header.class)
    })
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

    @Override
    public void doOverride(Map<String, Defines> definesMap)
    {
        children = Widget.doOverride(children, definesMap);
    }
}
