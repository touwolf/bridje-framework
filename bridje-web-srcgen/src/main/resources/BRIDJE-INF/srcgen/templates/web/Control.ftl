<#ftl encoding="UTF-8">

package ${control.package};

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import org.bridje.web.view.EventResult;
import org.bridje.web.view.Defines;
import org.bridje.web.view.controls.*;
import org.bridje.http.UploadedFile;
import org.bridje.el.ElEnvironment;
import javax.annotation.Generated;
import javax.xml.bind.Unmarshaller;

/**
 * This object represents the ${control.name} for the ${uisuite.name} Theme.
 */
@Generated(value = "org.bridje.web.srcgen.WebSourceGenerator", date = "${.now?string("yyyy-MM-dd")}", comments = "Generated by Bridje Web API")
@XmlAccessorType(XmlAccessType.FIELD)
<#if control.isTransient>@XmlTransient</#if>
public class ${control.name} extends Control
{
    <#if control.hasChildren>
    @XmlTransient
    private List<Control> childs;

    </#if>
    <#if control.hasInputs>
    @XmlTransient
    private List<UIInputExpression> inputs;

    </#if>
    <#if control.hasInputFiles>
    @XmlTransient
    private List<UIFileExpression> inputFiles;

    </#if>
    <#if control.hasEvents>
    @XmlTransient
    private List<UIEvent> events;

    </#if>
    <#if control.hasResources>
    @XmlTransient
    private List<String> resources;

    </#if>
    <#list control.fields as f>
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
    @XmlTransient
    private Control parent;

    <#list control.fields as f>
    <#if f.javaType == "UIExpression">
    public ${f.javaType} get${f.name?cap_first}Expression()
    {
        return ${f.name};
    }

    public ${f.type} get${f.name?cap_first}()
    {
        return get(${f.name}, ${f.type}.class, ${f.defaultValue});
    }
    <#elseif f.javaType == "UIInputExpression">
    public ${f.javaType} get${f.name?cap_first}Expression()
    {
        return ${f.name};
    }

    public ${f.type} get${f.name?cap_first}()
    {
        return get(${f.name}, ${f.type}.class, ${f.defaultValue});
    }
    <#elseif f.javaType == "UIFileExpression">
    public ${f.javaType} get${f.name?cap_first}Expression()
    {
        return ${f.name};
    }

    public UploadedFile get${f.name?cap_first}()
    {
        return get(${f.name});
    }
    <#elseif f.javaType == "UIEvent">
    public ${f.javaType} get${f.name?cap_first}()
    {
        return ${f.name};
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
    <#if control.hasChildren>
    @Override
    public List<? extends Control> childs()
    {
        if(childs == null)
        {
            childs = new ArrayList<>();
            <#list control.fields as f>
            <#if f.fieldType == "children">
            if(${f.name} != null)
            {
                <#if f.isSingle>
                childs.add(${f.name});
                <#else>
                childs.addAll(${f.name});
                </#if>
            }
            </#if>
            <#if f.fieldType == "child">
            if(${f.name} != null) childs.add(${f.name});
            </#if>
            </#list>
        }
        return childs;
    }

    </#if>
    <#if control.hasInputs>
    @Override
    public List<UIInputExpression> inputs()
    {
        if(inputs == null)
        {
            inputs = new ArrayList<>();
            <#list control.fields as f>
            <#if f.isInput>
            if(${f.name} != null && ${f.name}.isValid()) inputs.add(${f.name});
            </#if>
            </#list>
        }
        return inputs;
    }

    </#if>
    <#if control.hasInputFiles>
    @Override
    public List<UIFileExpression> inputFiles()
    {
        if(inputFiles == null)
        {
            inputFiles = new ArrayList<>();
            <#list control.fields as f>
            <#if f.isInputFile>
            if(${f.name} != null && ${f.name}.isValid()) inputFiles.add(${f.name});
            </#if>
            </#list>
        }
        return inputFiles;
    }

    </#if>
    <#if control.hasEvents>
    @Override
    public List<UIEvent> events()
    {
        if(events == null)
        {
            events = new ArrayList<>();
            <#list control.fields as f>
            <#if f.isEvent>
            if(${f.name} != null) events.add(${f.name});
            </#if>
            </#list>
        }
        return events;
    }

    </#if>
    <#if control.hasResources>
    @Override
    public List<String> resources()
    {
        if(resources == null)
        {
            resources = new ArrayList<>();
            resources.addAll(super.resources());
            <#list control.resources as r>
            resources.add("${r.name}");
            </#list>
        }
        return resources;
    }

    </#if>
    @Override
    public void doOverride(Map<String, Defines> definesMap)
    {
        <#list control.fields as f>
        <#if f.fieldType == "children">
        <#if f.allowPlaceHolder>
        if(${f.name} != null)
        {
            <#if f.isSingle>
            ${f.name} = (${f.javaType})Control.doOverride(${f.name}, definesMap);
            <#else>
            ${f.name} = Control.doOverride(${f.name}, definesMap);
            </#if>
        }
        </#if>
        </#if>
        <#if f.fieldType == "child">
        <#if f.allowPlaceHolder>
        if(${f.name} != null) ${f.name} = (${f.javaType})Control.doOverride(${f.name}, definesMap);
        </#if>
        </#if>
        </#list>
    }

    /**
     * Gets the parent control of this control.
     *
     * @return The parent control of this control.
     */
    public Control getParent()
    {
        return this.parent;
    }

    /**
     * Called by JAXB.
     * 
     * @param u The unmarshaller.
     * @param parent The parent.
     */
    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        if(parent instanceof Control)
        {
            this.parent = (Control) parent;
        }
    }

    <#if control.class.simpleName == "DynamicControlDef">
    @XmlAttribute
    private UIExpression data;

    @XmlAttribute
    private String var;

    public UIExpression getDataExpression()
    {
        return data;
    }

    public List getData()
    {
        return get(data, List.class, Collections.EMPTY_LIST);
    }

    public String getVar()
    {
        if(var == null) var = null;
        return var;
    }

    @Override
    public void readInput(ControlInputReader req, ElEnvironment env)
    {
        if(getVisible() != null && getVisible())
        {
            for(Object item : getData())
            {
                env.pushVar(getVar(), item);
                doReadInput(req, env);
                env.popVar(getVar());
            }
        }
    }

    @Override
    public EventResult executeEvent(ControlInputReader req, ElEnvironment env)
    {
        if(getVisible() != null && getVisible())
        {
            for(Object item : getData())
            {
                env.pushVar(getVar(), item);
                EventResult result = doExecuteEvent(req, env);
                env.popVar(getVar());
                if(result != null) return result;
            }
        }
        return null;
    }

    @Override
    public <T> T findById(ElEnvironment env, String id, ControlCallback<T> callback)
    {
        if(id == null || id.isEmpty()) return null;
        if(getVisible() != null && getVisible())
        {
            for(Object item : getData())
            {
                env.pushVar(getVar(), item);
                T result = doFindById(env, id, callback);
                env.popVar(getVar());
                if(result != null) return result;
            }
        }
        return null;
    }

    </#if>
}
