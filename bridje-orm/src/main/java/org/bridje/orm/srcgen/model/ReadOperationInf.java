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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class ReadOperationInf extends ParametizedOperationInf
{
    @XmlAttribute
    private ReadOperationResultType resultType;

    @XmlAttribute
    private String result;
    
    public ReadOperationResultType getResultType()
    {
        return resultType;
    }

    public void setResultType(ReadOperationResultType resultType)
    {
        this.resultType = resultType;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }
    
    public FieldInfBase getResultField()
    {
        return getEntity().getFields()
                            .stream()
                            .filter(f -> f.getName().equalsIgnoreCase(result))
                            .findAny().orElse(null);
    }

    @Override
    public OperationType getOperationType()
    {
        return OperationType.READ;
    }

    @Override
    public OperationInfBase clone(EntityInfBase entity)
    {
        ReadOperationInf result = new ReadOperationInf();
        clone(result, entity);
        result.resultType = this.resultType;
        result.result = this.result;
        return result;
    }
}
