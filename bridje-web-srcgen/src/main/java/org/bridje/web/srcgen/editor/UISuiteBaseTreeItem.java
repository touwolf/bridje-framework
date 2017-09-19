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

package org.bridje.web.srcgen.editor;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import org.bridje.ioc.Ioc;
import org.bridje.jfx.utils.JfxUtils;
import org.bridje.srcgen.editor.EditorTreeItem;

public final class UISuiteBaseTreeItem extends EditorTreeItem
{
    private final UISuiteBaseModel suite;

    public UISuiteBaseTreeItem(UISuiteBaseModel suite, ImageView img)
    {
        super(suite.getFile().getName(), img);
        this.suite = suite;
        setContextMenu(createContextMenu());
        setToolBar(createToolBar());
        getChildren().addAll(
                new IncludesTreeItem(suite),
                new FtlElementsTreeItem("Macros", suite.getFtlMacros(), UISuitesModel.ftlMacro(16)),
                new FtlElementsTreeItem("Functions", suite.getFtlFunctions(), UISuitesModel.ftlFunction(16)));
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
        if(suite instanceof UISuiteModel)
        {
            ModelUtils.save((UISuiteModel)suite);
        }

        if(suite instanceof PartialUISuiteModel)
        {
            ModelUtils.save((PartialUISuiteModel)suite);
        }
    }

    @Override
    public Node edit()
    {
        return Ioc.context().find(UISuiteEditor.class);
    }
}
