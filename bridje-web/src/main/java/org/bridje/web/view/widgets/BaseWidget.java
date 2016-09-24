
package org.bridje.web.view.widgets;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlTransient
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseWidget extends Widget
{
    @XmlAttribute
    private String id;

    @XmlJavaTypeAdapter(UIExpressionAdapter.class)
    @XmlAttribute(name = "class")
    private UIExpression styleClass;

    /**
     * Gets the widget`s id.
     * 
     * @return The widget´s id.
     */
    public String getId()
    {
        return id;
    }

    /**
     * Sets the widget`s id.
     * 
     * @param id The widget´s id.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * Gets the HTML style class for this component.
     * 
     * @return The space separated HTML style classes.
     */
    public String getStyleClass()
    {
        return get(styleClass, String.class, "");
    }

    @XmlAttribute(name = "visible")
    @XmlJavaTypeAdapter(UIExpressionAdapter.class)
    private UIExpression visible;

    public boolean getVisible()
    {
        return get(visible, Boolean.class, true);
    }
}
