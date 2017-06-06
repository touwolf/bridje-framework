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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

/**
 * this class represents a children field, a field that can hold 
 * several controls as children of the parent control.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ChildrenFlield implements FieldDef
{
    @XmlAttribute
    private String name;
    
    @XmlAttribute
    private Boolean allowPlaceHolder;
    
    @XmlAttribute
    private String wrapper;

    @XmlAttribute
    private Boolean single;
                
    @XmlElements(
    {
        @XmlElement(name = "child", type = ChildField.class)
    })
    private List<ChildField> content;

    /**
     * The name of the field.
     * 
     * @return The name of the field.
     */
    @Override
    public String getName()
    {
        return name;
    }

    /**
     * The name of the field.
     * 
     * @param name The name of the field.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * If this field allows placeholders as a child.
     * 
     * @return If this field allows placeholders as a child.
     */
    public boolean getAllowPlaceHolder()
    {
        if(this.allowPlaceHolder == null) return false;
        return allowPlaceHolder;
    }

    /**
     * If this field allows placeholders as a child.
     * 
     * @param allowPlaceHolder If this field allows placeholders as a child.
     */
    public void setAllowPlaceHolder(boolean allowPlaceHolder)
    {
        this.allowPlaceHolder = allowPlaceHolder;
    }

    /**
     * If the xml declaration of this children need to be wrapped by an element.
     * 
     * @return The name of the wrapper element.
     */
    public String getWrapper()
    {
        return wrapper;
    }

    /**
     * If the xml declaration of this children need to be wrapped by an element.
     * 
     * @param wrapper The name of the wrapper element.
     */
    public void setWrapper(String wrapper)
    {
        this.wrapper = wrapper;
    }

    /**
     * If only one of the children can be declared.
     * 
     * @return If only one of the children can be declared.
     */
    public boolean getIsSingle()
    {
        if(single == null) return false;
        return single;
    }

    /**
     * If only one of the children can be declared.
     * 
     * @param single If only one of the children can be declared.
     */
    public void setSingle(boolean single)
    {
        this.single = single;
    }

    /**
     * The list of children.
     * 
     * @return The list of children.
     */
    public List<ChildField> getContent()
    {
        return content;
    }

    /**
     * The list of children.
     * 
     * @param content The list of children.
     */
    public void setContent(List<ChildField> content)
    {
        this.content = content;
    }

    @Override
    public String getJavaType()
    {
        if(getIsSingle()) return "Control";
        return "List<Control>";
    }

    @Override
    public String getFieldType()
    {
        return "children";
    }

    /**
     * The default value for this field.
     * 
     * @return The default value for this field.
     */
    public String getDefaultValue()
    {
        if(getIsSingle()) return null;
        return "new ArrayList<>()";
    }

    @Override
    public boolean getIsChild()
    {
        return true;
    }

    @Override
    public boolean getIsEvent()
    {
        return false;
    }

    @Override
    public boolean getIsInput()
    {
        return false;
    }

    @Override
    public boolean getIsInputFile()
    {
        return false;
    }
}
