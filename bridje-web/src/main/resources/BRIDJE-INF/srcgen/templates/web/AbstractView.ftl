<#ftl encoding="UTF-8">

package ${uisuite.package};

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;
import org.bridje.web.view.*;
import org.bridje.web.view.controls.Control;

/**
 * Represents a view of the application, views are render by themes and are
 * composed from controls. The views are inmutables so once defined they will
 * stay the same at runtime.
 */
@XmlTransient
@XmlAccessorType(XmlAccessType.FIELD)
public class ${uisuite.name}AbstractView implements AbstractView
{
    @XmlElements(
    {
        @XmlElement(name = "extends", type = ${uisuite.name}ExtendsFrom.class),
        @XmlElement(name = "standalone", type = ${uisuite.name}Standalone.class)
    })    
    private ViewDefinition definition;

    @XmlTransient
    private Control root;

    private String defaultTheme;

    /**
     * The root control of this view.
     *
     * @return The root control.
     */
    public Control getRoot()
    {
        if(root == null && definition != null)
        {
            root = definition.findRoot();
            if(root == null) return null;
            DefaultTheme annot = root.getClass().getPackage().getAnnotation(DefaultTheme.class);
            if(annot != null)
            {
                defaultTheme = annot.name();
            }
        }
        return root;
    }

    /**
     * Gets the default theme associated with the roor control of the view.
     * 
     * @return The default theme for the root control of the view.
     */
    public String getDefaultTheme()
    {
        if(defaultTheme == null)
        {
            getRoot();
        }
        return defaultTheme;
    }

    /**
     * Gets the view definition control for this view.
     * 
     * @return The view definition control, standalone or extends.
     */
    public ViewDefinition getDefinition()
    {
        return definition;
    }
}
