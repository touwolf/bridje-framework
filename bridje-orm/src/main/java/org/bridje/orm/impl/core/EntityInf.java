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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bridje.ioc.Ioc;
import org.bridje.orm.Column;
import org.bridje.orm.OrderBy;
import org.bridje.orm.OrderByType;
import org.bridje.orm.RelationColumn;
import org.bridje.orm.dialects.ColumnData;
import org.bridje.orm.dialects.TableData;

/**
 *
 * @param <T>
 */
class EntityInf<T> implements TableData
{
    private static final Logger LOG = Logger.getLogger(EntityInf.class.getName());

    private final Class<T> entityClass;

    private final String tableName;

    private final FieldInf<T, ?> keyField;

    private final List<FieldInf<T, ?>> fields;

    private final List<RelationInf<T, ?>> relations;

    private final Map<String, FieldInf<T, ?>> fieldsMap;
    
    private final Map<String, RelationInf<T, ?>> relationsMap;

    public EntityInf(Class<T> entityClass, String tableName)
    {
        this.entityClass = entityClass;
        this.tableName = tableName;
        fields = new ArrayList<>();
        keyField = fillFields();
        relations = new ArrayList<>();
        relationsMap = new HashMap<>();
        if(keyField == null)
        {
            throw new IllegalArgumentException("The class " + entityClass.getName() + " does not have a valid key field.");
        }
        fieldsMap = new HashMap<>();
        fields.stream().forEach((fieldInf) -> fieldsMap.put(fieldInf.getField().getName(), fieldInf));
    }

    public Class<T> getEntityClass()
    {
        return entityClass;
    }

    public FieldInf getKeyField()
    {
        return keyField;
    }

    public List<FieldInf<T, ?>> getFields()
    {
        return fields;
    }

    public List<RelationInf<T, ?>> getRelations()
    {
        return relations;
    }

    @Override
    public String getTableName()
    {
        return tableName;
    }

    private FieldInf fillFields()
    {
        FieldInf keyInf = null;
        Field[] declaredFields = entityClass.getDeclaredFields();
        for (Field declaredField : declaredFields)
        {
            org.bridje.orm.Field fieldAnnot = declaredField.getAnnotation(org.bridje.orm.Field.class);
            if(fieldAnnot != null)
            {
                FieldInf fInf = new FieldInf(this, declaredField, declaredField.getType());
                if(fInf.isKey())
                {
                    if(keyInf != null)
                    {
                        throw new IllegalArgumentException("Entity " + entityClass.getName() + " has more than one key field.");
                    }
                    keyInf = fInf;
                }
                fields.add(fInf);
            }
        }
        return keyInf;
    }

    protected void fillRelations()
    {
        OrmMetaInfService metainf = Ioc.context().find(OrmMetaInfService.class);
        Field[] declaredFields = entityClass.getDeclaredFields();
        for (Field declaredField : declaredFields)
        {
            org.bridje.orm.Relation fieldAnnot = declaredField.getAnnotation(org.bridje.orm.Relation.class);
            if(fieldAnnot != null)
            {
                RelationInf fInf = new RelationInf(this, declaredField, metainf.findEntityInf(declaredField.getType()));
                relations.add(fInf);
            }
        }
        relations.stream().forEach((relationInf) -> relationsMap.put(relationInf.getField().getName(), relationInf));
    }

    public Stream<String> allFieldsStream(String prefix)
    {
        return Stream.concat(
                    fields.stream().map((field) -> prefix + field.getColumnName()),
                    relations.stream().map((relation) -> prefix + relation.getColumnName())
                );
    }
    
    public String allFieldsCommaSep(String prefix)
    {
        return Stream.concat(
                    fields.stream().map((field) -> prefix + field.getColumnName()),
                    relations.stream().map((relation) -> prefix + relation.getColumnName())
                ).collect(Collectors.joining(", "));
    }

