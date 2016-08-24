
package org.bridje.wui.comp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.bridje.web.view.comp.UIExpression;
import org.bridje.web.view.comp.UIExpressionAdapter;

@XmlRootElement(name = "header")
@XmlAccessorType(XmlAccessType.FIELD)
public class Header extends BaseComponent
{
    @XmlValue
    @XmlJavaTypeAdapter(UIExpressionAdapter.class)
    private UIExpression valueExpression;

    public String getValue()
    {
        return get(valueExpression, String.class, "");
    }

    public UIExpression getValueExpression()
    {
        return valueExpression;
    }

    public void setValueExpression(UIExpression valueExpression)
    {
        this.valueExpression = valueExpression;
    }
}
