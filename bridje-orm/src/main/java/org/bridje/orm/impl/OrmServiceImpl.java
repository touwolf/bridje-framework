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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.impl.ClassUtils;
import org.bridje.ioc.thls.Thls;
import org.bridje.ioc.thls.ThlsAction;
import org.bridje.ioc.thls.ThlsActionException;
import org.bridje.ioc.thls.ThlsActionException2;
import org.bridje.jdbc.JdbcService;
import org.bridje.orm.DataSourcesSetup;
import org.bridje.orm.DbObject;
import org.bridje.orm.Entity;
import org.bridje.orm.EntityContext;
import org.bridje.orm.OrmModel;
import org.bridje.orm.OrmService;
import org.bridje.orm.SQLDialect;
import org.bridje.orm.Table;
import org.bridje.orm.TableColumn;

@Component
class OrmServiceImpl implements OrmService
{
    private static final Logger LOG = Logger.getLogger(OrmServiceImpl.class.getName());

    public static final String ENTITYS_RESOURCE_FILE = "BRIDJE-INF/orm/entities.properties";

    private Map<Class<?>, TableImpl<?>> tablesMap;

    private Map<Class<?>, List<Class<?>>> modelsEntitysMap;

    private Map<Class<?>, List<TableImpl<?>>> modelsTablesMap;

    @Inject
    private JdbcService jdbcServ;
    
    @Inject
    private SQLDialect[] dialects;

