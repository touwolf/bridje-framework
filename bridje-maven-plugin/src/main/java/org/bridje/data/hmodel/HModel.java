/**
 * 
 * Copyright 2015 Bridje Framework.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *     
 */

package org.bridje.data.hmodel;

import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

/**
 * 
        Hierarchical model definition.
    
 */
@XmlRootElement(name = "hmodel")
@XmlAccessorType(XmlAccessType.FIELD)
public class HModel extends HEntityData
{
    @XmlAttribute
    private String packageName;
    
    @XmlAttribute
    private String namespace;
    
    private String license;
    
    @XmlElementWrapper(name = "entitys")
    @XmlElements(
    {
        @XmlElement(name = "entity", type = HEntity.class)
    })
    private java.util.List<HEntity> entitys;
    
    @XmlElementWrapper(name = "enums")
    @XmlElements(
    {
        @XmlElement(name = "enum", type = HEnum.class)
    })
    private java.util.List<HEnum> enums;
    
    /**
     * Package name for java clases.
     * @return A String object representing the value of the packageName field.
     */
    public String getPackageName()
    {
        return this.packageName;
    }

    /**
     * Package name for java clases.
     * @param packageName The new value for the packageName field.
     */
    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }

    /**
     * Target namespace for the xml schema.
     * @return A String object representing the value of the namespace field.
     */
    public String getNamespace()
    {
        return this.namespace;
    }

    /**
     * Target namespace for the xml schema.
     * @param namespace The new value for the namespace field.
     */
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }

    /**
     * License text to be used for this model.
     * @return A String object representing the value of the license field.
     */
    public String getLicense()
    {
        return this.license;
    }

    /**
     * License text to be used for this model.
     * @param license The new value for the license field.
     */
    public void setLicense(String license)
    {
        this.license = license;
    }

    /**
     * List of model entitys
     * @return A java.util.List&lt;HEntity&gt; object representing the value of the entitys field.
     */
    public java.util.List<HEntity> getEntitys()
    {
        return this.entitys;
    }

    /**
     * List of model entitys
     * @param entitys The new value for the entitys field.
     */
    public void setEntitys(java.util.List<HEntity> entitys)
    {
        this.entitys = entitys;
    }

    /**
     * List of model enumerators
     * @return A java.util.List&lt;HEnum&gt; object representing the value of the enums field.
     */
    public java.util.List<HEnum> getEnums()
    {
        return this.enums;
    }

    /**
     * List of model enumerators
     * @param enums The new value for the enums field.
     */
    public void setEnums(java.util.List<HEnum> enums)
    {
        this.enums = enums;
    }

            
    public static HModel loadModel(File source) throws JAXBException
    {
        JAXBContext ctx = JAXBContext.newInstance(HModel.class);
        Unmarshaller unmarsh = ctx.createUnmarshaller();
        return (HModel)unmarsh.unmarshal(source);
    }

    public static void generateSchema(final File target) throws JAXBException, IOException
    {
        JAXBContext ctx = JAXBContext.newInstance(HModel.class);
        ctx.generateSchema(new SchemaOutputResolver()
        {
            @Override
            public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException
            {
                return new StreamResult(target);
            }
        });
    }
}