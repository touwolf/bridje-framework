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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bridje.ioc.Ioc;
import org.bridje.orm.Column;
import org.bridje.orm.Entity;
import org.bridje.orm.OrderBy;
import org.bridje.orm.OrderByType;
import org.bridje.orm.OrmModel;
import org.bridje.orm.OrmService;
import org.bridje.orm.Table;
import org.bridje.orm.TableColumn;

class TableImpl<T> implements Table<T>
{
    private static final Logger LOG = Logger.getLogger(TableImpl.class.getName());

    private final Class<T> entity;

    private final String name;

    private final TableColumn<T, ?> key;

    private final List<TableColumn<T, ?>> columns;
    
    private final List<TableColumn<T, ?>> nonAiColumns;

    private final Map<String, TableColumn<T, ?>> columnsMap;
    
    private final Map<String, TableColumn<T, ?>> columnsByNameMap;

    private OrmService ormServ;
    
    public TableImpl(Class<T> entity, String name)
    {
        this.entity = entity;
        this.name = name;
        columns = new ArrayList<>();
        key = createColumns();
        if(key == null)
        {
            throw new IllegalArgumentException("The class " + entity.getName() + " does not have a valid key field.");
        }
        columnsMap = new HashMap<>();
        columnsByNameMap = new HashMap<>();
        columns.stream().forEach((column) -> columnsMap.put(column.getField().getName(), column));
        columns.stream().forEach((column) -> columnsByNameMap.put(column.getName(), column));
        nonAiColumns = columns.stream().filter(c -> !c.isAutoIncrement()).collect(Collectors.toList());
    }

    @Override
    public Class<T> getEntity()
    {
        return entity;
    }

    @Override
    public TableColumnImpl getKey()
    {
        return (TableColumnImpl)key;
    }

    @Override
    public List<TableColumn<T, ?>> getColumns()
    {
        return columns;
    }
    
