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

package org.bridje.orm.impl;

import java.lang.reflect.Field;
import java.sql.JDBCType;
import org.bridje.orm.Condition;
import org.bridje.orm.OrmService;
import org.bridje.orm.Table;
import org.bridje.orm.TableRelationColumn;

class TableRelationColumnImpl<E, R> extends TableColumnImpl<E, R> implements TableRelationColumn<E, R>
{
    private Table<R> relatedTable;

    public TableRelationColumnImpl(TableImpl<E> table, Field field, Class<R> entity)
    {
        super(table, field, entity);
    }
    
    protected void initRelation(OrmService ormServ)
    {
        relatedTable = ormServ.findTable(getType());
    }

    @Override
    public Table<R> getRelated()
    {
        return relatedTable;
    }

    @Override
    public JDBCType getSqlType()
    {
        JDBCType sqlType = super.getSqlType();
        if(sqlType == JDBCType.NULL)
        {
            return relatedTable.getKey().getSqlType();
        }
        return sqlType;
    }

    @Override
    protected Object getQueryParameter(E entity)
    {
        R value = (R)super.getQueryParameter(entity);
        if(value == null)
        {
            return null;
        }
        return ((TableColumnImpl<R, ?>)getRelated().getKey()).getValue(value);
    }

    @Override
    public Condition eq(R value)
    {
        Object idValue = ((TableColumnImpl<R, ?>)getRelated().getKey()).getValue(value);
        return new BinaryCondition(this, Operator.EQ, serialize(idValue));
    }

    @Override
    public Condition ne(R value)
    {
        Object idValue = ((TableColumnImpl<R, ?>)getRelated().getKey()).getValue(value);
        return new BinaryCondition(this, Operator.NE, serialize(idValue));
    }
}
