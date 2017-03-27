<#ftl encoding="UTF-8">

package ${uisuite.package};

import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.*;
import org.bridje.web.view.*;

/**
 * Represents a view of the application, views are render by themes and are
 * composed from controls. The views are inmutables so once defined they will
 * stay the same at runtime.
 */
@XmlRootElement(name = "layout")
@XmlAccessorType(XmlAccessType.FIELD)
public class ${uisuite.name}Layout extends ${uisuite.name}AbstractView implements WebLayout
{
    @XmlElements(
        @XmlElement(name = "meta", type = ${uisuite.name}MetaTag.class)
    )
    private List<MetaTag> metaTags;

    /**
     * Gets a list of meta information tags information to be rendered with views that extends this layout.
     *
     * @return A list of meta information tags assigned to views.
     */
    public List<MetaTag> getMetaTags()
    {
        if (metaTags == null)
        {
            metaTags = Collections.emptyList();
        }
        return metaTags;
    }
}