    public List<TableColumn<T, ?>> getNonAiColumns()
    {
        return nonAiColumns;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public TableColumn<T, ?> findColumn(String fieldName)
    {
        return columnsMap.get(fieldName);
    }
    
    @Override
    public TableColumn<T, ?> findColumnByName(String columnName)
    {
        return columnsByNameMap.get(columnName);
    }
    
    private TableColumnImpl createColumns()
    {
        TableColumnImpl keyInf = null;
        Field[] declaredFields = entity.getDeclaredFields();
        for (Field declaredField : declaredFields)
        {
            org.bridje.orm.Field fieldAnnot = declaredField.getAnnotation(org.bridje.orm.Field.class);
            if(fieldAnnot != null)
            {
                TableColumnImpl fInf = createColumn(declaredField);
                if(fInf.isKey())
                {
                    if(keyInf != null)
                    {
                        throw new IllegalArgumentException("Entity " + entity.getName() + " has more than one key field.");
                    }
                    keyInf = fInf;
                }
                columns.add(fInf);
            }
        }
        return keyInf;
    }

    public Stream<String> allFieldsStream(String prefix)
    {
        EntityContextImpl ctx = findContext();
        return columns.stream().map((field) -> prefix + ctx.getDialect().identifier(field.getName()));
    }

    public Stream<String> nonAiFieldsStream(String prefix)
    {
        EntityContextImpl ctx = findContext();
        return nonAiColumns.stream()
                        .map((field) -> prefix + ctx.getDialect()
                        .identifier(field.getName()));
    }
    
    public String allFieldsCommaSep()
    {
        EntityContextImpl ctx = findContext();
        return columns.stream()
                .map((column) -> column.writeSQL(null, ctx))
                .collect(Collectors.joining(", "));
    }

    public String nonAiFieldsCommaSep()
    {
        EntityContextImpl ctx = findContext();
        return nonAiColumns.stream()
                .filter((column) -> !column.isAutoIncrement())
                .map((column) -> column.writeSQL(null, ctx))
                .collect(Collectors.joining(", "));
    }

    public String allFieldsCommaSepNoTable()
    {
        EntityContextImpl ctx = findContext();
        return columns.stream()
                .map((column) -> ctx.getDialect().identifier(column.getName()))
                .collect(Collectors.joining(", "));
    }

    public String nonAiFieldsCommaSepNoTable()
    {
        EntityContextImpl ctx = findContext();
        return nonAiColumns.stream()
                .filter(c -> !c.isAutoIncrement())
                .map(c -> ctx.getDialect().identifier(c.getName()))
                .collect(Collectors.joining(", "));
    }

    public List<T> parseAll(ResultSet rs)
    {
        List<T> result = new ArrayList<>();
        try
        {
            while(rs.next())
            {
                T record = parseNew(rs);
                if(record != null)
                {
                    result.add(record);
                }
            }
        }
        catch (SQLException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return result;
    }

    public T parse(ResultSet rs) throws SQLException
    {
        if(rs.next()) return parseNew(rs);
        return null;
    }

    public T parse(T entity, ResultSet rs) throws SQLException
    {
        if(rs.next())
        {
            EntityContextImpl ctx = findContext();
            fillKey(entity, rs);
            ctx.getEnittysCache().put(entity, findKeyValue(entity));
            fill(entity, rs);
            return entity;
        }
        return null;
    }

    public <C> List<C> parseAll(int index, Column<C> column, ResultSet rs) throws SQLException
    {
        EntityContextImpl ctx = findContext();
        if(column instanceof AbstractColumn)
        {
            AbstractColumn<C> absColumn = (AbstractColumn)column;
            List<C> result = new ArrayList<>();
            while(rs.next())
            {
                C value = absColumn.readValue(index, rs, ctx);
                if(value != null) result.add((C)value);
            }
            return result;
        }
        else
        {
            return parseAll(index, column.getType(), rs);
        }
    }

    public <C> List<C> parseAll(int index, Class<C> type, ResultSet rs) throws SQLException
    {
        EntityContextImpl ctx = findContext();
        List<C> result = new ArrayList<>();
        while(rs.next())
        {
            Object value = CastUtils.castValue(type, rs.getObject(index), ctx);
            if(value != null) result.add((C)value);
        }
        return result;
    }

    public <C> C parse(int index, Column<C> column, ResultSet rs) throws SQLException
    {
        EntityContextImpl ctx = findContext();
        if(column instanceof AbstractColumn)
        {
            if(rs.next())
            {
                AbstractColumn<C> absColumn = (AbstractColumn<C>)column;
                return absColumn.readValue(index, rs, ctx);
            }
            return null;
        }
        else
        {
            return parse(index, column.getType(), rs);
        }
    }

    public <C> C parse(int index, Class<C> type, ResultSet rs) throws SQLException
    {
        if(rs.next())
        {
            EntityContextImpl ctx = findContext();
            Object value = CastUtils.castValue(type, rs.getObject(index), ctx);
            if(value != null) return (C)value;
        }
        return null;
    }

    public int parseCount(ResultSet rs) throws SQLException
    {
        if(rs.next()) return rs.getInt(1);
        return -1;
    }

    private T parseNew(ResultSet rs) throws SQLException
    {
        EntityContextImpl ctx = findContext();
        T entity = buildEntityObject();
        fillKey(entity, rs);
        ctx.getEnittysCache().put(entity, findKeyValue(entity));
        fill(entity, rs);
        return entity;
    }

    public T buildEntityObject()
    {
        try
        {
            return entity.newInstance();
        }
        catch (InstantiationException | IllegalAccessException ex)
        {
            throw new IllegalStateException("Cannot create Entity object for class " + entity.getName(), ex);
        }
    }

    private void fillKey(T entity, ResultSet rs) throws SQLException
    {
        EntityContextImpl ctx = findContext();
        TableColumnImpl keyImpl = ((TableColumnImpl)key);
        Object value = keyImpl.readValue(rs, ctx);
        keyImpl.setValue(entity, value);
    }
    
    private void fill(T entity, ResultSet rs) throws SQLException
    {
        for (TableColumn column : columns)
        {
            if(!column.isKey())
            {
                EntityContextImpl ctx = findContext();
                TableColumnImpl columnImpl = ((TableColumnImpl)column);
                Object value = columnImpl.readValue(rs, ctx);
                columnImpl.setValue(entity, value);
            }
        }
    }

    public <T> Object[] buildUpdateParameters(T entity, Object id)
    {
        List<Object> result = columns.stream()
                            .filter(fi -> !fi.isAutoIncrement())
                            .map(fi -> ((TableColumnImpl)fi).getQueryParameter(entity))
                            .collect(Collectors.toList());
        result.add(id);
        return result.toArray();
    }
    
    public <T> Object[] buildInsertParameters(T entity)
    {
        List<Object> result = columns.stream()
                                .filter(fi -> !fi.isAutoIncrement())
                                .map(fi -> ((TableColumnImpl)fi).getQueryParameter(entity))
                                .collect(Collectors.toList());
        return result.toArray();
    }

    @Override
    public Object findKeyValue(T entity)
    {
        return ((TableColumnImpl)key).getValue(entity);
    }
    
    public String buildIdCondition()
    {
        EntityContextImpl ctx = findContext();
        return ctx.getDialect().identifier(getName()) 
                + "." 
                + ctx.getDialect().identifier(key.getName()) + " = ?";
    }
    
    public String buildOrderBy(OrderBy orderBy, List<Object> parameters)
    {
        EntityContextImpl ctx = findContext();
        return orderBy.getColumn().writeSQL(parameters, ctx) + " " + (orderBy.getType() == OrderByType.ASC ? "ASC" : "DESC");
    }

    public <T> T updateKeyField(T entity, ResultSet rs) throws SQLException
    {
        ((TableColumnImpl)key).setValue(entity, parse(1, key.getType(), rs));
        return entity;
    }

    protected void initRelations(OrmService ormServ)
    {
        columns.stream()
                .filter((column) -> (column instanceof TableRelationColumnImpl))
                .map((column) -> (TableRelationColumnImpl)column)
                .forEach((relColumn) -> relColumn.initRelation(ormServ) );
    }

    private TableColumnImpl createColumn(Field declaredField)
    {
        if(declaredField.getType().getAnnotation(Entity.class) != null)
        {
            return new TableRelationColumnImpl(this, declaredField, declaredField.getType());
        }
        else if(String.class.equals(declaredField.getType()))
        {
            return new TableStringColumnImpl(this, declaredField);
        }
        return new TableColumnImpl(this, declaredField, declaredField.getType());
    }

    private EntityContextImpl findContext()
    {
        if(ormServ == null) ormServ = Ioc.context().find(OrmService.class);
        OrmModel model = ormServ.getModelForEntity(entity);
        if(model != null) return (EntityContextImpl)model.getContext();
        return null;
    }
}
