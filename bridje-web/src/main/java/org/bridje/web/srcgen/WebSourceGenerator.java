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

package org.bridje.web.srcgen;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.srcgen.SourceGenerator;
import org.bridje.srcgen.SrcGenService;
import org.bridje.web.srcgen.uisuite.ControlDef;
import org.bridje.web.srcgen.uisuite.UISuite;

@Component
public class WebSourceGenerator implements SourceGenerator
{
    @Inject
    private SrcGenService srcGen;

    @Override
    public void generateSources() throws IOException, JAXBException
    {
        List<UISuite> uiSuites = srcGen.findData("web/*.xml", UISuite.class);
        Map<String, Object> data;
        for (UISuite uiSuite : uiSuites)
        {
            data = new HashMap<>();
            data.put("uisuite", uiSuite);
            srcGen.createResource(uiSuite.getName(), "web/Theme.ftl", data);
            for (ControlDef controlDef : uiSuite.getControls())
            {
                data = new HashMap<>();
                data.put("uisuite", uiSuite);
                data.put("control", controlDef);
                srcGen.createClass(controlDef.getFullName(), "web/Control.ftl", data);
            }
        }
    }
}
