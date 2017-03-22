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

package org.bridje.orm.srcgen;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.orm.impl.SQLCustomTypeProcessor;
import org.bridje.orm.srcgen.model.CustomTypesProvider;
import org.bridje.orm.srcgen.model.EntityInf;
import org.bridje.orm.srcgen.model.EnumBaseInf;
import org.bridje.orm.srcgen.model.EnumInf;
import org.bridje.orm.srcgen.model.ModelInf;
import org.bridje.srcgen.SourceGenerator;
import org.bridje.srcgen.SrcGenService;
import org.bridje.vfs.VFile;

/**
 * This components is the source code generator for the ORM API. it will read
 * the ORM model files in from the SrcGenService and will generate al the
 * entityts and enumerators for that model.
 */
@Component
public class OrmSourceGenerator implements SourceGenerator<ModelInf>, CustomTypesProvider
{
    private static final Logger LOG = Logger.getLogger(OrmSourceGenerator.class.getName());

    @Inject
    private SrcGenService srcGen;

    private Map<String, String> customTypes;

    private Map<String, String> findCustomTypes() throws IOException
    {
        Map<String, String> result = new HashMap<>();
        Enumeration<URL> resources = loadDataTypeFiles();
        while (resources.hasMoreElements())
        {
            URL url = resources.nextElement();
            Properties prop = loadProperties(url);
            prop.forEach((k, v) -> result.put((String) k, (String) v));
        }
        return result;
    }

    @Override
    public String getJavaType(String type)
    {
        try
        {
            if (customTypes == null) customTypes = findCustomTypes();
            String value = customTypes.get(type);
            if (value == null || value.isEmpty()) return null;
            String[] arr = value.split(":");
            return arr[0];
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public String getColumnClass(String type)
    {
        try
        {
            if (customTypes == null) customTypes = findCustomTypes();
            String value = customTypes.get(type);
            if (value == null || value.isEmpty()) return null;
            String[] arr = value.split(":");
            return arr[1];
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public Map<ModelInf, VFile> findData()
    {
        try
        {
            return srcGen.findData(ModelInf.class);
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public void generateSources(ModelInf modelInf, VFile file) throws IOException
    {
        Map<String, Object> data;
        data = new HashMap<>();
        data.put("model", modelInf);
        srcGen.createClass(modelInf.getFullName(), "orm/Model.ftl", data);

        data = new HashMap<>();
        data.put("model", modelInf);
        for (EntityInf entity : modelInf.getEntities())
        {
            data.put("entity", entity);
            srcGen.createClass(entity.getFullName(), "orm/Entity.ftl", data);
        }

        data = new HashMap<>();
        data.put("model", modelInf);
        for (EnumBaseInf enumInf : modelInf.getEnums())
        {
            if(enumInf instanceof EnumInf)
            {
                data.put("enum", enumInf);
                srcGen.createClass(enumInf.getFullName(), "orm/Enum.ftl", data);
            }
        }
    }

    @Override
    public String toString()
    {
        return "ORM Models";
    }

    private Properties loadProperties(URL url) throws IOException
    {
        Properties prop = new Properties();
        try (InputStream is = url.openStream())
        {
            prop.load(is);
        }
        return prop;
    }

    private Enumeration<URL> loadDataTypeFiles() throws IOException
    {
        return getClass().getClassLoader().getResources(SQLCustomTypeProcessor.CUSTOM_DATATYPE_FILE);
    }
}
