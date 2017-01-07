
package org.bridje.web.view.test.controls;

import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import org.bridje.web.view.Defines;
import org.bridje.web.view.controls.Control;
import org.bridje.web.view.controls.ControlPlaceHolder;

@XmlRootElement(name = "vlayout")
@XmlAccessorType(XmlAccessType.FIELD)
public class VerticalLayout extends BaseContainer
{
    @XmlElements(
    {
        @XmlElement(name = "placeholder", type = ControlPlaceHolder.class),
        @XmlElement(name = "header", type = Header.class)
    })
    private List<Control> children;

    public List<Control> getChildren()
    {
        return children;
    }

    public void setChildren(List<Control> children)
    {
        this.children = children;
    }

    @Override
    public List<? extends Control> childs()
    {
        return getChildren();
    }

    @Override
    public void doOverride(Map<String, Defines> definesMap)
    {
        children = Control.doOverride(children, definesMap);
    }
}
