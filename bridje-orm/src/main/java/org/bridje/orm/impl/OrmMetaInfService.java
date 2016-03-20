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
import org.bridje.ioc.impl.ComponentProcessor;

/**
 *
 */
@Component
class OrmMetaInfService
{
    private static final Logger LOG = Logger.getLogger(OrmMetaInfService.class.getName());

    private final Map<String, EntitysModel> models;

    private final Map<Class, EntitysModel> modelsToEntitys;

    public OrmMetaInfService()
    {
        this.models = new HashMap<>();
        this.modelsToEntitys = new HashMap<>();
        try
        {
            fillModels();
            this.models.forEach((k, m) -> m.fillRelations());
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public EntitysModel getModel(String modelName)
    {
        return models.get(modelName);
    }
    
    public EntitysModel getModel(Class entityClass)
    {
        return modelsToEntitys.get(entityClass);
    }

    private void fillModels() throws IOException
    {
        List<URL> files = findModelsFiles();
        files.stream()
                .map((url) -> readFile(url))
                .forEach((prop) -> prop.forEach(this::createEntity));
    }

    private List<URL> findModelsFiles() throws IOException
    {
        List<URL> urls = new ArrayList<>();
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(ComponentProcessor.COMPONENTS_RESOURCE_FILE);
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
    
    private void createEntity(Object objClsName, Object objModelName)
    {
        try
        {
            String clsName = (String)objClsName;
            String modelName = (String)objModelName;
            EntitysModel model = findOrCreateModel(modelName);
            Class cls = Class.forName(clsName);
            model.addEntity(cls);
            modelsToEntitys.put(cls, model);
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private EntitysModel findOrCreateModel(String modelName)
    {
        if(!models.containsKey(modelName))
        {
            models.put(modelName, new EntitysModel(modelName));
        }
        return models.get(modelName);
    }
}
