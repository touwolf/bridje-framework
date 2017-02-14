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

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
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
        setDividerPositions(0.3f);
    }

    private TreeView<Object> createTreeView()
    {
        TreeView result = new TreeView<>();
        SourceGenerator[] srcGenerator = Ioc.context().findAll(SourceGenerator.class);
        TreeItem root = new TreeItem();
        
        for (SourceGenerator sourceGenerator : srcGenerator)
        {
            SrcGenerationNode srcGenNode = new SrcGenerationNode(sourceGenerator);
            root.getChildren().add(srcGenNode);
        }
        root.setExpanded(true);
        result.setShowRoot(false);
        result.setRoot(root);
        return result;
    }

    private Pane createRightPane()
    {
        Pane pane = new Pane();
        return pane;
    }
}
