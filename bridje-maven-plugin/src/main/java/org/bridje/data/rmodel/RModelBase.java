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

package org.bridje.data.rmodel;

/**
 * 
 */
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public abstract class RModelBase
{
            
    public static RModel loadModel(java.io.File source) throws javax.xml.bind.JAXBException
    {
        javax.xml.bind.JAXBContext ctx = javax.xml.bind.JAXBContext.newInstance(RModel.class);
        javax.xml.bind.Unmarshaller unmarsh = ctx.createUnmarshaller();
        return (RModel)unmarsh.unmarshal(source);
    }

    public static void generateSchema(final java.io.File target) throws javax.xml.bind.JAXBException, java.io.IOException
    {
        javax.xml.bind.JAXBContext ctx = javax.xml.bind.JAXBContext.newInstance(RModel.class);
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