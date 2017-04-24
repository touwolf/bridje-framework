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

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import org.bridje.jfx.binding.BiContentConverter;
import org.bridje.jfx.binding.ExBindings;
import org.bridje.jfx.utils.JfxUtils;
import org.bridje.srcgen.editor.EditorTreeItem;
import org.bridje.web.srcgen.models.ResourceModel;
import org.bridje.web.srcgen.models.UISuiteModel;
import org.bridje.web.srcgen.models.UISuitesModel;

public final class ResourcesTreeItem extends EditorTreeItem
{
    private final UISuiteModel suite;

    public ResourcesTreeItem(UISuiteModel suite)
    {
        super("Resources", UISuitesModel.resource(16));
        this.suite = suite;
        BiContentConverter<TreeItem<Object>, ResourceModel> resConverter = createResConverter();
        if(suite.getResources() == null) suite.setResources(FXCollections.observableArrayList());
        ExBindings.bindContentBidirectional(getChildren(), suite.getResources(), resConverter);
        setContextMenu(createResourcesContextMenu());
        setToolBar(createResourcesToolBar());
    }

    private ContextMenu createResourcesContextMenu()
    {
        ContextMenu ctx = new ContextMenu();
        ctx.getItems().add(JfxUtils.createMenuItem("Save", UISuitesModel.save(24), this::saveModel));
        ctx.getItems().add(JfxUtils.createMenuItem("Add Resource", UISuitesModel.add(24), this::addResource));
        return ctx;
    }

    private ToolBar createResourcesToolBar()
    {
        ToolBar tb = new ToolBar();
        tb.getItems().add(JfxUtils.createToolButton(UISuitesModel.save(32), this::saveModel));
        tb.getItems().add(JfxUtils.createToolButton(UISuitesModel.add(32), this::addResource));
        return tb;
    }

    public void addResource(ActionEvent event)
    {
        ResourceModel ctrl = new ResourceModel();
        ctrl.setName("newResource" + suite.getResources().size());
        ctrl.setParent(suite);
        ctrl.setContent(FXCollections.observableArrayList());
        suite.getResources().add(ctrl);
    }

    private BiContentConverter<TreeItem<Object>, ResourceModel> createResConverter()
    {
        return new BiContentConverter<TreeItem<Object>, ResourceModel>()
        {
            @Override
            public ResourceModel convertFrom(TreeItem<Object> value)
            {
                return (ResourceModel)value.getValue();
            }

            @Override
            public TreeItem<Object> convertTo(ResourceModel value)
            {
                return toTreeItem(value);
            }
        };
    }

    private ResourceTreeItem toTreeItem(ResourceModel resource)
    {
        ResourceTreeItem tiResource = new ResourceTreeItem(resource);
        return tiResource;
    }

    public void saveModel(ActionEvent event)
    {
        ModelUtils.saveUISuite(suite);
    }
}
