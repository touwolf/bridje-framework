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

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.sql.JDBCType;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.orm.Key;

/**
 *
 */
class FieldInf
{
    private static final Logger LOG = Logger.getLogger(FieldInf.class.getName());
    
    private final Field field;

    private final String columnName;
    
    private final boolean key;
    
    private final JDBCType sqlType;
    
    private final long length;
    
    private final long precision;
    
    public FieldInf(Field field)
    {
        this.field = field;
        this.field.setAccessible(true);
        org.bridje.orm.Field annotation = this.field.getAnnotation(org.bridje.orm.Field.class);
        if(annotation == null)
        {
            throw new IllegalArgumentException("The field " + field.getName() + " is not a valid entity field.");
        }
        this.key = this.field.getAnnotation(Key.class) != null;
        this.columnName = findColumnName(annotation.column());
        this.sqlType = findSqlType(annotation.type());
        this.length = findLength(annotation.length());
        this.precision = annotation.precision();
    }

    public Field getField()
    {
        return field;
    }

    public boolean isKey()
    {
        return key;
    }

    public String getColumnName()
    {
        return columnName;
    }
    
    public void setValue(Object entity, Object value)
    {
        try
        {
            this.field.set(entity, castValue(value));
        }
        catch (Exception e)
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
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public String createFieldStmt()
    {
        StringWriter sw = new StringWriter();
        
        sw.append("`");
        sw.append(getColumnName());
        sw.append("` ");
        sw.append(getSqlType().getName());
        if(getLength() > 0)
        {
            sw.append("(");
            sw.append(String.valueOf(getLength()));
            if(precision > 0 && 
                    (sqlType == JDBCType.FLOAT || sqlType == JDBCType.DOUBLE || sqlType == JDBCType.DECIMAL) )
            {
                sw.append(", ");
                sw.append(String.valueOf(getPrecision()));
            }
            sw.append(")");
        }
        if(isKey())
        {
            sw.append(" NOT NULL");
        }
        else
        {
            if(sqlType == JDBCType.TIMESTAMP
                    || sqlType == JDBCType.TIMESTAMP_WITH_TIMEZONE)
            {
                sw.append(" DEFAULT '0000-00-00 00:00:00'");
            }
            else
            {
                sw.append(" DEFAULT NULL");
            }
        }
        
        return sw.toString();
    }

    public JDBCType getSqlType()
    {
        return sqlType;
    }

    public long getLength()
    {
        return length;
    }

    public long getPrecision()
    {
        return precision;
    }

    private long findLength(int length)
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

    private Object castValue(Object value)
    {
        if(value != null)
        {
            if(!field.getType().isAssignableFrom(value.getClass()))
            {
                if(Character.class.isAssignableFrom(field.getType()))
                {
                    return toCharacter(value);
                }
                if(Byte.class.isAssignableFrom(field.getType()))
                {
                    return toByte(value);
                }
                if(Short.class.isAssignableFrom(field.getType()))
                {
                    return toShort(value);
                }
            }
            return value;
        }
        return null;
    }

    private Character toCharacter(Object value)
    {
        if(value instanceof String && !((String)value).isEmpty())
        {
            return ((String) value).toCharArray()[0];
        }
        return null;
    }

    private Object toByte(Object value)
    {
        if(value instanceof Number)
        {
            return ((Number)value).byteValue();
        }
        if(value instanceof String && !((String)value).isEmpty())
        {
            return Byte.valueOf((String)value);
        }
        return null;
    }
    
    private Object toShort(Object value)
    {
        if(value instanceof Number)
        {
            return ((Number)value).shortValue();
        }
        if(value instanceof String && !((String)value).isEmpty())
        {
            return Short.valueOf((String)value);
        }
        return null;
    }
}
