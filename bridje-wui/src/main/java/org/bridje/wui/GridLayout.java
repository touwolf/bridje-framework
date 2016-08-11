
package org.bridje.wui;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import org.bridje.web.view.comp.WebComponent;

@XmlRootElement(name = "grid")
@XmlAccessorType(XmlAccessType.FIELD)
public class GridLayout extends BaseContainer
{
    @XmlAttribute(name = "columns")
    private int columns;
    
    @XmlAnyElement(lax = true)
    private List<WebComponent> children;

    public List<WebComponent> getChildren()
    {
        return children;
    }

    public int getColumns()
    {
        if(columns <= 0)
        {
            return 1;
        }
        return columns;
    }

    @Override
    public List<? extends WebComponent> childs()
    {
        return getChildren();
    }
}
