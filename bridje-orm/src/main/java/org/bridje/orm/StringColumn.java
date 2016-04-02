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

/**
 * Represents an string column, string columns are the same as a regular columns
 * but the ofer some unique functions like length.
 *
 * @param <E> The type of the entity that the field this columns represents
 * belongs to.
 */
public class StringColumn<E> extends Column<E, String>
{

    /**
     * This constructor is used to create a column without any funcions, a
     * column constructed by this constructor along will represent the plain
     * database column with no functions, columns must be created with the table
     * object they belong to, the field name as declared in the entity class,
     * and the Type of the field.
     *
     * @param table The Table object this columns belong to.
     * @param field The field name for the declared java field in the base
     * entity class.
     */
    public StringColumn(Table<E> table, String field)
    {
        super(table, field, String.class);
    }

    /**
     * Creates a new column that will return the the length in characters ot
     * this column.
     *
     * @return The new created column.
     */
    public NumberFunctionColumn<E, Integer, String> length()
    {
        return new NumberFunctionColumn<>(this, Integer.class, "LENGTH(%s)", null);
    }

    /**
     * Creates a new like condition for this column.
     *
     * @param value The like expression.
     * @return The new created condition.
     */
    public Condition like(String value)
    {
        return new BinaryCondition(this, Operator.LIKE, value);
    }
}
