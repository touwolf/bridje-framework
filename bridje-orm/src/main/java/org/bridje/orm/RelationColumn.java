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

package org.bridje.orm;

import org.bridje.ioc.Ioc;
/**
 * Represents a relation column.
 *
 * @param <E> The type of the entity that the field this columns represents
 * belongs to.
 * @param <R>
 */
public class RelationColumn<E, R> extends Column<E, R>
{
    private Table<R> related;

    /**
     * This constructor is used to create a column without any funcions, a
     * column constructed by this constructor along will represent the plain
     * database column with no functions, columns must be created with the table
     * object they belong to, the field name as declared in the entity class,
     * and the Type of the field.
     *
     * @param table The Table object this columns belong to.
     * @param related
     * @param field The field name for the declared java field in the base
     * entity class.
     */
    public RelationColumn(Table<E> table, Table<R> related, String field)
    {
        super(table, field, related.getEntityClass());
    }

    public Table<R> getRelatedTable()
    {
        return related;
    }

    @Override
    public Condition eq(R value)
    {
        Object keyValue = Ioc.context().find(OrmService.class).findKeyValue(value);
        return new BinaryCondition(this, Operator.EQ, keyValue);
    }
}
