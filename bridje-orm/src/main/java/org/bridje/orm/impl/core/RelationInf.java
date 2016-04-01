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

package org.bridje.orm.impl.core;

import java.lang.reflect.Field;
import java.sql.JDBCType;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.orm.Relation;
import org.bridje.orm.dialects.ColumnData;
import org.bridje.orm.dialects.TableData;

/**
 *
 */
class RelationInf<T, R> implements ColumnData
{
    private static final Logger LOG = Logger.getLogger(RelationInf.class.getName());

    private final Field field;

    private final String columnName;

    private final EntityInf<T> entityInf;
    
    private final EntityInf<R> relatedEntity;

    public RelationInf(EntityInf<T> entityInf, Field field, EntityInf<R> relatedEntity)
    {
        this.entityInf = entityInf;
        this.field = field;
        this.field.setAccessible(true);
        this.relatedEntity = relatedEntity;
        Relation annotation = field.getAnnotation(Relation.class);
        this.columnName = findColumnName(annotation.column());
    }

    public Field getField()
    {
        return field;
    }

    @Override
    public String getColumnName()
    {
        return columnName;
    }

    public EntityInf<R> getRelatedEntity()
    {
        return relatedEntity;
    }

    private String findColumnName(String column)
    {
        if(column == null || column.trim().isEmpty())
        {
            return field.getName();
        }
        return column.trim();
    }

    public void setValue(Object entity, Object value)
    {
        try
        {
            this.field.set(entity, value);
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public <T> R getValue(T entity)
    {
        try
        {
            return (R)this.field.get(entity);
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public <T> Object getColumnValue(T entity)
    {
        R relatedEntityObject = getValue(entity);
        if(relatedEntityObject != null)
        {
            return relatedEntity.getKeyField().getValue(relatedEntityObject);
        }
        return null;
    }

    public EntityInf<T> getEntityInf()
    {
        return entityInf;
    }

    @Override
    public JDBCType getSqlType()
    {
        return relatedEntity.getKeyField().getSqlType();
    }

    @Override
    public boolean isKey()
    {
        return false;
    }

    @Override
    public TableData getTableData()
    {
        return entityInf;
    }

    @Override
    public int getLength()
    {
        return relatedEntity.getKeyField().getLength();
    }

    @Override
    public int getPrecision()
    {
        return relatedEntity.getKeyField().getPrecision();
    }

    @Override
    public boolean isIndexed()
    {
        return true;
    }

    @Override
    public String getDefaultValue()
    {
        return "NULL";
    }

    @Override
    public boolean isAutoIncrement()
    {
        return false;
    }
}
