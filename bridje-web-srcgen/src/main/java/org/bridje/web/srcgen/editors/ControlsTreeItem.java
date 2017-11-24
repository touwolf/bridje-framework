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
import org.bridje.web.srcgen.models.ControlDefModel;
import org.bridje.web.srcgen.models.UISuiteModel;
import org.bridje.web.srcgen.models.UISuitesModel;

/**
 * A Tree Item for a list of ControlDefModel objects.
 */
public final class ControlsTreeItem extends EditorTreeItem
{
    private final UISuiteModel suite;

    public ControlsTreeItem(UISuiteModel suite)
    {
        super("Controls", UISuitesModel.control(16));
        this.suite = suite;
        BiContentConverter<TreeItem<Object>, ControlDefModel> ctrlConverter = createCtrlConverter();
        if(suite.getControls() == null) suite.setControls(FXCollections.observableArrayList());        
        ExBindings.bindContentBidirectional(getChildren(), this.suite.getControls(), ctrlConverter);
        setContextMenu(createControlsContextMenu());
        setToolBar(createControlsToolBar());
    }

    private ContextMenu createControlsContextMenu()
    {
        ContextMenu ctx = new ContextMenu();
        ctx.getItems().add(JfxUtils.createMenuItem("Save", UISuitesModel.save(24), this::saveModel));
        ctx.getItems().add(JfxUtils.createMenuItem("Add Control", UISuitesModel.addControl(24), this::addControl));
        return ctx;
    }

    private ToolBar createControlsToolBar()
    {
        ToolBar tb = new ToolBar();
        tb.getItems().add(JfxUtils.createToolButton(UISuitesModel.save(32), this::saveModel));
        tb.getItems().add(JfxUtils.createToolButton(UISuitesModel.addControl(32), this::addControl));
        return tb;
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
                return new ControlTreeItem(value);
            }
        };
    }

    public void addControl(ActionEvent event)
    {
        ControlDefModel ctrl = new ControlDefModel();
        ctrl.setParent(suite);
        ctrl.setName("NewControl" + suite.getControls().size());
        suite.getControls().add(ctrl);
    }

    public void saveModel(ActionEvent event)
    {
        ModelUtils.saveUISuite(suite);
    }
}
