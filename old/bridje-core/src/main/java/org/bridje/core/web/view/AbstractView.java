/*
 * Copyright 2015 Bridje Framework.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bridje.core.web.view;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import org.bridje.vfs.VirtualFile;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractView
{
    private static final Logger LOG = Logger.getLogger(AbstractView.class.getName());

    @XmlElements(
            {
                @XmlElement(name = "extends", type = Extends.class),
                @XmlElement(name = "accordion", type = Accordion.class),
                @XmlElement(name = "border", type = BorderLayout.class),
                @XmlElement(name = "horizontal", type = HorizontalLayout.class),
                @XmlElement(name = "menubar", type = MenuBar.class),
                @XmlElement(name = "section", type = Section.class),
                @XmlElement(name = "tabsheet", type = TabSheet.class),
                @XmlElement(name = "toolbar", type = ToolBar.class),
                @XmlElement(name = "vertical", type = VerticalLayout.class)
            })
    private Component content;

    public Component getContent()
    {
        return content;
    }

    boolean isAbstract()
    {
        if(content instanceof Container)
        {
            return ((Container)content).isAbstract();
        }
        return false;
    }
    
    static Page loadPage(VirtualFile file)
    {
        Object obj = loadFile(file);
        if(obj != null && obj instanceof Page)
        {
            return (Page)obj;
        }
        return null;
    }
    
    static Fragment loadFragment(VirtualFile file)
    {
        Object obj = loadFile(file);
        if(obj != null && obj instanceof Fragment)
        {
            return (Fragment)obj;
        }
        return null;
    }
    
    private static Object loadFile(VirtualFile file)
    {
        InputStream is = null;
        try
        {
            is = file.open();
            JAXBContext jaxbCtx = JAXBContext.newInstance(Page.class, Fragment.class);
            Unmarshaller unm = jaxbCtx.createUnmarshaller();
            Object obj = unm.unmarshal(is);
            if(obj instanceof Page || obj instanceof Fragment)
            {
                return obj;
            }
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        finally
        {
            if(is != null)
            {
                try
                {
                    is.close();
                }
                catch (Exception e)
                {
                    LOG.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }
        return null;
    }

    public void travel(ComponentVisitor visitor)
    {
        travel(visitor, getContent());
    }

    private void travel(ComponentVisitor visitor, Component component)
    {
        if(component instanceof Container)
        {
            travel(visitor, (Container)component);
        }
        if(component instanceof Widget)
        {
            travel(visitor, (Widget)component);
        }
    }

    private void travel(ComponentVisitor visitor, Container container)
    {
        visitor.visit(container);
        for(Component comp : container.getChilds())
        {
            travel(visitor, comp);
        }
    }
    
    private void travel(ComponentVisitor visitor, Widget widget)
    {
        visitor.visit(widget);
    }

    public void travel(ContainerVisitor visitor)
    {
        travel(visitor, getContent());
    }

    private void travel(ContainerVisitor visitor, Component component)
    {
        if(component instanceof Container)
        {
            travel(visitor, (Container)component);
        }
    }

    private void travel(ContainerVisitor visitor, Container container)
    {
        visitor.visit(container);
        for(Component comp : container.getChilds())
        {
            travel(visitor, comp);
        }
    }

    public void travel(WidgetVisitor visitor)
    {
        travel(visitor, getContent());
    }

    private void travel(WidgetVisitor visitor, Component component)
    {
        if(component instanceof Container)
        {
            travel(visitor, (Container)component);
        }
        if(component instanceof Widget)
        {
            travel(visitor, (Widget)component);
        }
    }

    private void travel(WidgetVisitor visitor, Container container)
    {
        for(Component comp : container.getChilds())
        {
            travel(visitor, comp);
        }
    }
    
    private void travel(WidgetVisitor visitor, Widget widget)
    {
        visitor.visit(widget);
    }
}
