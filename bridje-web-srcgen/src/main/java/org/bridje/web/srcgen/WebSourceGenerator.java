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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TreeItem;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.srcgen.SourceGenerator;
import org.bridje.srcgen.SrcGenService;
import org.bridje.vfs.VFile;
import org.bridje.web.srcgen.editor.ModelUtils;
import org.bridje.web.srcgen.editor.PartialUISuiteTreeItem;
import org.bridje.web.srcgen.editor.UISuiteTreeItem;
import org.bridje.web.srcgen.uisuite.ControlDef;
import org.bridje.web.srcgen.uisuite.ControlEnum;
import org.bridje.web.srcgen.uisuite.PartialUISuite;
import org.bridje.web.srcgen.uisuite.UISuite;

/**
 * This components is responsible for the source code generation of the web
 * controls and freemarker themes for the given UISuite.
 */
@Component
public class WebSourceGenerator implements SourceGenerator<UISuite>
{
    private static final Logger LOG = Logger.getLogger(WebSourceGenerator.class.getName());

    @Inject
    private SrcGenService srcGen;

    /**
     * This method generates the source code for the controls and the ftl theme
     * for the given UISuite.
     *
     * @param uiSuite The UISuite definition.
     *
     * @throws IOException If any i/o exception occurs reading or writing the source files.
     */
    @Override
    public void generateSources(UISuite uiSuite, VFile file) throws IOException
    {
        if(uiSuite.getName() == null)
        {
            LOG.log(Level.SEVERE, "The UISuite for {0} does not have a name.", file.getPath().toString());
            return;
        }
        if(uiSuite.getPackage() == null)
        {
            LOG.log(Level.SEVERE, "The UISuite for {0} does not have a package.", file.getPath().toString());
            return;            
        }
        Map<String, Object> data = new HashMap<>();
        uiSuite.processIncludes(file.getParent());
        data.put("uisuite", uiSuite);
        srcGen.createResource("BRIDJE-INF/web/themes/" + uiSuite.getName().toLowerCase() + "/Theme.ftl", "web/Theme.ftl", data);
        srcGen.createClass(uiSuite.getPackage() + "/package-info", "web/package-info.ftl", data);
        String fullName = uiSuite.getPackage() + "." + uiSuite.getName();
        srcGen.createClass(fullName + "AbstractView", "web/AbstractView.ftl", data);
        srcGen.createClass(fullName + "Defines", "web/Defines.ftl", data);
        srcGen.createClass(fullName + "ExtendsFrom", "web/ExtendsFrom.ftl", data);
        srcGen.createClass(fullName + "Layout", "web/Layout.ftl", data);
        srcGen.createClass(fullName + "MetaTag", "web/MetaTag.ftl", data);
        srcGen.createClass(fullName + "Standalone", "web/Standalone.ftl", data);
        srcGen.createClass(fullName + "View", "web/View.ftl", data);
        if(uiSuite.getControls() != null)
        {
            for (ControlDef controlDef : uiSuite.getControls())
            {
                data = new HashMap<>();
                data.put("uisuite", uiSuite);
                data.put("control", controlDef);
                srcGen.createClass(controlDef.getFullName(), "web/Control.ftl", data);
            }
        }

        if(uiSuite.getEnums() != null)
        {
            for(ControlEnum en : uiSuite.getEnums())
            {
                data = new HashMap<>();
                data.put("uisuite", uiSuite);
                data.put("enum", en);
                srcGen.createClass(en.getFullName(), "web/Enum.ftl", data);
            }
        }
    }

    @Override
    public Map<UISuite, VFile> findData()
    {
        try
        {
            return srcGen.findData(UISuite.class);
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public TreeItem<Object> createTreeItem(VFile file)
    {
        try
        {
            UISuite suite = srcGen.readFile(file, UISuite.class);
            if(suite != null)
            {
                UISuiteTreeItem result = new UISuiteTreeItem(ModelUtils.toModel(suite, file));
                return result;
            }
            PartialUISuite partialSuite = srcGen.readFile(file, PartialUISuite.class);
            if(partialSuite != null)
            {
                PartialUISuiteTreeItem result = new PartialUISuiteTreeItem(ModelUtils.toPartialModel(partialSuite, file));
                return result;
            }
        }
        catch (IOException e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }
}
