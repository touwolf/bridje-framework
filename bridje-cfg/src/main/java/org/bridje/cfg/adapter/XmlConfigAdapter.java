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

package org.bridje.cfg.adapter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.bridje.cfg.ConfigAdapter;
import org.bridje.ioc.Component;

/**
 * This adapter use JAXB to write an read configuration objects into/from xml
 * files, configuration classes using this adapter must be annotated with
 * XmlRootElement annotation.
 */
@Component
public class XmlConfigAdapter implements ConfigAdapter
{
    @Override
    public String findFileName(String name, Class<?> cls)
    {
        return name + ".xml";
    }

    @Override
    public String findDefaultFileName(Class<?> cls)
    {
        XmlRootElement ann = cls.getAnnotation(XmlRootElement.class);
        if(ann != null && ann.name() != null && !ann.name().trim().isEmpty() && !ann.name().equalsIgnoreCase("##default"))
        {
            return ann.name() + ".xml";
        }
        return cls.getSimpleName().toLowerCase() + ".xml";
    }

    @Override
    public void write(Object newConfig, Writer writer) throws IOException
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(newConfig.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(newConfig, writer);
        }
        catch (JAXBException ex)
        {
            throw new IOException(ex.getMessage(), ex);
        }
    }

    @Override
    public Object read(Class<?> cls, Reader reader) throws IOException
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(cls);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Object result = unmarshaller.unmarshal(reader);
            return result;
        }
        catch (JAXBException ex)
        {
            throw new IOException(ex.getMessage(), ex);
        }
    }
}
