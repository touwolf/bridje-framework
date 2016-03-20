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
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Component;

/**
 *
 */
@Component
class OrmMetaInfService
{
    private static final Logger LOG = Logger.getLogger(OrmMetaInfService.class.getName());

    private final Map<Class<?>, EntityInf<?>> entitysMap;

    public OrmMetaInfService()
    {
        this.entitysMap = new HashMap<>();
        try
        {
            fillEntitys();
            this.entitysMap.forEach((k, e) -> e.fillRelations());
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public <T> EntityInf<T> findEntityInf(Class<?> entityClass)
    {
        return (EntityInf<T>)this.entitysMap.get(entityClass);
    }
    
    private void fillEntitys() throws IOException
    {
        List<URL> files = findModelsFiles();
        files.stream()
                .map((url) -> readFile(url))
                .forEach((prop) -> prop.forEach(this::createEntity));
    }

    private List<URL> findModelsFiles() throws IOException
    {
        List<URL> urls = new ArrayList<>();
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(EntityProcessor.COMPONENTS_RESOURCE_FILE);
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
        catch(Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return prop;
    }
    
    private void createEntity(Object objClsName, Object objTableName)
    {
        try
        {
            String clsName = (String)objClsName;
            String tableName = (String)objTableName;
            Class cls = Class.forName(clsName);
            findOrCreateEntity(cls, tableName);
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private <T> EntityInf<T> findOrCreateEntity(Class<T> entityClass, String tableName)
    {
        if(!entitysMap.containsKey(entityClass))
        {
            entitysMap.put(entityClass, new EntityInf<>(entityClass, tableName));
        }
        return (EntityInf<T>) entitysMap.get(entityClass);
    }
}