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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Reads the input for the given child or children field.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ReadChildren implements ControlFlowAction
{
    @XmlAttribute
    private String fieldName;

    /**
     * The child or children field name to read the input.
     * 
     * @return The child or children field name to read the input.
     */
    public String getFieldName()
    {
        return fieldName;
    }

    /**
     * The child or children field name to read the input.
     * 
     * @param fieldName The child or children field name to read the input.
     */
    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }
}
