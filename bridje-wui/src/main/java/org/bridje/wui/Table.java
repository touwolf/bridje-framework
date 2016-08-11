
package org.bridje.wui;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.bridje.web.view.comp.UIExpression;
import org.bridje.web.view.comp.UIExpressionAdapter;

@XmlRootElement(name = "table")
@XmlAccessorType(XmlAccessType.FIELD)
public class Table extends BaseComponent
{
    @XmlAttribute(name = "data")
    @XmlJavaTypeAdapter(UIExpressionAdapter.class)
    private UIExpression dataExpression;

    @XmlAttribute(name = "var")
    private String var;

    @XmlElements(
    {
        @XmlElement(name = "column", type = TableColumn.class)
    })
    private List<TableColumn> columns;

    public Collection getData()
    {
        return get(dataExpression, Collection.class, Collections.EMPTY_LIST);
    }

    public String getVar()
    {
        return var;
    }

    public List<TableColumn> getColumns()
    {
        return columns;
    }
}
