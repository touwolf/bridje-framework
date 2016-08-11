
package org.bridje.wui.comp;

import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.bridje.web.view.comp.UIEvent;
import org.bridje.web.view.comp.UIEventAdapter;
import org.bridje.web.view.comp.UIExpression;
import org.bridje.web.view.comp.UIExpressionAdapter;

/**
 * Represents a simple button, that can be clicked by the user and it will
 * trigger an event into the server.
 */
@XmlRootElement(name = "button")
@XmlAccessorType(XmlAccessType.FIELD)
public class Button extends BaseComponent
{
    @XmlValue
    @XmlJavaTypeAdapter(UIExpressionAdapter.class)
    private UIExpression captionExpression;

    @XmlAttribute(name = "action")
    @XmlJavaTypeAdapter(UIEventAdapter.class)
    private UIEvent action;

    /**
     * Gets the text of this component, this is the text that the button will render.
     * 
     * @return The button text.
     */
    public String getCaption()
    {
        return get(captionExpression, String.class, "");
    }

    /**
     * Gets the event that will be trigger by this button when clicked.
     * 
     * @return The event for this button.
     */
    public UIEvent getAction()
    {
        return action;
    }

    @Override
    public List<UIEvent> events()
    {
        if (action != null)
        {
            return Collections.singletonList(action);
        }
        return super.events();
    }
}
