
package org.bridje.wui.widgets;

import org.bridje.web.view.widgets.BaseWidget;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.bridje.web.view.widgets.UIExpression;
import org.bridje.web.view.widgets.UIExpressionAdapter;

@XmlRootElement(name = "link")
@XmlAccessorType(XmlAccessType.FIELD)
public class Link extends BaseWidget
{
    @XmlValue
    @XmlJavaTypeAdapter(UIExpressionAdapter.class)
    private UIExpression valueExpression;

    @XmlAttribute(name = "href")
    @XmlJavaTypeAdapter(UIExpressionAdapter.class)
    private UIExpression hrefExpression;

    public String getValue()
    {
        return get(valueExpression, String.class, "");
    }
    
    public String getHref()
    {
        return get(hrefExpression, String.class, "");
    }

    public UIExpression getHrefExpression()
    {
        return hrefExpression;
    }
    
    public UIExpression getValueExpression()
    {
        return valueExpression;
    }
}
