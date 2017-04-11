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

public class ControlTreeItem extends EditorTreeItem
{
    private final ControlDefModel control;

    private final UISuiteModel suite;
    
    private final static ControlEditor editor = new ControlEditor();

    public ControlTreeItem(ControlDefModel control, UISuiteModel suite)
    {
        super(control, loadImage("control.png", 16));
        this.control = control;
        this.suite = suite;
        setContextMenu(createContextMenu());
        setToolBar(createToolBar());
    }

    private ContextMenu createContextMenu()
    {
        ContextMenu ctx = new ContextMenu();
        ctx.getItems().add(JfxUtils.createMenuItem("Save", loadImage("save.png", 24), this::saveModel));
        ctx.getItems().add(JfxUtils.createMenuItem("Delete", loadImage("delete.png", 24), this::deleteControl));
        return ctx;
    }

    private ToolBar createToolBar()
    {
        ToolBar tb = new ToolBar();
        tb.getItems().add(JfxUtils.createToolButton(loadImage("save.png", 32), this::saveModel));
        tb.getItems().add(JfxUtils.createToolButton(loadImage("delete.png", 32), this::deleteControl));
        return tb;
    }
    
    public void saveModel(ActionEvent event)
    {
        ModelUtils.saveUISuite(suite);
    }

    public void deleteControl(ActionEvent event)
    {
        if(suite.getControls().contains(control))
        {
            suite.getControls().remove(control);
        }
        else 
        {
            suite.getControlsTemplates().remove(control);
        }
    }

    private static Node loadImage(String image, int size)
    {
        return JfxUtils.loadImage(ControlTreeItem.class, image, size, size);
    }

    @Override
    public Node edit()
    {
        if(editor.getUISuite() == null || !editor.getUISuite().equals(suite)) editor.setUISuite(suite);
        editor.setControl(control);
        return editor;
    }
}
