
package org.bridje.wui;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.bridje.web.view.comp.UIExpression;
import org.bridje.web.view.comp.UIExpressionAdapter;

/**
 * Base class for all container components. A container is a component that has
 * other components as children of his.
 */
@XmlTransient
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseContainer extends BaseComponent
{
    @XmlJavaTypeAdapter(UIExpressionAdapter.class)
    @XmlAttribute(name = "childClass")
    private UIExpression childStyleClass;

    /**
     * Gets the style class to be apply to the childrens.
     * 
     * @return The style class to be apply to the childerns
     */
    public String getChildStyleClass()
    {
        return get(childStyleClass, String.class, "");
    }
}
