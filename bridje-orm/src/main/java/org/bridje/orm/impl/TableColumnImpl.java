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
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Ioc;
import org.bridje.orm.Condition;
import org.bridje.orm.EntityContext;
import org.bridje.orm.Key;
import org.bridje.orm.SQLAdapter;
import org.bridje.orm.TableColumn;
import org.bridje.orm.adpaters.EnumAdapter;

/**
 *
 */
class TableColumnImpl<E, T> extends AbstractColumn<T> implements TableColumn<E, T>
{
    private static final Logger LOG = Logger.getLogger(TableColumnImpl.class.getName());
    
    private final Field field;

    private final String name;
    
    private final boolean key;
    
    private final JDBCType sqlType;
    
    private final int length;
    
    private final int precision;
    
    private final TableImpl<E> table;

    private final Class<T> type;

    private final boolean indexed;
    
    private final boolean autoIncrement;
    
    private final boolean required;
    
    private SQLAdapter adapter;
    
    public TableColumnImpl(TableImpl<E> table, Field field, Class<T> type)
    {
        this.table = table;
        this.type = type;
        this.field = field;
        this.field.setAccessible(true);
        org.bridje.orm.Field annotation = this.field.getAnnotation(org.bridje.orm.Field.class);
        if(annotation == null)
        {
            throw new IllegalArgumentException("The field " + field.getName() + " is not a valid entity field.");
        }
        this.key = this.field.getAnnotation(Key.class) != null;
        this.autoIncrement = this.key && this.field.getAnnotation(Key.class).autoIncrement();
        this.name = findColumnName(annotation.column());
        this.sqlType = findSqlType(annotation.type());
        this.length = findLength(annotation.length());
        this.precision = (sqlType == JDBCType.FLOAT || sqlType == JDBCType.DOUBLE || sqlType == JDBCType.DECIMAL) ? annotation.precision() : 0;
        this.indexed = annotation.index();
        this.required = annotation.required();
        if(annotation.adapter() == SQLAdapter.class)
        {
            if(Enum.class.isAssignableFrom(this.type))
            {
                this.adapter = findAdapter(EnumAdapter.class);
            }
        }
        else
        {
            this.adapter = findAdapter(annotation.adapter());
        }
    }

    @Override
    public TableImpl<E> getTable()
    {
        return table;
    }

    @Override
    public boolean isKey()
    {
        return key;
    }

    @Override
    public String getName()
    {
        return name;
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

    @Override
    public Class<T> getType()
    {
        return type;
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
            def = "NULL";
        }
        return def;
    }

    @Override
    public Field getField()
    {
        return field;
    }

    protected void setValue(Object entity, Object value)
    {
        try
        {
            this.field.set(entity, unserialize(value));
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    protected Object getValue(E entity)
    {
        try
        {
            if(entity == null)
            {
                return null;
            }
            return this.field.get(entity);
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }
    
    protected Object getQueryParameter(E entity)
    {
        if(entity == null)
        {
            return null;
        }
        return serialize(getValue(entity));
    }

    private int findLength(int length)
    {
        if(length <= 0)
        {
            switch(sqlType)
            {
                case VARCHAR:
                    return 100;
                case CHAR:
                    return 1;
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
            else if(java.sql.Date.class.isAssignableFrom(field.getType()))
            {
                return JDBCType.DATE;
            }
            else if(Date.class.isAssignableFrom(field.getType()))
            {
                return JDBCType.TIMESTAMP;
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
    public String writeSQL(List<Object> parameters, EntityContext ctx)
    {
        return ctx.getDialect().identifier(table.getName()) + "." + ctx.getDialect().identifier(name);
    }

    @Override
    public Condition eq(T value)
    {
        return new BinaryCondition(this, Operator.EQ, serialize(value));
    }

    @Override
    public boolean isRequired()
    {
        return required;
    }

    @Override
    public Condition ne(T value)
    {
        return new BinaryCondition(this, Operator.NE, serialize(value));
    }
    
    public Object serialize(Object value)
    {
        if(adapter != null)
        {
            return adapter.serialize(value, this);
        }
        return value;
    }

    public Object unserialize(Object value)
    {
        if(adapter != null)
        {
            return adapter.unserialize(value, this);
        }
        return value;
    }

    private SQLAdapter instantiate(Class<? extends SQLAdapter> adapter)
    {
        try
        {
            return (SQLAdapter)adapter.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    private SQLAdapter findAdapter(Class<? extends SQLAdapter> sqlAdapt)
    {
        SQLAdapter sqlAdapter = Ioc.context().find(sqlAdapt);
        if(sqlAdapter == null)
        {
            sqlAdapter = instantiate(sqlAdapt);
        }
        return sqlAdapter;
    }

    public void validate(E entity) throws SQLException
    {
        Object value = getQueryParameter(entity);
        if(isRequired())
        {
            if(value == null)
            {
                throw new SQLException("The column " + getName() + " cannot be null.");
            }
        }
        if(value instanceof String)
        {
           if( ((String) value).length() > length)
           {
               throw new SQLException("Data to big for column " + getName());
           }
        }
    }
}
