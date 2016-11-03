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
import org.bridje.orm.NumberColumn;
import org.bridje.orm.SQLAdapter;
import org.bridje.orm.adpaters.EnumAdapter;
import org.bridje.orm.SQLCustomType;
import org.bridje.orm.TableNumberColumn;

class TableColumnImpl<E, T> extends AbstractColumn<T> implements TableNumberColumn<E, T>
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
    
    private SQLAdapter<T, Object> adapter;
    
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
        SQLCustomType customType = this.type.getAnnotation(SQLCustomType.class);
        this.key = this.field.getAnnotation(Key.class) != null;
        this.autoIncrement = this.key && this.field.getAnnotation(Key.class).autoIncrement();
        this.name = findColumnName(annotation.column());
        this.sqlType = findSqlType(annotation, customType);
        this.length = findLength(annotation, customType);
        this.precision = (sqlType == JDBCType.FLOAT || sqlType == JDBCType.DOUBLE || sqlType == JDBCType.DECIMAL) ? findPrecision(annotation, customType) : 0;
        this.indexed = annotation.index();
        this.required = annotation.required();
        this.adapter = findAdapter(findAdapterClass(annotation, customType));
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

    protected T getValue(E entity)
    {
        try
        {
            if(entity == null)
            {
                return null;
            }
            return (T)this.field.get(entity);
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

    private int findPrecision(org.bridje.orm.Field annotation, SQLCustomType customType)
    {
        int currentPrecision = annotation.precision();
        if(customType != null && currentPrecision <= 0)
        {
            currentPrecision = customType.precision();
        }
        return currentPrecision;
    }

    private Class<? extends SQLAdapter> findAdapterClass(org.bridje.orm.Field annotation, SQLCustomType customType)
    {
        Class<? extends SQLAdapter> cls = annotation.adapter();
        if(customType != null && (cls == null || cls == SQLAdapter.class))
        {
            cls = customType.adapter();
        }
        return cls;
    }
    
    private int findLength(org.bridje.orm.Field annotation, SQLCustomType customType)
    {
        int length = annotation.length();
        if(customType != null && length <= 0)
        {
            length = customType.length();
        }
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

    private JDBCType findSqlType(org.bridje.orm.Field annotation, SQLCustomType customType)
    {
        JDBCType currentType = annotation.type();
        if(customType != null && 
                    (currentType == JDBCType.NULL || currentType == null))
        {
            currentType = customType.type();
        }
        if(currentType == JDBCType.NULL)
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
        return currentType;
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
    
    @Override
    public Object serialize(T value)
    {
        if(value == null)
        {
            return null;
        }
        if(adapter != null)
        {
            return adapter.serialize(value, this);
        }
        return value;
    }

    @Override
    public T unserialize(Object value)
    {
        if(value == null)
        {
            return null;
        }
        if(adapter != null)
        {
            return adapter.unserialize(value, this);
        }
        return (T)value;
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

    private SQLAdapter findAdapter(Class<? extends SQLAdapter> adapterCls)
    {
        SQLAdapter result = null;
        if(adapterCls == SQLAdapter.class)
        {
            if(Enum.class.isAssignableFrom(this.type))
            {
                result = findAdapterOrCreate(EnumAdapter.class);
            }
        }
        else
        {
            result = findAdapterOrCreate(adapterCls);
        }
        return result;
    }

    private SQLAdapter findAdapterOrCreate(Class<? extends SQLAdapter> adapterCls)
    {
        SQLAdapter sqlAdapter = Ioc.context().find(adapterCls);
        if(sqlAdapter == null)
        {
            sqlAdapter = instantiate(adapterCls);
        }
        return sqlAdapter;        
    }

    @Override
    public NumberColumn<T> sum()
    {
        return new FunctionColumnImpl<>(this,getType(), "SUM(%s)");
    }

    @Override
    public NumberColumn<T> puls(T value)
    {
        FunctionColumnImpl result = new FunctionColumnImpl<>(this,getType(), "%s + ?");
        result.addParameter(value);
        return result;
    }

    @Override
    public NumberColumn<T> minus(T value)
    {
        FunctionColumnImpl result = new FunctionColumnImpl<>(this,getType(), "%s + ?");
        result.addParameter(value);
        return result;
    }

    @Override
    public Condition gt(T value)
    {
        return new BinaryCondition(this, Operator.GT, serialize(value));
    }

    @Override
    public Condition ge(T value)
    {
        return new BinaryCondition(this, Operator.GE, serialize(value));
    }

    @Override
    public Condition lt(T value)
    {
        return new BinaryCondition(this, Operator.LT, serialize(value));
    }

    @Override
    public Condition le(T value)
    {
        return new BinaryCondition(this, Operator.LE, serialize(value));
    }
}
