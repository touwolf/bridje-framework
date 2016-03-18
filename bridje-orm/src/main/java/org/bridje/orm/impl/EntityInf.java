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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.bridje.orm.Entity;

/**
 *
 * @param <T>
 */
class EntityInf<T>
{
    private static final Logger LOG = Logger.getLogger(EntityInf.class.getName());

    private final Class<T> entityClass;

    private final String tableName;

    private FieldInf keyField;

    private final List<FieldInf> fields;

    public EntityInf(Class<T> entityClass)
    {
        this.entityClass = entityClass;
        Entity annotation = this.entityClass.getAnnotation(Entity.class);
        if(annotation == null)
        {
            throw new IllegalArgumentException("The class " + entityClass.getName() + " is not a valid entity.");
        }
        tableName = annotation.table();
        fields = new ArrayList<>();
        keyField = fillFields();
        if(keyField == null)
        {
            throw new IllegalArgumentException("The class " + entityClass.getName() + " does not have a valid key field.");
        }
    }

    public Class<T> getEntityClass()
    {
        return entityClass;
    }

    public FieldInf getKeyField()
    {
        return keyField;
    }

    public List<FieldInf> getFields()
    {
        return fields;
    }

    public String getTableName()
    {
        return tableName;
    }

    private FieldInf fillFields()
    {
        FieldInf keyField = null;
        Field[] declaredFields = entityClass.getDeclaredFields();
        for (Field declaredField : declaredFields)
        {
            org.bridje.orm.Field fieldAnnot = declaredField.getAnnotation(org.bridje.orm.Field.class);
            if(fieldAnnot != null)
            {
                FieldInf fInf = new FieldInf(declaredField);
                if(fInf.isKey())
                {
                    if(keyField != null)
                    {
                        throw new IllegalArgumentException("Entity " + entityClass.getName() + " has more than one key field.");
                    }
                    keyField = fInf;
                }
                fields.add(fInf);
            }
        }
        return keyField;
    }
    
    public <T> String buildSelectQuery(String condition)
    {
        return buildSelectQuery(condition, -1, -1);
    }
    
    public <T> String buildSelectQuery(String condition, int index, int size)
    {
        StringBuilder sw = new StringBuilder();
        
        sw.append("SELECT ");
        sw.append(fields.stream()
                    .map((field) -> field.getColumnName())
                    .collect(Collectors.joining(", ")));
        sw.append(" FROM ");
        sw.append(getTableName());
        sw.append(" WHERE ");
        sw.append(condition);
        if(index >= 0 && size >= 0)
        {
            sw.append(" LIMIT ");
            sw.append(index);
            sw.append(", ");
            sw.append(size);
        }
        sw.append(";");
        
        return sw.toString();
    }
    
    public <T> String buildCountQuery(String condition)
    {
        StringBuilder sw = new StringBuilder();
        
        sw.append("SELECT ");
        sw.append("count(*)");
        sw.append(" FROM ");
        sw.append(getTableName());
        sw.append(" WHERE ");
        sw.append(condition);
        sw.append(";");
        
        return sw.toString();
    }
    
    public List<T> parseAllEntitys(ResultSet rs)
    {
        List<T> result = new ArrayList<>();
        try
        {
            while(rs.next())
            {
                T entity = parseEntityInternal(rs);
                if(entity != null)
                {
                    result.add(entity);
                }
            }
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }
    
    public T parseEntity(ResultSet rs)
    {
        try
        {
            if(rs.next())
            {
                return parseEntityInternal(rs);
            }
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }
    
    public int parseCount(ResultSet rs)
    {
        try
        {
            if(rs.next())
            {
                return rs.getInt(0);
            }
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return -1;
    }
    
    private T parseEntityInternal(ResultSet rs) throws SQLException
    {
        T entity = buildEntityObject();
        fillEntity(entity, rs);
        return entity;
    }

    public T buildEntityObject()
    {
        try
        {
            return entityClass.newInstance();
        }
        catch (InstantiationException | IllegalAccessException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    private void fillEntity(T entity, ResultSet rs) throws SQLException
    {
        for (FieldInf fieldInf : fields)
        {
            Object value = rs.getObject(fieldInf.getColumnName());
            fieldInf.setValue(entity, value);
        }
    }

    public <T> String buildInsertQuery(T entity)
    {
        StringBuilder sw = new StringBuilder();
        
        sw.append("INSERT INTO ");
        sw.append(getTableName());
        sw.append(" (");
        sw.append(fields.stream()
                    .map((field) -> field.getColumnName())
                    .collect(Collectors.joining(", ")));
        sw.append(") VALUES (");
        sw.append(fields.stream()
                    .map((field) -> "?")
                    .collect(Collectors.joining(", ")));
        sw.append(")");
        return sw.toString();        
    }

    public <T> Object[] buildInsertParameters(T entity)
    {
        return fields.stream()
                .map((fi) -> fi.getValue(entity))
                .collect(Collectors.toList())
                .toArray();
    }

    public <T> String buildDeleteQuery(T entity)
    {
        StringBuilder sw = new StringBuilder();
        
        sw.append("DELETE FROM ");
        sw.append(getTableName());
        sw.append(" WHERE ");
        sw.append(keyField.getColumnName());
        sw.append(" = ?;");

        return sw.toString();        
    }

    public <T> Object findKeyValue(T entity)
    {
        return keyField.getValue(entity);
    }

    public String buildCreateTableQuery()
    {
        StringBuilder sw = new StringBuilder();
        sw.append("CREATE TABLE `");
        sw.append(tableName);
        sw.append("` (\n");
        sw.append(fields.stream()
                .map((f) -> f.createFieldStmt())
                .collect(Collectors.joining(", \n")));
        sw.append(", \nPRIMARY KEY (`");
        sw.append(keyField.getColumnName());
        sw.append("`)\n");
        sw.append(") ENGINE=InnoDB;");

        return sw.toString();
    }
    
    public String buildIdCondition()
    {
        return keyField.getColumnName() + " = ?";
    }
}
