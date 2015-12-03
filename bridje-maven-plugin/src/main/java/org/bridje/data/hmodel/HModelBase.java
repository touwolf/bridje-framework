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

/**
 * Hierarchical model definition.
 */
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public abstract class HModelBase extends HEntityData
{
    @javax.xml.bind.annotation.XmlAttribute
    private String packageName;
    
    @javax.xml.bind.annotation.XmlAttribute
    private String namespace;
    
    private String license;
    
    @javax.xml.bind.annotation.XmlElementWrapper(name = "entitys")
    @javax.xml.bind.annotation.XmlElements(
    {
        @javax.xml.bind.annotation.XmlElement(name = "entity", type = HEntity.class)
    })
    private java.util.List<HEntity> entitys;
    
    @javax.xml.bind.annotation.XmlElementWrapper(name = "enums")
    @javax.xml.bind.annotation.XmlElements(
    {
        @javax.xml.bind.annotation.XmlElement(name = "enum", type = HEnum.class)
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
        if(this.entitys == null)
        {
            return null;
        }
        return java.util.Collections.unmodifiableList(this.entitys);
    }

    /**
     * List of model enumerators
     * @return A java.util.List&lt;HEnum&gt; object representing the value of the enums field.
     */
    public java.util.List<HEnum> getEnums()
    {
        if(this.enums == null)
        {
            return null;
        }
        return java.util.Collections.unmodifiableList(this.enums);
    }

    /**
     * Adds a HEntity object to the entitys list.
     * @param value The object to be added
     */
    public void addEntity(HEntity value)
    {
        if(value == null)
        {
            return;
        }
        if(this.entitys == null)
        {
            this.entitys = new java.util.ArrayList<>();
        }
        this.entitys.add(value);
    }

    /**
     * Removes a HEntity object from the entitys list.
     * @param value The object to be removed.
     */
    public void removeEntity(HEntity value)
    {
        if(value == null)
        {
            return;
        }
        if(this.entitys == null)
        {
            return;
        }
        this.entitys.remove(value);
    }

    /**
     * Adds a HEnum object to the enums list.
     * @param value The object to be added
     */
    public void addEnum(HEnum value)
    {
        if(value == null)
        {
            return;
        }
        if(this.enums == null)
        {
            this.enums = new java.util.ArrayList<>();
        }
        this.enums.add(value);
    }

    /**
     * Removes a HEnum object from the enums list.
     * @param value The object to be removed.
     */
    public void removeEnum(HEnum value)
    {
        if(value == null)
        {
            return;
        }
        if(this.enums == null)
        {
            return;
        }
        this.enums.remove(value);
    }

            
    public static HModel loadModel(java.io.File source) throws javax.xml.bind.JAXBException
    {
        javax.xml.bind.JAXBContext ctx = javax.xml.bind.JAXBContext.newInstance(HModel.class);
        javax.xml.bind.Unmarshaller unmarsh = ctx.createUnmarshaller();
        return (HModel)unmarsh.unmarshal(source);
    }

    public static void generateSchema(final java.io.File target) throws javax.xml.bind.JAXBException, java.io.IOException
    {
        javax.xml.bind.JAXBContext ctx = javax.xml.bind.JAXBContext.newInstance(HModel.class);
        ctx.generateSchema(new javax.xml.bind.SchemaOutputResolver()
        {
            @Override
            public javax.xml.transform.Result createOutput(String namespaceUri, String suggestedFileName) throws java.io.IOException
            {
                return new javax.xml.transform.stream.StreamResult(target);
            }
        });
    }
}