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

import java.util.ArrayList;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.bridje.web.srcgen.adapter.CData;

/**
 * Base class for the control definitions and control templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseControlDef
{
    @XmlID
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String base;

    @XmlIDREF
    @XmlAttribute
    private TemplateControlDef baseTemplate;

    @XmlJavaTypeAdapter(CData.class)
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
        @XmlElement(name = "child", type = ChildField.class),
        @XmlElement(name = "children", type = ChildrenFlield.class)
    })
    private List<FieldDef> fields;

    @XmlElementWrapper(name = "resources")
    @XmlElements(
    {
        @XmlElement(name = "resource", type = ResourceRef.class)
    })
    private List<ResourceRef> resources;

    @XmlTransient
    private List<FieldDef> allFields;
    
    @XmlTransient
    private List<ResourceRef> allResources;
    
    @XmlTransient
    private UISuite uiSuite;

    /**
     * The name of this definition.
     *
     * @return An string representing the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The name of this definition.
     *
     * @param name An string representing the name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The parent for this control, by default it is "Control".
     * 
     * @return The name of the parent control.
     */
    public String getBase()
    {
        if (base == null)
        {
            base = "Control";
        }
        return base;
    }

    /**
     * The parent for this control, by default it is "Control".
     * 
     * @param base The name of the parent control.
     */
    public void setBase(String base)
    {
        this.base = base;
    }

    /**
     * The base template for this control.
     * 
     * @return The template to be use by this control.
     */
    public TemplateControlDef getBaseTemplate()
    {
        return baseTemplate;
    }

    /**
     * The base template for this control.
     * 
     * @param baseTemplate The template to be use by this control.
     */
    public void setBaseTemplate(TemplateControlDef baseTemplate)
    {
        this.baseTemplate = baseTemplate;
    }

    /**
     * Defines the render freemarker script to be use by this control.
     * 
     * @return The text of the render script.
     */
    public String getRender()
    {
        return render;
    }

    /**
     * Defines the render freemarker script to be use by this control.
     * 
     * @param render The text of the render script.
     */
    public void setRender(String render)
    {
        this.render = render;
    }

    /**
     * 
     * @return 
     */
    public List<ResourceRef> getDeclaredResources()
    {
        return resources;
    }

    /**
     * 
     * @param resources 
     */
    public void setDeclaredResources(List<ResourceRef> resources)
    {
        resources = null;
        this.resources = resources;
    }

    /**
     * 
     * @return 
     */
    public List<FieldDef> getDeclaredFields()
    {
        return fields;
    }

    /**
     * 
     * @param resources 
     */
    public void setDeclaredFields(List<FieldDef> fields)
    {
        allFields = null;
        this.fields = fields;
    }
    
    /**
     * The list of fields that this control will support.
     * 
     * @return The list of fields that this control will support.
     */
    public List<FieldDef> getFields()
    {
        if(allFields == null)
        {
            allFields = new ArrayList<>();
            if(baseTemplate != null)
            {
                allFields.addAll(baseTemplate.getFields());
            }
            allFields.addAll(fields);
        }
        return allFields;
    }

    /**
     * The resources this control needs.
     * 
     * @return The resources this control needs.
     */
    public List<ResourceRef> getResources()
    {
        if(allResources == null)
        {
            allResources = new ArrayList<>();
            if(baseTemplate != null)
            {
                allResources.addAll(baseTemplate.getResources());
            }
            if(resources != null)
            {
                allResources.addAll(resources);
            }
        }
        return allResources;
    }

    /**
     * The full name of the java class for this control.
     * 
     * @return The full name of the java class for this control.
     */
    public String getFullName()
    {
        return getPackage() + "." + getName();
    }

    /**
     * This method is called by JAXB after the unmarshal has happend.
     * 
     * @param u The unmarshaller.
     * @param parent The parent.
     */
    public void afterUnmarshal(Unmarshaller u, Object parent)
    {
        if(parent instanceof UISuite)
        {
            uiSuite = (UISuite) parent;
        }
    }

    /**
     * Gets the parent UISuite object.
     * 
     * @return The parent UISuite object.
     */
    public UISuite getUISuite()
    {
        return uiSuite;
    }

    /**
     * Sets the parent UISuite object.
     * 
     * @param uiSuite The parent UISuite object.
     */
    void setUiSuite(UISuite uiSuite)
    {
        if(resources != null) resources.stream().forEach(r -> r.setUiSuite(uiSuite));
        this.uiSuite = uiSuite;
    }

    /**
     * The java class package for this control. This is taken from the UISuite.
     * 
     * @return The java class package for this control.
     */
    public String getPackage()
    {
        return uiSuite.getPackage();
    }
    
    /**
     * If this control has any children.
     * 
     * @return true the control has childrens, false otherwise.
     */
    public boolean getHasChildren()
    {
        return getFields().stream().anyMatch(f -> f.getIsChild());
    }

    /**
     * If the control has inputs.
     * 
     * @return true the control has inputs, false otherwise.
     */
    public boolean getHasInputs()
    {
        return getFields().stream().anyMatch(f -> f.getIsInput());
    }

    /**
     * If the control has any event.
     * 
     * @return true the control has events, false otherwise.
     */
    public boolean getHasEvents()
    {
        return getFields().stream().anyMatch(f -> f.getIsEvent());
    }

    /**
     * If the control has resources.
     * 
     * @return true the control has resources, false otherwise.
     */
    public boolean getHasResources()
    {
        return !getResources().isEmpty();
    }
}
