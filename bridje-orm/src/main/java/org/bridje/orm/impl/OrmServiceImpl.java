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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.impl.ClassUtils;
import org.bridje.jdbc.JdbcService;
import org.bridje.orm.DbObject;
import org.bridje.orm.EntityContext;
import org.bridje.orm.OrmService;
import org.bridje.orm.SQLDialect;
import org.bridje.orm.Table;
import org.bridje.orm.TableColumn;

/**
 *
 */
@Component
class OrmServiceImpl implements OrmService
{
    private static final Logger LOG = Logger.getLogger(OrmServiceImpl.class.getName());

    public static final String COMPONENTS_RESOURCE_FILE = "BRIDJE-INF/orm/entitys.properties";

    private Map<Class<?>, TableImpl<?>> tablesMap;

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
        }
        catch (IOException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Override
    public EntityContext createContext(String dsName)
    {
        return createContext(jdbcServ.getDataSource(dsName));
    }

    @Override
    public EntityContext createContext(DataSource ds)
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
    
    private void createTables() throws IOException
    {
        List<URL> files = findModelsFiles();
        files.stream()
                .map((url) -> readFile(url))
                .forEach((prop) -> prop.forEach(this::createTable));
        injectDbObjects();
    }

    private List<URL> findModelsFiles() throws IOException
    {
        List<URL> urls = new ArrayList<>();
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(COMPONENTS_RESOURCE_FILE);
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
        tablesMap.forEach(this::injectDbObject);
    }

    private void injectDbObject(Class<?> entity, TableImpl<?> table)
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
                    else if(field.getType().equals(TableColumn.class))
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
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
