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
import org.bridje.web.srcgen.models.StandaloneDefModel;
import org.bridje.web.srcgen.models.UISuitesModel;

public final class StandaloneTreeItem extends EditorTreeItem
{
    private final StandaloneDefModel standalone;

    private final static StandaloneEditor editor = new StandaloneEditor();

    public StandaloneTreeItem(StandaloneDefModel standalone)
    {
        super(standalone, UISuitesModel.control(16));
        this.standalone = standalone;
        setContextMenu(createContextMenu());
        setToolBar(createToolBar());
        this.standalone.nameProperty().addListener((observable, oldValue, newValue) ->
        {
            setValue(null);
            setValue(standalone);
        });
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
        ModelUtils.saveUISuite(this.standalone.getParent());
    }

    @Override
    public Node edit()
    {
        editor.setStandalone(standalone);
        return editor;
    }
}
