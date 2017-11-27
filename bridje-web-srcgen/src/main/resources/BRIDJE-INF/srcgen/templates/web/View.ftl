<#ftl encoding="UTF-8">

package ${uisuite.package};

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.*;
import org.bridje.web.view.*;
import org.bridje.web.view.controls.*;
import javax.annotation.Generated;
import org.bridje.ioc.Ioc;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;

/**
 * Represents a view of the application, views are render by themes and are
 * composed from controls. The views are inmutables so once defined they will
 * stay the same at runtime.
 */
@Generated(value = "org.bridje.web.srcgen.WebSourceGenerator", date = "${.now?string("yyyy-MM-dd")}", comments = "Generated by Bridje Web API")
@XmlRootElement(name = "view")
@XmlAccessorType(XmlAccessType.FIELD)
public final class ${uisuite.name}View extends ${uisuite.name}AbstractView implements WebView
{
    private static final Logger LOG = Logger.getLogger(${uisuite.name}View.class.getName());

    @XmlTransient
    private String name;

    @XmlAttribute(name = "title")
    private UIExpression title;

    @XmlTransient
    private Map<String, UIEvent> events;

    @XmlTransient
    private Map<String, UIInputExpression> inputs;

    @XmlTransient
    private Map<String, UIFileExpression> fileInputs;

    @XmlTransient
    private Set<Class<?>> controls;

    @XmlTransient
    private Set<String> resources;

    <#list uisuite.fields as f>
    <#if f.fieldType == "attribute">
    @XmlAttribute
    <#elseif f.fieldType == "value">
    @XmlValue
    <#elseif f.fieldType == "children">
    <#if f.wrapper?? && f.wrapper != "">
    @XmlElementWrapper( name = "${f.wrapper}" )
    </#if>
    @XmlElements(
    {
        <#list f.content![] as c>
        @XmlElement( name = "${c.name}", type = ${c.type}.class ),
        </#list>
        <#if f.allowPlaceHolder>
        @XmlElement( name = "placeholder", type = ControlPlaceHolder.class ),
        </#if>
    })
    <#elseif f.fieldType == "child">
    @XmlElements(
    {
        @XmlElement( name = "${f.name}", type = ${f.javaType}.class ),
        <#if f.allowPlaceHolder>
        @XmlElement( name = "placeholder", type = ControlPlaceHolder.class ),
        </#if>
    })
    </#if>
    private ${f.javaType!} ${f.name};

    </#list>
    @Override
    public String getTitle()
    {
        return Control.get(title, String.class, null);
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setFile(VFile file)
    {
        super.setFile(file);
        if(file != null) 
        {
            String viewPath = toViewPath(file.getPath());
            this.name = viewPath;
        }
    }

    private String toViewPath(Path path)
    {
        Path basePath = Ioc.context().find(WebViewsManager.class).getBasePath();
        String viewPath = path.toString().substring(basePath.toString().length());
        viewPath = viewPath.substring(0, viewPath.length() - ".view.xml".length());
        return viewPath;
    }

    @Override
    public Set<String> getResources()
    {
        if (resources == null)
        {
            initResources();
        }
        return resources;
    }

    @Override
    public Set<Class<?>> getControls()
    {
        if (controls == null)
        {
            initControls();
        }
        return controls;
    }

    <#list uisuite.fields as f>
    <#if f.javaType == "UIExpression">
    public ${f.javaType} get${f.name?cap_first}Expression()
    {
        return ${f.name};
    }

    public ${f.type} get${f.name?cap_first}()
    {
        return Control.get(${f.name}, ${f.type}.class, ${f.defaultValue});
    }

    <#else>
    public ${f.javaType} get${f.name?cap_first}()
    {
        <#if f.defaultValue??>
        if(${f.name} == null) ${f.name} = ${f.defaultValue};
        </#if>
        return ${f.name};
    }
    </#if>

    </#list>
    @Override
    public UIInputExpression findInput(String exp)
    {
        if (inputs == null)
        {
            initInputs();
        }
        return inputs.get(exp);
    }

    @Override
    public UIEvent findEvent(String action)
    {
        if (events == null)
        {
            initEvents();
        }
        return events.get(action);
    }

    @Override
    public boolean hasFileInput()
    {
        if (fileInputs == null)
        {
            initFileInputs();
        }
        return !fileInputs.isEmpty();
    }

    @Override
    public UIFileExpression findFileInput(String exp)
    {
        if (fileInputs == null)
        {
            initFileInputs();
        }
        return fileInputs.get(exp);
    }

    private boolean checkIsValidView()
    {
        if(getRoot() != null)
        {
            return true;
        }
        LOG.log(Level.WARNING, "The view {0} does not have a valid root control, it will be ignored.", getName());
        return false;
    }

    private synchronized void initEvents()
    {
        if (events == null)
        {
            if (checkIsValidView())
            {
                Map<String, UIEvent> eventsMap = new LinkedHashMap<>();
                findEvents(getRoot(), eventsMap);
                events = eventsMap;
            }
            else
            {
                events = Collections.emptyMap();
            }
        }
    }

    private synchronized void initControls()
    {
        if (controls == null)
        {
            if (checkIsValidView())
            {
                Set<Class<?>> controlsSet = new LinkedHashSet<>();
                findControls(getRoot(), controlsSet);
                controls = controlsSet;
            }
            else
            {
                controls = Collections.emptySet();
            }
        }
    }

    private synchronized void initResources()
    {
        if (resources == null)
        {
            if (checkIsValidView())
            {
                Set<String> resourcesSet = new LinkedHashSet<>();
                findResources(getRoot(), resourcesSet);
                resources = resourcesSet;
            }
            else
            {
                resources = Collections.emptySet();
            }
        }
    }

    private synchronized void initInputs()
    {
        if (inputs == null)
        {
            if (checkIsValidView())
            {
                Map<String, UIInputExpression> inputsMap = new LinkedHashMap<>();
                findInputs(getRoot(), inputsMap);
                inputs = inputsMap;
            }
            else
            {
                inputs = Collections.emptyMap();
            }
        }
    }

    private synchronized void initFileInputs()
    {
        if (fileInputs == null)
        {
            if (checkIsValidView())
            {
                Map<String, UIFileExpression> inputsMap = new LinkedHashMap<>();
                findFileInputs(getRoot(), inputsMap);
                fileInputs = inputsMap;
            }
            else
            {
                fileInputs = Collections.emptyMap();
            }
        }
    }

    private void findEvents(Control control, Map<String, UIEvent> eventsMap)
    {
        control.events().forEach((ev) -> eventsMap.put(ev.getExpression(), ev));
        control.childs().forEach((child) -> findEvents(child, eventsMap));
    }

    private void findInputs(Control control, Map<String, UIInputExpression> inputsMap)
    {
        control.inputs().forEach((in) -> inputsMap.put(in.getParameter(), in));
        control.childs().forEach((child) -> findInputs(child, inputsMap));
    }

    private void findResources(Control control, Set<String> resourcesSet)
    {
        control.resources().forEach((r) -> resourcesSet.add(r));
        control.childs().forEach((child) -> findResources(child, resourcesSet));
    }

    private void findControls(Control control, Set<Class<?>> controlsSet)
    {
        controlsSet.add(control.getClass());
        control.childs().forEach((child) -> findControls(child, controlsSet));
    }

    private void findFileInputs(Control control, Map<String, UIFileExpression> inputsMap)
    {
        control.inputFiles().forEach((in) -> inputsMap.put(in.getParameter(), in));
        control.childs().forEach((child) -> findFileInputs(child, inputsMap));
    }
}
