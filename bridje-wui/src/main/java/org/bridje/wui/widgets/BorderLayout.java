
package org.bridje.wui.widgets;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.bridje.web.view.widgets.Widget;

/**
 * A layout that contains a central region and 4 regions surrounding it.
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
     * Gets the widget at the top region.
     * 
     * @return The widget at the top region.
     */
    public Widget getTop()
    {
        if (top != null)
        {
            return top.getContent();
        }
        return null;
    }

    /**
     * Gets the widget at the left region.
     * 
     * @return The widget at the left region.
     */
    public Widget getLeft()
    {
        if (left != null)
        {
            return left.getContent();
        }
        return null;
    }

    /**
     * Gets the widget at the center region.
     * 
     * @return The widget at the center region.
     */
    public Widget getCenter()
    {
        if (center != null)
        {
            return center.getContent();
        }
        return null;
    }

    /**
     * Gets the widget at the right region.
     * 
     * @return The widget at the right region.
     */
    public Widget getRight()
    {
        if (right != null)
        {
            return right.getContent();
        }
        return null;
    }

    /**
     * Gets the widget at the bottom region.
     * 
     * @return The widget at the bottom region.
     */
    public Widget getBottom()
    {
        if (bottom != null)
        {
            return bottom.getContent();
        }
        return null;
    }

    @Override
    public List<? extends Widget> childs()
    {
        List<Widget> widgets = new ArrayList<>();
        if (getTop() != null)
        {
            widgets.add(getTop());
        }
        if (getLeft() != null)
        {
            widgets.add(getLeft());
        }
        if (getRight() != null)
        {
            widgets.add(getRight());
        }
        if (getBottom() != null)
        {
            widgets.add(getBottom());
        }
        if (getCenter() != null)
        {
            widgets.add(getCenter());
        }
        return widgets;
    }
}
