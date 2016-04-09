/*
 * Copyright 2016 Bridje Framework.
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

package org.bridje.jfx;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

final class WorkspaceArea extends AnchorPane
{
    private Node content;

    public WorkspaceArea()
    {
    }

    public WorkspaceArea(WorkspacePanel content)
    {
        add(content);
    }
    
    public void add(WorkspacePanel node)
    {
        if(node == null)
        {
            throw new IllegalArgumentException("node");
        }
        if(content == null)
        {
            content = node;
            getChildren().add(node);
            fixAnchors();
            return;
        }

        if(! (content instanceof TabPane) )
        {
            getChildren().remove(content);
            Tab tab = new Tab( ((WorkspacePanel)content).getTitle(), content);
            TabPane tp = new TabPane(tab);
            ((WorkspacePanel)content).setParentTab(tab);
            content = tp;
            getChildren().add(content);
            fixAnchors();
        }

        TabPane tp = (TabPane)content;
        Tab tab = new Tab();
        tab.setText(node.getTitle());
        tab.setContent((Node)node);
        node.setParentTab(tab);
        tp.getTabs().add(tab);
    }

    public void remove(WorkspacePanel node)
    {
        if(node == null)
        {
            throw new IllegalArgumentException("node");
        }
        if(content == null)
        {
            return;
        }

        if(content instanceof TabPane)
        {
            TabPane tp = (TabPane)content;
            tp.getTabs().remove(node.getParentTab());
            node.setParentTab(null);
            if(tp.getTabs().size() == 1)
            {
                getChildren().remove(content);
                content = tp.getTabs().get(0).getContent();
                ((WorkspacePanel)content).setParentTab(null);
                getChildren().add(content);
                fixAnchors();
            }
        }

        if(content.equals(node))
        {
            getChildren().remove(content);
            content = null;
        }
    }

    private void fixAnchors()
    {
        setTopAnchor(content, 0d);
        setLeftAnchor(content, 0d);
        setBottomAnchor(content, 0d);
        setRightAnchor(content, 0d);
    }
    
    public boolean isEmpty()
    {
        return getChildren().isEmpty();
    }
}
