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
import org.bridje.web.srcgen.models.ControlDefModel;
import org.bridje.web.srcgen.models.UISuiteModel;
import org.bridje.web.srcgen.models.UISuitesModel;

/**
 * A Tree Item for the ControlDefModel class.
 */
public final class ControlTreeItem extends EditorTreeItem
{
    private final ControlDefModel control;

    private final static ControlEditor EDITOR = new ControlEditor();

    /**
     * Constructor that receives the control.
     * 
     * @param control The control for this Tree Item.
     */
    public ControlTreeItem(ControlDefModel control)
    {
        super(control, UISuitesModel.control(16));
        this.control = control;
        setContextMenu(createContextMenu());
        setToolBar(createToolBar());
        this.control.nameProperty().addListener((observable, oldValue, newValue) ->
        {
            setValue(null);
            setValue(control);
        });
    }

    @Override
    public Node edit()
    {
        EDITOR.setControl(control);
        return EDITOR;
    }
    
    private ContextMenu createContextMenu()
    {
        ContextMenu ctx = new ContextMenu();
        ctx.getItems().add(JfxUtils.createMenuItem("Save", UISuitesModel.save(24), this::saveModel));
        ctx.getItems().add(JfxUtils.createMenuItem("Delete", UISuitesModel.delete(24), this::deleteControl));
        return ctx;
    }

    private ToolBar createToolBar()
    {
        ToolBar tb = new ToolBar();
        tb.getItems().add(JfxUtils.createToolButton(UISuitesModel.save(32), this::saveModel));
        tb.getItems().add(JfxUtils.createToolButton(UISuitesModel.delete(32), this::deleteControl));
        return tb;
    }

    private void saveModel(ActionEvent event)
    {
        ModelUtils.saveUISuite(control.getParent());
    }

    private void deleteControl(ActionEvent event)
    {
        UISuiteModel suite = control.getParent();
        if(suite.getControls().contains(control))
        {
            suite.getControls().remove(control);
        }
        else 
        {
            suite.getControlsTemplates().remove(control);
        }
    }
}
