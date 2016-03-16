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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
class FieldInf
{
    private static final Logger LOG = Logger.getLogger(FieldInf.class.getName());

    private final Field field;

    private final String columnName;
    
    private final boolean key;
    
    private final String sqlDataType;
    
    public FieldInf(Field field)
    {
        this.field = field;
        this.field.setAccessible(true);
        org.bridje.orm.Field annotation = this.field.getAnnotation(org.bridje.orm.Field.class);
        if(annotation == null)
        {
            throw new IllegalArgumentException("The field " + field.getName() + " is not a valid entity field.");
        }
        this.key = annotation.key();
        this.columnName = annotation.column();
        this.sqlDataType = annotation.sqlType();
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
            this.field.set(entity, value);
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
        sw.append(getSqlDataType());
        if(isKey())
        {
            sw.append(" NOT NULL");
        }
        else
        {
            sw.append(" DEFAULT NULL");
        }
        
        return sw.toString();
    }

    private String getSqlDataType()
    {
        return sqlDataType;
    }
}
