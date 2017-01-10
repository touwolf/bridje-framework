/*
 * Copyright 2017 Bridje Framework.
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

package org.bridje.web.srcgen.uisuite;

import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public class BaseControlDef
{
    @XmlID
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String rootElement;

    @XmlAttribute
    private String base;

    @XmlIDREF
    @XmlAttribute
    private TemplateControlDef baseTemplate;

    private String render;

    @XmlElementWrapper(name = "fields")
    @XmlElements(
    {
        @XmlElement(name = "outAttr", type = OutAttrField.class),
        @XmlElement(name = "inAttr", type = InAttrFlield.class),
        @XmlElement(name = "eventAttr", type = EventAttrFlield.class),
        @XmlElement(name = "attr", type = AttrFlield.class),
        @XmlElement(name = "outEl", type = OutElementField.class),
        @XmlElement(name = "inEl", type = InElementFlield.class),
        @XmlElement(name = "eventEl", type = EventElementFlield.class),
        @XmlElement(name = "el", type = ElementField.class),
        @XmlElement(name = "outValue", type = OutValueField.class),
        @XmlElement(name = "inValue", type = InValueFlield.class),
        @XmlElement(name = "eventValue", type = EventValueFlield.class),
        @XmlElement(name = "value", type = ValueFlield.class),
        @XmlElement(name = "child", type = ChildFlield.class),
        @XmlElement(name = "children", type = ChildrenFlield.class)
    })
    private List<FieldDef> fields;

    @XmlElementWrapper(name = "resources")
    @XmlElements(
    {
        @XmlElement(name = "resource", type = Resource.class)
    })
    private List<Resource> resources;
    
    @XmlTransient
    private UISuite uiSuite;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getRootElement()
    {
        return rootElement;
    }

    public void setRootElement(String rootElement)
    {
        this.rootElement = rootElement;
    }

    public String getBase()
    {
        if(base == null) base = "Control";
        return base;
    }

    public void setBase(String base)
    {
        this.base = base;
    }

    public TemplateControlDef getBaseTemplate()
    {
        return baseTemplate;
    }

    public void setBaseTemplate(TemplateControlDef baseTemplate)
    {
        this.baseTemplate = baseTemplate;
    }

    public String getRender()
    {
        return render;
    }

    public void setRender(String render)
    {
        this.render = render;
    }

    public List<FieldDef> getFields()
    {
        return fields;
    }

    public void setFields(List<FieldDef> fields)
    {
        this.fields = fields;
    }

    public List<Resource> getResources()
    {
        return resources;
    }

    public void setResources(List<Resource> resources)
    {
        this.resources = resources;
    }

    public String getFullName()
    {
        return getPackage() + "." + getName();
    }

    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        uiSuite = (UISuite)parent;
    }

    public UISuite getUISuite()
    {
        return uiSuite;
    }
    
    public String getPackage()
    {
        return uiSuite.getPackage();
    }
}
