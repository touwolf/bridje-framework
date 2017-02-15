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

package org.bridje.srcgen.impl.edit;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.bridje.ioc.Ioc;
import org.bridje.srcgen.SourceGenerator;

public class SrcEditorMainPane extends SplitPane
{
    private final TreeView<Object> treeView;
    
    private final Pane rightPane;
    
    public SrcEditorMainPane()
    {
        treeView = createTreeView();
        rightPane = createRightPane();
        getItems().add(treeView);
        getItems().add(rightPane);
        setOrientation(Orientation.HORIZONTAL);
        setResizableWithParent(treeView, false);
        setDividerPositions(0.3f);
    }

    private TreeView<Object> createTreeView()
    {
        TreeView result = new TreeView<>();
        SourceGenerator[] srcGenerator = Ioc.context().findAll(SourceGenerator.class);
        TreeItem root = new TreeItem();
        
        for (SourceGenerator sourceGenerator : srcGenerator)
        {
            root.getChildren().add(sourceGenerator.createTreeNode());
        }
        root.setExpanded(true);
        result.setShowRoot(false);
        result.setRoot(root);
        result.getSelectionModel().selectedItemProperty()
                .addListener((ObservableValue observable, Object oldValue, Object newValue) ->
                {
                    rightPane.getChildren().clear();
                    if(oldValue instanceof EditableItem)
                    {
                        ((EditableItem)oldValue).commit();
                    }
                    if(newValue instanceof EditableItem)
                    {
                        Pane editor = ((EditableItem)newValue).getEditor();
                        if(editor != null)
                        {
                            rightPane.getChildren().add(editor);
                            ((EditableItem)newValue).startEdit();
                        }
                    }
                });
        result.setMinWidth(200);
        return result;
    }

    private Pane createRightPane()
    {
        StackPane pane = new StackPane();
        return pane;
    }
}
