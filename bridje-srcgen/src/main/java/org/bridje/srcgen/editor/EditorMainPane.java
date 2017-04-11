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

package org.bridje.srcgen.editor;

import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.bridje.ioc.Ioc;
import org.bridje.srcgen.SourceGenerator;

public class EditorMainPane extends BorderPane
{
    private final TreeView<Object> tvMain;
    
    private final SplitPane spCenter;
    
    private final StackPane stpContent;
    
    private final ToolBar tbEmpty;

    public EditorMainPane()
    {
        tbEmpty = new ToolBar();
        tbEmpty.setMinHeight(50);
        tvMain = new TreeView<>();
        tvMain.setShowRoot(false);
        TreeItem<Object> root = new TreeItem<>();
        tvMain.setRoot(root);
        stpContent = new StackPane();
        spCenter = new SplitPane(tvMain, stpContent);
        spCenter.setDividerPositions(0.3d);
        setCenter(spCenter);
        setTop(tbEmpty);

        SourceGenerator[] sources = Ioc.context().findAll(SourceGenerator.class);
        for (SourceGenerator source : sources)
        {
            TreeItem<Object> item = source.createEditorTreeItem();
            if(item != null)
            {
                root.getChildren().add(item);
            }
        }
        tvMain.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->
                {
                    if(newValue != null && newValue instanceof EditorTreeItem)
                    {
                        tvMain.setContextMenu(((EditorTreeItem)newValue).getContextMenu());
                        setTop(((EditorTreeItem)newValue).getToolBar());
                    }
                    else
                    {
                        tvMain.setContextMenu(null);
                        setTop(tbEmpty);
                    }
                });
    }

}
