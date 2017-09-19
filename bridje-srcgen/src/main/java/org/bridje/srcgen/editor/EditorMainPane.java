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

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.bridje.ioc.Ioc;
import org.bridje.srcgen.SourceGenerator;
import org.bridje.srcgen.SrcGenService;
import org.bridje.vfs.VFile;

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
        spCenter.widthProperty()
                .addListener((observable, oldValue, ewValue) -> spCenter.setDividerPositions(0.3d));
        setCenter(spCenter);
        setTop(tbEmpty);

        VFile dataFolder = new VFile(SrcGenService.DATA_PATH);
        VFile[] files = dataFolder.listFiles();
        for (VFile child : files)
        {
            if(child.isDirectory()) 
            {
                root.getChildren().add(new FolderTreeItem(child));
            }
        }
        for (VFile child : files)
        {
            if(child.isFile())
            {
                SourceGenerator[] sources = Ioc.context().findAll(SourceGenerator.class);
                for (SourceGenerator source : sources)
                {
                    TreeItem<Object> item = source.createTreeItem(child);
                    if(item != null)
                    {
                        root.getChildren().add(item);
                        break;
                    }
                }
            }
        }

        tvMain.getSelectionModel()
                .selectedItemProperty()
                .addListener(this::changed);
    }

    void changed(ObservableValue<? extends TreeItem<Object>> observable, TreeItem<Object> oldValue, TreeItem<Object> newValue)
    {
        stpContent.getChildren().clear();
        if(newValue != null && newValue instanceof EditorTreeItem)
        {
            EditorTreeItem ti = (EditorTreeItem)newValue;
            tvMain.setContextMenu(ti.getContextMenu());
            setTop(ti.getToolBar());
            Node editor = ti.edit();
            if(editor != null) stpContent.getChildren().add(editor);
        }
        else
        {
            tvMain.setContextMenu(null);
            setTop(tbEmpty);
        }
    }
}
