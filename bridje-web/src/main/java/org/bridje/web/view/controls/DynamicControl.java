
package org.bridje.web.view.controls;

import java.util.Collections;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.List;
import java.util.Map;
import org.bridje.web.view.EventResult;
import org.bridje.web.view.Defines;
import org.bridje.el.ElEnvironment;

@XmlAccessorType(XmlAccessType.FIELD)
public class DynamicControl extends Control
{
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
    public void doOverride(Map<String, Defines> definesMap)
    {
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

}
