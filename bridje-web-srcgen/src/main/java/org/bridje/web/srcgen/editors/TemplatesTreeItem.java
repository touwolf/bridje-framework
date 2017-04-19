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
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import org.bridje.jfx.binding.BiContentConverter;
import org.bridje.jfx.binding.ExBindings;
import org.bridje.jfx.utils.JfxUtils;
import org.bridje.srcgen.editor.EditorTreeItem;
import org.bridje.web.srcgen.models.ControlDefModel;
import org.bridje.web.srcgen.models.ResourceModel;
import org.bridje.web.srcgen.models.UISuiteModel;
import org.bridje.web.srcgen.models.UISuitesModel;

public final class TemplatesTreeItem extends EditorTreeItem
{
    private final UISuiteModel suite;

    public TemplatesTreeItem(UISuiteModel suite)
    {
        super("Templates", UISuitesModel.control(16));
        this.suite = suite;
        BiContentConverter<TreeItem<Object>, ControlDefModel> ctrlConverter = createCtrlConverter();
        if(suite.getControlsTemplates() == null) suite.setControlsTemplates(FXCollections.observableArrayList());
        ExBindings.bindContentBidirectional(getChildren(), suite.getControlsTemplates(), ctrlConverter);
        setContextMenu(createTemplatesContextMenu());
        setToolBar(createTemplatesToolBar());
    }

    private BiContentConverter<TreeItem<Object>, ControlDefModel> createCtrlConverter()
    {
        return new BiContentConverter<TreeItem<Object>, ControlDefModel>()
        {
            @Override
            public ControlDefModel convertFrom(TreeItem<Object> value)
            {
                return (ControlDefModel)value.getValue();
            }

            @Override
            public TreeItem<Object> convertTo(ControlDefModel value)
            {
                return toTreeItem(value);
            }
        };
    }

    private ControlTreeItem toTreeItem(ControlDefModel control)
    {
        ControlTreeItem tiControl = new ControlTreeItem(control);
        return tiControl;
    }

    public void addTemplate(ActionEvent event)
    {
        ControlDefModel ctrl = new ControlDefModel();
        ctrl.setName("NewTemplate" + suite.getControlsTemplates().size());
        suite.getControlsTemplates().add(ctrl);
    }

    private ContextMenu createTemplatesContextMenu()
    {
        ContextMenu ctx = new ContextMenu();
        ctx.getItems().add(JfxUtils.createMenuItem("Save", UISuitesModel.save(24), this::saveModel));
        ctx.getItems().add(JfxUtils.createMenuItem("Add Template", UISuitesModel.addControl(24), this::addTemplate));
        return ctx;
    }

    private ToolBar createTemplatesToolBar()
    {
        ToolBar tb = new ToolBar();
        tb.getItems().add(JfxUtils.createToolButton(UISuitesModel.save(32), this::saveModel));
        tb.getItems().add(JfxUtils.createToolButton(UISuitesModel.addControl(32), this::addTemplate));
        return tb;
    }

    public void saveModel(ActionEvent event)
    {
        ModelUtils.saveUISuite(suite);
    }
}
