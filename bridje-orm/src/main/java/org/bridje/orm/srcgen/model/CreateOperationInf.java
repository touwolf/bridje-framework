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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

/**
 * This class represents a create operation for an Entity, The create operation
 * will be added to the ORM model class in the source code generation.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateOperationInf extends ParametizedOperationInf
{
    @XmlElements(
            {
                @XmlElement(name = "set", type = OperationSetField.class)
            })
    private List<OperationSetField> sets;

    /**
     * The list of fields and the values that will be set to does fields in the
     * create operation.
     *
     * @return A list of fields to set and their values.
     */
    public List<OperationSetField> getSets()
    {
        if (sets == null)
        {
            sets = new ArrayList<>();
        }
        return sets;
    }

    @Override
    public OperationType getOperationType()
    {
        return OperationType.CREATE;
    }

}
