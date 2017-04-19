<#ftl encoding="UTF-8">

package ${uisuite.package};

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import org.bridje.ioc.Ioc;
import org.bridje.web.view.*;
import org.bridje.web.view.controls.Control;

/**
 * Defines that the view will extend from the given layout.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ${uisuite.name}ExtendsFrom implements ViewDefinition, ExtendsFrom
{
    @XmlAttribute
    private String layout;

    @XmlElements(
    {
        @XmlElement(name = "define", type = ${uisuite.name}Defines.class)
    })
    private List<Defines> defines;

    @XmlTransient
    private Map<String, Defines> definesMap;

    /**
     * Gets all the placeholder definitions for this view.
     *
     * @return A map with the name of the placeholder an the corresponding
     * Defines object.
     */
    public Map<String, Defines> getDefinesMap()
    {
        if (definesMap == null)
        {
            initDefinesMap();
        }
        return definesMap;
    }

    /**
     * Gets the list of defines for this view.
     *
     * @return A list of Defines objects.
     */
    public List<Defines> getDefines()
    {
        return defines;
    }

    /**
     * The name of the parent layout.
     * 
     * @return An string with the name of the parent layout.
     */
    public String getLayout()
    {
        return layout;
    }

    @Override
    public Control findRoot()
    {
        WebLayoutManager layoutManag = Ioc.context().find(WebLayoutManager.class);
        WebLayout webLayout = layoutManag.loadLayout(layout);
        if (webLayout != null)
        {
            Control control = webLayout.getRoot();
            control.override(getDefinesMap());
            return control;
        }
        return null;
    }

    private void initDefinesMap()
    {
        definesMap = new HashMap<>();
        if(defines != null)
        {
            for (Defines define : defines)
            {
                definesMap.put(define.getName(), define);
            }
        }
    }
}
