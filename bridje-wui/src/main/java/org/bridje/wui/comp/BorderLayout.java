
package org.bridje.wui.comp;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.bridje.web.view.comp.WebComponent;

/**
 * A layout that contains a central region and 4 regions surronding it.
 */
@XmlRootElement(name = "borderlayout")
@XmlAccessorType(XmlAccessType.FIELD)
public class BorderLayout extends BaseContainer
{
    private BorderLayoutRegion top;

    private BorderLayoutRegion left;

    private BorderLayoutRegion center;

    private BorderLayoutRegion right;

    private BorderLayoutRegion bottom;

    /**
     * Gets the web component at the top region.
     * 
     * @return The web component at the top region.
     */
    public WebComponent getTop()
    {
        if (top != null)
        {
            return top.getComponent();
        }
        return null;
    }

    /**
     * Gets the web component at the left region.
     * 
     * @return The web component at the left region.
     */
    public WebComponent getLeft()
    {
        if (left != null)
        {
            return left.getComponent();
        }
        return null;
    }

    /**
     * Gets the web component at the center region.
     * 
     * @return The web component at the center region.
     */
    public WebComponent getCenter()
    {
        if (center != null)
        {
            return center.getComponent();
        }
        return null;
    }

    /**
     * Gets the web component at the right region.
     * 
     * @return The web component at the right region.
     */
    public WebComponent getRight()
    {
        if (right != null)
        {
            return right.getComponent();
        }
        return null;
    }

    /**
     * Gets the web component at the bottom region.
     * 
     * @return The web component at the bottom region.
     */
    public WebComponent getBottom()
    {
        if (bottom != null)
        {
            return bottom.getComponent();
        }
        return null;
    }

    @Override
    public List<? extends WebComponent> childs()
    {
        List<WebComponent> comps = new ArrayList<>();
        if (getTop() != null)
        {
            comps.add(getTop());
        }
        if (getLeft() != null)
        {
            comps.add(getLeft());
        }
        if (getRight() != null)
        {
            comps.add(getRight());
        }
        if (getBottom() != null)
        {
            comps.add(getBottom());
        }
        if (getCenter() != null)
        {
            comps.add(getCenter());
        }
        return comps;
    }
}
