
package org.bridje.wui.comp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.bridje.web.view.comp.UIExpression;
import org.bridje.web.view.comp.UIExpressionAdapter;
import org.bridje.web.view.comp.WebComponent;

@XmlAccessorType(XmlAccessType.FIELD)
public class TableColumn extends WebComponent
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