    @PostConstruct
    public void init()
    {
        this.tablesMap = new HashMap<>();
        try
        {
            createTables();
            createModelsEntitys();
        }
        catch (IOException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public <T extends OrmModel> T createModel(String dsName, Class<T> modelCls)
    {
        EntityContext ctx = createContext(dsName);
        return instantiateModel(ctx, modelCls);
    }

    @Override
    public <T extends OrmModel> T createModel(DataSource ds, Class<T> modelCls)
    {
        EntityContext ctx = createContext(ds);
        return instantiateModel(ctx, modelCls);
    }

    private EntityContext createContext(String dsName)
    {
        return createContext(jdbcServ.getDataSource(dsName));
    }

    private EntityContext createContext(DataSource ds)
    {
        if(ds == null)
        {
            throw new IllegalArgumentException("No datasource was specified.");
        }
        for (SQLDialect dialect : dialects)
        {
            if(dialect.canHandle(ds))
            {
                return new EntityContextImpl(this, ds, dialect);
            }
        }
        throw new IllegalArgumentException("Can´t find a valid dialect for this DataSource");
    }
    
    @Override
    public <T> TableImpl<T> findTable(Class<T> entity)
    {
        TableImpl<T> result = (TableImpl<T>)this.tablesMap.get(entity);
        if(result == null)
        {
            throw new IllegalArgumentException(entity.getName() + " is not an entity class.");
        }
        return result;
    }

    @Override
    public <T> Class<? extends OrmModel> findModelClass(Class<T> entity)
    {
        Entity annotation = entity.getAnnotation(Entity.class);
        if(annotation == null)
        {
            throw new IllegalArgumentException(entity.getName() + " is not an entity class.");
        }
        return annotation.model();
    }

    @Override
    public <T extends OrmModel> List<Class<?>> findEntitys(Class<T> modelClass)
    {
        return Collections.unmodifiableList(modelsEntitysMap.get(modelClass));
    }

    @Override
    public <T extends OrmModel> List<Table<?>> findTables(Class<T> modelClass)
    {
        return Collections.unmodifiableList(modelsTablesMap.get(modelClass).stream().map(t -> (TableImpl<?>)t).collect(Collectors.toList()));
    }

    private void createModelsEntitys() throws IOException
    {
        modelsEntitysMap = new HashMap<>();
        tablesMap.forEach((e, t) -> addEntityToModel(e));
        modelsTablesMap = new HashMap<>();
        tablesMap.forEach((e, t) -> addTableToModel(e, t));
    }
    
    private void addEntityToModel(Class<?> entityClass)
    {
        Entity annotation = entityClass.getAnnotation(Entity.class);
        List<Class<?>> lst = modelsEntitysMap.get(annotation.model());
        if(lst == null)
        {
            lst = new ArrayList<>();
            modelsEntitysMap.put(annotation.model(), lst);
        }
        lst.add(entityClass);
    }
    
    private void addTableToModel(Class<?> entityClass, TableImpl<?> table)
    {
        Entity annotation = entityClass.getAnnotation(Entity.class);
        List<TableImpl<?>> lst = modelsTablesMap.get(annotation.model());
        if(lst == null)
        {
            lst = new ArrayList<>();
            modelsTablesMap.put(annotation.model(), lst);
        }
        lst.add(table);
    }

    private void createTables() throws IOException
    {
        List<URL> files = findModelsFiles();
        files.stream()
                .map((url) -> readFile(url))
                .forEach((prop) -> prop.forEach(this::createTable));
        injectDbObjects();
        tablesMap.forEach((k, v) -> v.initRelations(this));
    }

    private List<URL> findModelsFiles() throws IOException
    {
        List<URL> urls = new ArrayList<>();
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(ENTITYS_RESOURCE_FILE);
        while (resources.hasMoreElements())
        {
            URL nextElement = resources.nextElement();
            urls.add(nextElement);
        }
        return urls;
    }

    private Properties readFile(URL url)
    {
        Properties prop = new Properties();
        try (InputStream is = url.openStream())
        {
            prop.load(is);
        }
        catch(IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return prop;
    }
    
    private void createTable(Object objClsName, Object objTableName)
    {
        try
        {
            String clsName = (String)objClsName;
            String tableName = (String)objTableName;
            Class cls = Class.forName(clsName);
            findOrTableEntity(cls, tableName);
        }
        catch (ClassNotFoundException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private <T> TableImpl<T> findOrTableEntity(Class<T> entityClass, String tableName)
    {
        if(!tablesMap.containsKey(entityClass))
        {
            tablesMap.put(entityClass, new TableImpl<>(entityClass, tableName));
        }
        return (TableImpl<T>) tablesMap.get(entityClass);
    }

    private void injectDbObjects()
    {
        tablesMap.forEach((entity, table) -> this.injectDbObject(entity));
    }

    private void injectDbObject(Class<?> entity)
    {
        try
        {
            Field[] fields = entity.getDeclaredFields();
            for (Field field : fields)
            {
                DbObject dbObject = field.getAnnotation(DbObject.class);
                if (Modifier.isStatic(field.getModifiers()) 
                        && dbObject != null)
                {
                    if(field.getType().equals(Table.class))
                    {
                        Type param = ClassUtils.parameterType(field.getGenericType(), 0);
                        if(param != null)
                        {
                            field.set(null, findTable(ClassUtils.rawClass(param)));
                        }
                    }
                    else if(TableColumn.class.isAssignableFrom(field.getType()))
                    {
                        Type param = ClassUtils.parameterType(field.getGenericType(), 0);
                        if(param != null)
                        {
                            TableImpl tb = findTable(ClassUtils.rawClass(param));
                            if(tb != null)
                            {
                                field.set(null, tb.findColumn(dbObject.value()));
                            }
                        }                        
                    }
                }
            }
        }
        catch (SecurityException | IllegalArgumentException | IllegalAccessException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private <T extends OrmModel> T instantiateModel(EntityContext ctx, Class<T> modelCls)
    {
        try
        {
            Constructor<T> constructor = modelCls.getDeclaredConstructor(EntityContext.class);
            if(constructor != null)
            {
                constructor.setAccessible(true);
                return constructor.newInstance(ctx);
            }
        }
        catch (NoSuchMethodException | SecurityException 
                | IllegalAccessException | IllegalArgumentException 
                | InvocationTargetException | InstantiationException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public <T> T doWithModels(ThlsAction<T> action, DataSourcesSetup setup)
    {
        return Thls.doAs(action, ModelsThlsProvider.class, new ModelsThlsProvider(setup, this));
    }

    @Override
    public <T, E extends Throwable> T doWithModelsEx(ThlsActionException<T, E> action, DataSourcesSetup setup) throws E
    {
        return Thls.doAsEx(action, ModelsThlsProvider.class, new ModelsThlsProvider(setup, this));
    }

    @Override
    public <T, E extends Throwable, E2 extends Throwable> T doWithModelsEx2(ThlsActionException2<T, E, E2> action, DataSourcesSetup setup) throws E, E2
    {
        return Thls.doAsEx2(action, ModelsThlsProvider.class, new ModelsThlsProvider(setup, this));
    }

    @Override
    public <T extends OrmModel> T getModel(Class<T> modelClass)
    {
        return Thls.get(ModelsThlsProvider.class).getModel(modelClass);
    }
}
