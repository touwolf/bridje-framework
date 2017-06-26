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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

/**
 * A UI suite standalone specification.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class StandaloneDef
{
    @XmlElements(
    {
        @XmlElement(name = "child", type = ChildField.class)
    })
    private List<ChildField> content;

    /**
     * The content for this standalone declaration.
     * 
     * @return The content for this standalone declaration.
     */
    public List<ChildField> getContent()
    {
        return content;
    }

    /**
     * The content for this standalone declaration
     * 
     * @param content The content for this standalone declaration
     */
    public void setContent(List<ChildField> content)
    {
        this.content = content;
    }
}
