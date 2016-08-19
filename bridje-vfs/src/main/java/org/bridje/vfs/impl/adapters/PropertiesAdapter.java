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

package org.bridje.vfs.impl.adapters;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import org.bridje.ioc.Component;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFileAdapter;

@Component
class PropertiesAdapter implements VFileAdapter
{
    @Override
    public String[] getExtensions()
    {
        return new String[]{"properties"};
    }

    @Override
    public Class<?>[] getClasses()
    {
        return new Class<?>[]{ Properties.class };
    }

    @Override
    public boolean canHandle(VFile vf, Class<?> resultCls)
    {
        return true;
    }

    @Override
    public <T> T read(VFile vf, Class<T> resultCls) throws IOException
    {
        try(InputStream is = vf.openForRead())
        {
            Properties prop = new Properties();
            prop.load(is);
            return (T)prop;
        }
    }

    @Override
    public <T> void write(VFile vf, T contentObj) throws IOException
    {
        try(OutputStream os = vf.openForWrite())
        {
            Properties prop = (Properties)contentObj;
            prop.store(os, null);
        }
    }
}
