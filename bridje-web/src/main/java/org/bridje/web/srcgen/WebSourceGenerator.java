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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.srcgen.SourceGenerator;
import org.bridje.srcgen.SrcGenService;
import org.bridje.web.srcgen.uisuite.ControlDef;
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
    public void generateSources(UISuite uiSuite) throws IOException
    {
        Map<String, Object> data = new HashMap<>();
        data.put("uisuite", uiSuite);
        srcGen.createResource("BRIDJE-INF/web/themes/" + uiSuite.getName().toLowerCase() + "/Theme.ftl", "web/Theme.ftl", data);
        srcGen.createClass(uiSuite.getPackage() + "/package-info", "web/package-info.ftl", data);
        for (ControlDef controlDef : uiSuite.getControls())
        {
            data = new HashMap<>();
            data.put("uisuite", uiSuite);
            data.put("control", controlDef);
            srcGen.createClass(controlDef.getFullName(), "web/Control.ftl", data);
        }
    }

    @Override
    public List<UISuite> findData()
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
    public String getName(UISuite data)
    {
        return data.getName();
    }

    @Override
    public String getName()
    {
        return "Web UI Suites";
    }

    @Override
    public TreeItem<UISuite> createTreeNode(UISuite data)
    {
        TreeItem<UISuite> treeView = new TreeItem<>(data, createImageView("uisuite.png"));
        return treeView;
    }

    private static ImageView createImageView(String image)
    {
        ImageView imageView = new ImageView(new Image(WebSourceGenerator.class.getResourceAsStream(image)));
        imageView.setFitHeight(18);
        imageView.setFitWidth(18);
        return imageView;
    }

    @Override
    public ImageView getImage()
    {
        return createImageView("uisuite.png");
    }
}
