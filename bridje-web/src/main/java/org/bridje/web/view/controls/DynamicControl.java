
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
        for(Object item : getData())
        {
            env.pushVar(getVar(), item);
            inputFiles().stream().forEachOrdered(inputFile -> set(inputFile, req.popUploadedFile(inputFile.getParameter())));
            inputs().stream().forEachOrdered(input -> set(input, req.popParameter(input.getParameter())));
            childs().forEach(control -> control.readInput(req, env));
            env.popVar(getVar());
        }
    }

    @Override
    public EventResult executeEvent(ControlInputReader req, ElEnvironment env)
    {
        for(Object item : getData())
        {
            env.pushVar(getVar(), item);
            for (UIEvent event : events()) if(eventTriggered(req, event)) return invokeEvent(event);
            for (Control control : childs())
            {
                EventResult result = control.executeEvent(req, env);
                if(result != null) return result;
            }
            env.popVar(getVar());
        }
        return null;
    }

    @Override
    public Control findById(ElEnvironment env, String id, ControlCallback callback)
    {
        if(id == null || id.isEmpty()) return null;
        for(Object item : getData())
        {
            env.pushVar("item", item);
            if(id.equals(getId())) 
            {
                callback.process(this);
                return this;
            }
            for (Control control : childs())
            {
                Control result = control.findById(env, id, callback);
                if (result != null) return result;
            }
            env.popVar("item");
        }
        return null;
    }

}