    public List<T> parseAll(ResultSet rs, EntityContextImpl ctx)
    {
        List<T> result = new ArrayList<>();
        try
        {
            while(rs.next())
            {
                T entity = parseNew(rs, ctx);
                if(entity != null)
                {
                    result.add(entity);
                    ctx.getEnittysCache().put(entity, findKeyValue(entity));
                }
            }
        }
        catch (SQLException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }

    public T parse(ResultSet rs, EntityContextImpl ctx) throws SQLException
    {
        if(rs.next())
        {
            T result = parseNew(rs, ctx);
            if(result != null)
            {
                ctx.getEnittysCache().put(result, findKeyValue(result));
            }
            return result;
        }
        return null;
    }

    public T parse(T entity, ResultSet rs, EntityContextImpl ctx) throws SQLException
    {
        if(rs.next())
        {
            fill(entity, rs, ctx);
            ctx.getEnittysCache().put(entity, findKeyValue(entity));
            return entity;
        }
        return null;
    }

    public <C> List<C> parseAll(int index, Column<T, C> column, ResultSet rs, EntityContextImpl ctx) throws SQLException
    {
        if(column instanceof RelationColumn)
        {
            RelationInf<T, C> relationInf = findRelationInfo(column);
            return parseAll(index, relationInf, rs, ctx);
        }
        else
        {
            return parseAll(index, column.getType(), rs, ctx);
        }
    }

    public <C> List<C> parseAll(int index, Class<C> type, ResultSet rs, EntityContextImpl ctx) throws SQLException
    {
        List<C> result = new ArrayList<>();
        while(rs.next())
        {
            Object value = CastUtils.castValue(type, rs.getObject(index));
            if(value != null)
            {
                result.add((C)value);
            }
        }
        return result;
    }

    public <C> List<C> parseAll(int index, RelationInf<T, C> relation, ResultSet rs, EntityContextImpl ctx) throws SQLException
    {
        Class columnType = relation.getRelatedEntity().getKeyField().getDataType();
        List<?> keys = parseAll(index, columnType, rs, ctx);
        List<C> result = new ArrayList<>(keys.size());
        for (Object key : keys)
        {
            C value = ctx.find(relation.getRelatedEntity().getEntityClass(), key);
            result.add(value);
        }
        return result;
    }

    public <C> C parse(int index, Column<T, C> column, ResultSet rs, EntityContextImpl ctx) throws SQLException
    {
        if(column instanceof RelationColumn)
        {
            RelationInf<T, C> relationInf = findRelationInfo(column);
            return parse(index, relationInf, rs, ctx);
        }
        else
        {
            return parse(index, column.getType(), rs, ctx);
        }
    }

    public <C> C parse(int index, Class<C> type, ResultSet rs, EntityContextImpl ctx) throws SQLException
    {
        if(rs.next())
        {
            Object value = CastUtils.castValue(type, rs.getObject(index));
            if(value != null)
            {
                return (C)value;
            }
        }
        return null;
    }

    public <C> C parse(int index, RelationInf<T, C> relation, ResultSet rs, EntityContextImpl ctx) throws SQLException
    {
        Class<?> columnType = relation.getRelatedEntity().getKeyField().getDataType();
        Object value = parse(index, columnType, rs, ctx);
        if(value != null)
        {
            return ctx.find(relation.getRelatedEntity().getEntityClass(), value);
        }
        return null;
    }

    public int parseCount(ResultSet rs) throws SQLException
    {
        if(rs.next())
        {
            return rs.getInt(1);
        }
        return -1;
    }

    private T parseNew(ResultSet rs, EntityContextImpl ctx) throws SQLException
    {
        T entity = buildEntityObject();
        fill(entity, rs, ctx);
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
            throw new RuntimeException("Cannot create Entity object for class " + entityClass.getName(), ex);
        }
    }

    private void fill(T entity, ResultSet rs, EntityContextImpl ctx) throws SQLException
    {
        for (FieldInf fieldInf : fields)
        {
            Object value = rs.getObject(fieldInf.getColumnName());
            fieldInf.setValue(entity, value);
        }
        for (RelationInf relationInf : relations)
        {
            Object value = rs.getObject(relationInf.getColumnName());
            Class<?> rec = relationInf.getRelatedEntity().getEntityClass();
            Object relatedEntityObject = ctx.find(rec, value);
            relationInf.setValue(entity, relatedEntityObject);
        }
    }

    public <T> Object[] buildUpdateParameters(T entity, Object id)
    {
        List<Object> result = Stream.concat(
                            fields.stream().map((fi) -> fi.getValue(entity)),
                            relations.stream().map((fi) -> fi.getColumnValue(entity))
                        ).collect(Collectors.toList());
        result.add(id);
        return result.toArray();
    }
    
    public <T> Object[] buildInsertParameters(T entity)
    {
        List<Object> result = Stream.concat(
                            fields.stream().map((fi) -> fi.getValue(entity)),
                            relations.stream().map((fi) -> fi.getColumnValue(entity))
                        ).collect(Collectors.toList());
        return result.toArray();
    }

    public <T> Object findKeyValue(T entity)
    {
        return keyField.getValue(entity);
    }

    public String buildIdCondition()
    {
        return keyField.getColumnName() + " = ?";
    }

    public <C> FieldInf<T, C> findFieldInfo(Column<T, C> column)
    {
        return (FieldInf<T, C>)fieldsMap.get(column.getField());
    }

    public <C> RelationInf<T, C> findRelationInfo(Column<T, C> column)
    {
        return (RelationInf<T, C>)relationsMap.get(column.getField());
    }
    
    public String buildOrderBy(OrderBy orderBy, String prefix)
    {
        return prefix + findColumnName(orderBy.getColumn()) + " " + (orderBy.getType() == OrderByType.ASC ? "ASC" : "DESC");
    }

    public int allFieldsCount()
    {
        return fields.size() + relations.size();
    }

    @Override
    public List<ColumnData> getColumns()
    {
        return Stream.concat(
                    fields.stream().map((field) -> (ColumnData)field),
                    relations.stream().map((relation) -> (ColumnData)relation)
                ).collect(Collectors.toList());
    }

    @Override
    public ColumnData getKeyColumn()
    {
        return keyField;
    }

    public <T> T updateKeyField(T entity, ResultSet rs, EntityContextImpl entityContext) throws SQLException
    {
        keyField.setValue(entity, parse(1, keyField.getDataType(), rs, entityContext));
        return entity;
    }
    
    public String findColumnName(Column<T, ?> column)
    {
        if(column instanceof RelationColumn)
        {
            return findRelationInfo(column).getColumnName();
        }
        else
        {
            return findFieldInfo(column).getColumnName();
        }
    }
}
