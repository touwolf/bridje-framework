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

package org.bridje.orm.srcgen.model;

/**
 * 
 */
public class OrderByInf
{
    private FieldInfBase field;

    private OrderByType type;

    public OrderByInf()
    {
    }

    public OrderByInf(FieldInfBase field, OrderByType type)
    {
        this.field = field;
        this.type = type;
    }
    
    public FieldInfBase getField()
    {
        return field;
    }

    public void setField(FieldInfBase field)
    {
        this.field = field;
    }

    public OrderByType getType()
    {
        return type;
    }

    public void setType(OrderByType type)
    {
        this.type = type;
    }
}
