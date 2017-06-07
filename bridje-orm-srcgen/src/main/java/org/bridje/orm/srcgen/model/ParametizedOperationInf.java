/*
 * Copyright 2016 Bridje Framework.
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

package org.bridje.orm.srcgen.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * This class is the base clase for all the parametized operations.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class ParametizedOperationInf extends OperationInfBase
{
    @XmlAttribute
    private String params;

    @XmlTransient
    private List<FieldInfBase> paramsFields;

    /**
     * The list of params that this operation have.
     * 
     * @return A list of field information objects that represents the params.
     */
    public List<FieldInfBase> getParams()
    {
        if(paramsFields == null)
        {
            paramsFields = parseFields(params);
        }
        return paramsFields;
    }

    /**
     * Utility method to parse all the params, out of the params attribute.
     * 
     * @param fields The comma separated fields String.
     * @return The list of fields for the params.
     */
    protected List<FieldInfBase> parseFields(String fields)
    {
        List<FieldInfBase> result = new ArrayList<>();
        if(fields == null || fields.isEmpty()) return result;
        String[] split = fields.split(",");
        for (String fieldName : split)
        {
            FieldInfBase field = getEntity().findField(fieldName);
            if(field == null) continue;
            result.add(field);
        }
        return result;
    }
}
