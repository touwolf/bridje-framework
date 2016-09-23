
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
    @XmlAttribute(name = "visible")
    @XmlJavaTypeAdapter(UIExpressionAdapter.class)
    private UIExpression visible;

    public boolean getVisible()
    {
        return get(visible, Boolean.class, true);
    }
}
