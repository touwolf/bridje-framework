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
import org.bridje.jfx.utils.JfxUtils;
import org.bridje.srcgen.editor.EditorTreeItem;
import org.bridje.web.srcgen.models.ResourceModel;
import org.bridje.web.srcgen.models.UISuiteModel;
import org.bridje.web.srcgen.models.UISuitesModel;

public final class ResourceTreeItem extends EditorTreeItem
{
    private final ResourceModel resource;

    private final static ResourceEditor EDITOR = new ResourceEditor();

    public ResourceTreeItem(ResourceModel resource)
    {
        super(resource, UISuitesModel.resource(16));
        this.resource = resource;
        setContextMenu(createContextMenu());
        setToolBar(createToolBar());
        this.resource.nameProperty().addListener((observable, oldValue, newValue) ->
        {
            setValue(null);
            setValue(resource);
        });
    }

    private ContextMenu createContextMenu()
    {
        ContextMenu ctx = new ContextMenu();
        ctx.getItems().add(JfxUtils.createMenuItem("Save", UISuitesModel.save(24), this::saveModel));
        ctx.getItems().add(JfxUtils.createMenuItem("Delete", UISuitesModel.delete(24), this::deleteResource));
        return ctx;
    }

    private ToolBar createToolBar()
    {
        ToolBar tb = new ToolBar();
        tb.getItems().add(JfxUtils.createToolButton(UISuitesModel.save(32), this::saveModel));
        tb.getItems().add(JfxUtils.createToolButton(UISuitesModel.delete(32), this::deleteResource));
        return tb;
    }
    
    public void saveModel(ActionEvent event)
    {
        UISuiteModel suite = this.resource.getParent();
        ModelUtils.saveUISuite(suite);
    }

    private void deleteResource(ActionEvent event)
    {
        UISuiteModel suite = this.resource.getParent();
        suite.getResources().remove(resource);
    }

    @Override
    public Node edit()
    {
        UISuiteModel suite = this.resource.getParent();
        EDITOR.setUISuite(suite);
        EDITOR.setResource(resource);
        return EDITOR;
    }
}
