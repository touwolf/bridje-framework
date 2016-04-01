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
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.orm.Key;
import org.bridje.orm.dialects.ColumnData;
import org.bridje.orm.dialects.TableData;

/**
 *
 */
class FieldInf<T, C> implements ColumnData
{
    private static final Logger LOG = Logger.getLogger(FieldInf.class.getName());
    
    private final Field field;

    private final String columnName;
    
    private final boolean key;
    
    private final JDBCType sqlType;
    
    private final int length;
    
    private final int precision;
    
    private final EntityInf<T> entityInf;

    private final Class<C> dataType;

    private final boolean indexed;
    
    private final boolean autoIncrement;
    
    public FieldInf(EntityInf<T> entityInf, Field field, Class<C> dataType)
    {
        this.entityInf = entityInf;
        this.dataType = dataType;
        this.field = field;
        this.field.setAccessible(true);
        org.bridje.orm.Field annotation = this.field.getAnnotation(org.bridje.orm.Field.class);
        if(annotation == null)
        {
            throw new IllegalArgumentException("The field " + field.getName() + " is not a valid entity field.");
        }
        this.key = this.field.getAnnotation(Key.class) != null;
        this.autoIncrement = this.key && this.field.getAnnotation(Key.class).autoIncrement();
        this.columnName = findColumnName(annotation.column());
        this.sqlType = findSqlType(annotation.type());
        this.length = findLength(annotation.length());
        this.precision = (sqlType == JDBCType.FLOAT || sqlType == JDBCType.DOUBLE || sqlType == JDBCType.DECIMAL) ? annotation.precision() : 0;
        this.indexed = annotation.index();
    }

    public Field getField()
    {
        return field;
    }

    @Override
    public boolean isKey()
    {
        return key;
    }

    @Override
    public String getColumnName()
    {
        return columnName;
    }
    
    public void setValue(Object entity, Object value)
    {
        try
        {
            this.field.set(entity, CastUtils.castValue(field.getType(), value));
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public <T> Object getValue(T entity)
    {
        try
        {
            return this.field.get(entity);
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public JDBCType getSqlType()
    {
        return sqlType;
    }

    @Override
    public int getLength()
    {
        return length;
    }

    @Override
    public int getPrecision()
    {
        return precision;
    }

    public EntityInf<T> getEntityInf()
    {
        return entityInf;
    }

    public Class<C> getDataType()
    {
        return dataType;
    }

    private int findLength(int length)
    {
        if(length <= 0)
        {
            switch(sqlType)
            {
                case VARCHAR:
                    return 100;
                default:
                    return 0;
            }
        }
        return length;
    }

    private JDBCType findSqlType(JDBCType type)
    {
        if(type == JDBCType.NULL)
        {
            if(Boolean.class.isAssignableFrom(field.getType()))
            {
                return JDBCType.BOOLEAN;
            }
            else if(Byte.class.isAssignableFrom(field.getType()))
            {
                return JDBCType.TINYINT;
            }
            else if(Short.class.isAssignableFrom(field.getType()))
            {
                return JDBCType.SMALLINT;
            }
            else if(Integer.class.isAssignableFrom(field.getType()))
            {
                return JDBCType.INTEGER;
            }
            else if(Long.class.isAssignableFrom(field.getType()))
            {
                return JDBCType.BIGINT;
            }
            else if(Float.class.isAssignableFrom(field.getType()))
            {
                return JDBCType.FLOAT;
            }
            else if(Double.class.isAssignableFrom(field.getType()))
            {
                return JDBCType.DOUBLE;
            }
            else if(CharSequence.class.isAssignableFrom(field.getType()))
            {
                return JDBCType.VARCHAR;
            }
            else if(Character.class.isAssignableFrom(field.getType()))
            {
                return JDBCType.CHAR;
            }
            else if(Time.class.isAssignableFrom(field.getType()))
            {
                return JDBCType.TIME;
            }
            else if(Timestamp.class.isAssignableFrom(field.getType()))
            {
                return JDBCType.TIMESTAMP;
            }
            else if(Date.class.isAssignableFrom(field.getType())
                    || java.sql.Date.class.isAssignableFrom(field.getType()))
            {
                return JDBCType.DATE;
            }
        }
        return type;
    }

    private String findColumnName(String column)
    {
        if(column == null || column.trim().isEmpty())
        {
            return field.getName();
        }
        return column.trim();
    }

    @Override
    public TableData getTableData()
    {
        return entityInf;
    }

    @Override
    public boolean isIndexed()
    {
        return indexed;
    }

    @Override
    public boolean isAutoIncrement()
    {
        return autoIncrement;
    }
    
    @Override
    public String getDefaultValue()
    {
        String def = null;
        if(!isKey())
        {
            if(getSqlType() == JDBCType.TIMESTAMP
                    || getSqlType() == JDBCType.TIMESTAMP_WITH_TIMEZONE)
            {
                def = "'0000-00-00 00:00:00'";
            }
            else
            {
                def = "NULL";
            }
        }
        return def;
    }
}
