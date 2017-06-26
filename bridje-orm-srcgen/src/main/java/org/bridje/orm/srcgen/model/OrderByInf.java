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
 * Information about orther by statement of a query.
 * 
 */
public class OrderByInf
{
    private FieldInfBase field;

    private OrderByType type;

    /**
     * Default constructor.
     */
    public OrderByInf()
    {
    }

    /**
     * Constructor by field and type.
     * 
     * @param field The field for this order by.
     * @param type The type for this order by.
     */
    public OrderByInf(FieldInfBase field, OrderByType type)
    {
        this.field = field;
        this.type = type;
    }
    
    /**
     * The field.
     * 
     * @return The field.
     */
    public FieldInfBase getField()
    {
        return field;
    }

    /**
     * The field.
     * 
     * @param field The field.
     */
    public void setField(FieldInfBase field)
    {
        this.field = field;
    }

    /**
     * The order by type.
     * 
     * @return The order by type.
     */
    public OrderByType getType()
    {
        return type;
    }

    /**
     * The order by type.
     * 
     * @param type The order by type.
     */
    public void setType(OrderByType type)
    {
        this.type = type;
    }
}
