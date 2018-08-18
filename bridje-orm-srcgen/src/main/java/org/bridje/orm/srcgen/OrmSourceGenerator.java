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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
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
 * entities and enumerators for that model.
 */
@Component
public class OrmSourceGenerator implements SourceGenerator<ModelInf>
{
    private static final Logger LOG = Logger.getLogger(OrmSourceGenerator.class.getName());

    @Inject
    private SrcGenService srcGen;

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
        for (EntityInf entity : modelInf.getEntities())
        {
            entity.doExtendsBase();
        }

        Map<String, Object> data;
        data = new HashMap<>();
        data.put("model", modelInf);
        srcGen.createClass(modelInf.getFullName() + "Base", "orm/Model.ftl", data);
        srcGen.createClass(modelInf.getFullName() + "Types", "orm/Types.ftl", data);

        data = new HashMap<>();
        data.put("model", modelInf);
        for (EntityInf entity : modelInf.getEntities())
        {
            data.put("entity", entity);
            if(entity.getIsAbstract())
            {
                srcGen.createClass(entity.getFullName(), "orm/AbstractEntity.ftl", data);
            }
            else
            {
                srcGen.createClass(entity.getFullName(), "orm/Entity.ftl", data);
                srcGen.createClass(entity.getFullName() + "_", "orm/Entity_.ftl", data);
            }
        }

        data = new HashMap<>();
        data.put("model", modelInf);
        for (EnumBaseInf enumInf : modelInf.getEnums())
        {
            if (enumInf instanceof EnumInf)
            {
                data.put("enum", enumInf);
                srcGen.createClass(enumInf.getFullName(), "orm/Enum.ftl", data);
            }
        }
    }
}
