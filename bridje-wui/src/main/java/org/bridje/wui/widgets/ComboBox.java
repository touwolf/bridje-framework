
package org.bridje.wui.widgets;

import org.bridje.web.view.widgets.BaseWidget;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.bridje.web.view.widgets.UIInputExpression;
import org.bridje.web.view.widgets.UIInputExpressionAdapter;

@XmlRootElement(name = "combobox")
@XmlAccessorType(XmlAccessType.FIELD)
public class ComboBox extends BaseWidget
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

    public void setValueExpression(UIInputExpression valueExpression)
    {
        this.valueExpression = valueExpression;
    }

    @Override
    public List<UIInputExpression> inputs()
    {
        List<UIInputExpression> result = super.inputs();
        result.add(valueExpression);
        return result;
    }
}
