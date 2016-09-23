
package org.bridje.wui.widgets;

import org.bridje.web.view.widgets.BaseContainer;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import org.bridje.web.view.widgets.Widget;

@XmlRootElement(name = "gridlayout")
@XmlAccessorType(XmlAccessType.FIELD)
public class GridLayout extends BaseContainer
{
    @XmlAttribute(name = "columns")
    private int columns;
    
    @XmlAnyElement(lax = true)
    @XmlElementRefs(
    {
        @XmlElementRef(name = "borderlayout", type = BorderLayout.class),
        @XmlElementRef(name = "button", type = Button.class),
        @XmlElementRef(name = "checkbox", type = CheckBox.class),
        @XmlElementRef(name = "combobox", type = ComboBox.class),
        @XmlElementRef(name = "empty", type = Empty.class),
        @XmlElementRef(name = "gridlayout", type = GridLayout.class),
        @XmlElementRef(name = "header", type = Header.class),
        @XmlElementRef(name = "horizLayout", type = HorizontalLayout.class),
        @XmlElementRef(name = "image", type = Image.class),
        @XmlElementRef(name = "link", type = Link.class),
        @XmlElementRef(name = "paragraph", type = Paragraph.class),
        @XmlElementRef(name = "password", type = Password.class),
        @XmlElementRef(name = "table", type = Table.class),
        @XmlElementRef(name = "text", type = Text.class),
        @XmlElementRef(name = "textarea", type = TextArea.class),
        @XmlElementRef(name = "textbox", type = TextBox.class),
        @XmlElementRef(name = "vertlayout", type = VerticalLayout.class)
    })
    private List<Widget> children;

    public List<Widget> getChildren()
    {
        return children;
    }

    public int getColumns()
    {
        if(columns <= 0)
        {
            return 1;
        }
        return columns;
    }

    @Override
    public List<? extends Widget> childs()
    {
        return getChildren();
    }
}
