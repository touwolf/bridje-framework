/*
 * Copyright 2017 Bridje Framework.
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

package org.bridje.web.srcgen.editors;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import org.bridje.jfx.utils.JfxUtils;
import org.bridje.srcgen.editor.EditorTreeItem;
import org.bridje.web.srcgen.models.UISuiteModel;
import org.bridje.web.srcgen.models.UISuitesModel;

public final class UISuiteTreeItem extends EditorTreeItem
{
    private final static UISuiteEditor editor = new UISuiteEditor();

    private final UISuiteModel suite;
    
    public UISuiteTreeItem(UISuiteModel suite)
    {
        super(suite, UISuitesModel.uisuite(16));
        this.suite = suite;
        setContextMenu(createContextMenu());
        setToolBar(createToolBar());

        TreeItem<Object> tiStandalone = new StandaloneTreeItem(suite.getStandalone());
        TreeItem<Object> tiDefines = new StandaloneTreeItem(suite.getDefines());
        TreeItem<Object> tiResources = new ResourcesTreeItem(suite);
        TreeItem<Object> tiTemplates = new TemplatesTreeItem(suite);
        TreeItem<Object> tiControls = new ControlsTreeItem(suite);

        getChildren().addAll(tiStandalone, tiDefines, tiResources, tiTemplates, tiControls);
    }

    private ContextMenu createContextMenu()
    {
        ContextMenu ctx = new ContextMenu();
        ctx.getItems().add(JfxUtils.createMenuItem("Save", UISuitesModel.save(24), this::saveModel));
        return ctx;
    }

    private ToolBar createToolBar()
    {
        ToolBar tb = new ToolBar();
        tb.getItems().add(JfxUtils.createToolButton(UISuitesModel.save(32), this::saveModel));
        return tb;
    }

    public void saveModel(ActionEvent event)
    {
        ModelUtils.saveUISuite(suite);
    }

    @Override
    public Node edit()
    {
        editor.setUISuite(suite);
        return editor;
    }
}