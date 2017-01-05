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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.orm.srcgen.inf.EntityInf;
import org.bridje.orm.srcgen.inf.EnumInf;
import org.bridje.orm.srcgen.inf.ModelInf;
import org.bridje.srcgen.SourceGenerator;
import org.bridje.srcgen.SrcGenService;

@Component
public class OrmSourceGenerator implements SourceGenerator
{
    @Inject
    private SrcGenService srcGen;
    
    @Override
    public void generateSources() throws IOException, JAXBException
    {
        List<ModelInf> models = srcGen.findData("orm/*.xml", ModelInf.class);
        Map<String, Object> data;
        for (ModelInf modelInf : models)
        {
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
            for (EnumInf enumInf : modelInf.getEnums())
            {
                data.put("enum", enumInf);
                srcGen.createClass(enumInf.getFullName(), "orm/Enum.ftl", data);
            }
        }
    }
    
}
