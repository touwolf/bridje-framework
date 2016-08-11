
package org.bridje.wui.comp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.bridje.web.view.comp.UIExpression;
import org.bridje.web.view.comp.UIExpressionAdapter;
import org.bridje.web.view.comp.WebComponent;

@XmlTransient
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseComponent extends WebComponent
{
    @XmlAttribute(name = "visible")
    @XmlJavaTypeAdapter(UIExpressionAdapter.class)
    private UIExpression visible;

    public boolean getVisible()
    {
        return get(visible, Boolean.class, true);
    }
}
