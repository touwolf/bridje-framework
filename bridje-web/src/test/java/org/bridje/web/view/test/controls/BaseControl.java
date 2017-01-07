
package org.bridje.web.view.test.controls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.bridje.web.view.controls.UIExpression;
import org.bridje.web.view.controls.UIExpressionAdapter;
import org.bridje.web.view.controls.Control;

@XmlTransient
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class BaseControl extends Control
{
    @XmlAttribute(name = "visible")
    @XmlJavaTypeAdapter(UIExpressionAdapter.class)
    private UIExpression visible;

    public boolean getVisible()
    {
        return get(visible, Boolean.class, true);
    }
}
