
package org.bridje.wui.widgets;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.bridje.web.view.widgets.UIExpression;
import org.bridje.web.view.widgets.UIExpressionAdapter;
import org.bridje.web.view.widgets.Widget;

@XmlAccessorType(XmlAccessType.FIELD)
public class TableColumn extends Widget
{
    @XmlAttribute(name = "value")
    @XmlJavaTypeAdapter(UIExpressionAdapter.class)
    private UIExpression value;

    @XmlAttribute(name = "title")
    @XmlJavaTypeAdapter(UIExpressionAdapter.class)
    private UIExpression title;
    
    public String getValue()
    {
        return get(value, String.class, "");
    }

    public String getTitle()
    {
        return get(title, String.class, "");
    }
}
