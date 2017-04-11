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
import org.bridje.jfx.utils.BiContentConverter;
import org.bridje.jfx.utils.ExBindings;
import org.bridje.jfx.utils.JfxUtils;
import org.bridje.srcgen.editor.EditorTreeItem;
import org.bridje.web.srcgen.models.ControlDefModel;
import org.bridje.web.srcgen.models.UISuiteModel;

public class UISuiteTreeItem extends EditorTreeItem
{
    private final static UISuiteEditor editor = new UISuiteEditor();

    private final UISuiteModel suite;
    
    public UISuiteTreeItem(UISuiteModel suite)
    {
        super(suite, loadImage("uisuite.png", 16));
        this.suite = suite;
        setContextMenu(createContextMenu());
        setToolBar(createToolBar());

        EditorTreeItem tiTemplates = new EditorTreeItem("Templates", loadImage("control.png", 16));
        BiContentConverter<TreeItem<Object>, ControlDefModel> ctrlConverter = createCtrlConverter();
        if(suite.getControlsTemplates() == null) suite.setControlsTemplates(FXCollections.observableArrayList());
        ExBindings.bindContentBidirectional(tiTemplates.getChildren(), suite.getControlsTemplates(), ctrlConverter);
        tiTemplates.setContextMenu(createTemplatesContextMenu());
        tiTemplates.setToolBar(createTemplatesToolBar());

        EditorTreeItem tiControls = new EditorTreeItem("Controls", loadImage("control.png", 16));
        if(suite.getControls() == null) suite.setControls(FXCollections.observableArrayList());        
        ExBindings.bindContentBidirectional(tiControls.getChildren(), suite.getControls(), ctrlConverter);
        tiControls.setContextMenu(createControlsContextMenu());
        tiControls.setToolBar(createControlsToolBar());

        getChildren().addAll(tiTemplates, tiControls);
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
        ControlTreeItem tiControl = new ControlTreeItem(control, suite);
        return tiControl;
    }

    private ContextMenu createTemplatesContextMenu()
    {
        ContextMenu ctx = new ContextMenu();
        ctx.getItems().add(JfxUtils.createMenuItem("Save", loadImage("save.png", 24), this::saveModel));
        ctx.getItems().add(JfxUtils.createMenuItem("Add Template", loadImage("add.png", 24), this::addTemplate));
        return ctx;
    }

    private ToolBar createTemplatesToolBar()
    {
        ToolBar tb = new ToolBar();
        tb.getItems().add(JfxUtils.createToolButton(loadImage("save.png", 32), this::saveModel));
        tb.getItems().add(JfxUtils.createToolButton(loadImage("add.png", 32), this::addTemplate));
        return tb;
    }

    private ContextMenu createControlsContextMenu()
    {
        ContextMenu ctx = new ContextMenu();
        ctx.getItems().add(JfxUtils.createMenuItem("Save", loadImage("save.png", 24), this::saveModel));
        ctx.getItems().add(JfxUtils.createMenuItem("Add Control", loadImage("add.png", 24), this::addControl));
        return ctx;
    }

    private ToolBar createControlsToolBar()
    {
        ToolBar tb = new ToolBar();
        tb.getItems().add(JfxUtils.createToolButton(loadImage("save.png", 32), this::saveModel));
        tb.getItems().add(JfxUtils.createToolButton(loadImage("add.png", 32), this::addControl));
        return tb;
    }

    private ContextMenu createContextMenu()
    {
        ContextMenu ctx = new ContextMenu();
        ctx.getItems().add(JfxUtils.createMenuItem("Save", loadImage("save.png", 24), this::saveModel));
        return ctx;
    }

    private ToolBar createToolBar()
    {
        ToolBar tb = new ToolBar();
        tb.getItems().add(JfxUtils.createToolButton(loadImage("save.png", 32), this::saveModel));
        return tb;
    }

    public void addTemplate(ActionEvent event)
    {
        ControlDefModel ctrl = new ControlDefModel();
        ctrl.setName("NewTemplate");
        suite.getControlsTemplates().add(ctrl);
    }

    public void addControl(ActionEvent event)
    {
        ControlDefModel ctrl = new ControlDefModel();
        ctrl.setName("NewControl");
        suite.getControls().add(ctrl);
    }

    public void saveModel(ActionEvent event)
    {
        ModelUtils.saveUISuite(suite);
    }

    private static Node loadImage(String image, int size)
    {
        return JfxUtils.loadImage(UISuiteTreeItem.class, image, size, size);
    }

    @Override
    public Node edit()
    {
        editor.setUISuite(suite);
        return editor;
    }
}
