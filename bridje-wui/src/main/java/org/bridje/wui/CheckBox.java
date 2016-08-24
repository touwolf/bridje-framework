
package org.bridje.wui.comp;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.bridje.web.view.comp.UIInputExpression;
import org.bridje.web.view.comp.UIInputExpressionAdapter;
import static org.bridje.web.view.comp.WebComponent.get;

@XmlRootElement(name = "checkbox")
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckBox extends BaseComponent
{
    @XmlAttribute(name = "value")
    @XmlJavaTypeAdapter(UIInputExpressionAdapter.class)
    private UIInputExpression valueExpression;

    public String getValue()
    {
        return get(valueExpression, String.class, "");
    }

    public UIInputExpression getValueExpression()
    {
        return valueExpression;
    }

    @Override
    public List<UIInputExpression> inputs()
    {
        List<UIInputExpression> result = super.inputs();
        result.add(valueExpression);
        return result;
    }
}
