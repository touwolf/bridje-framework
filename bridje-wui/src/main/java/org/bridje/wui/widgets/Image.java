
package org.bridje.wui.widgets;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.bridje.web.view.widgets.UIExpression;
import org.bridje.web.view.widgets.UIExpressionAdapter;

@XmlRootElement(name = "image")
@XmlAccessorType(XmlAccessType.FIELD)
public class Image extends BaseWidget
{
    @XmlAttribute(name = "src")
    @XmlJavaTypeAdapter(UIExpressionAdapter.class)
    private UIExpression srcExpression;

    @XmlAttribute(name = "href")
    @XmlJavaTypeAdapter(UIExpressionAdapter.class)
    private UIExpression hrefExpression;

    public String getSrc()
    {
        return get(srcExpression, String.class, "");
    }
    
    public String getHref()
    {
        return get(hrefExpression, String.class, "");
    }

    public UIExpression getHrefExpression()
    {
        return hrefExpression;
    }
    
    public UIExpression getSrcExpression()
    {
        return srcExpression;
    }
}
