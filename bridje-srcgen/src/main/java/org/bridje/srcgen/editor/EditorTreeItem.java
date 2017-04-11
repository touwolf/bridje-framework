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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;

public class EditorTreeItem extends TreeItem<Object>
{
    private final ObjectProperty<ContextMenu> contextMenuProperty = new SimpleObjectProperty<>();

    private final ObjectProperty<ToolBar> toolBarProperty = new SimpleObjectProperty<>();

    public EditorTreeItem(Object value, Node graphic)
    {
        super(value, graphic);
    }

    public ObjectProperty<ContextMenu> contextMenuProperty()
    {
        return contextMenuProperty;
    }

    public ContextMenu getContextMenu()
    {
        return contextMenuProperty.get();
    }

    public void setContextMenu(ContextMenu value)
    {
        contextMenuProperty.set(value);
    }

    public ObjectProperty<ToolBar> toolBarProperty()
    {
        return toolBarProperty;
    }

    public ToolBar getToolBar()
    {
        return toolBarProperty.get();
    }

    public void setToolBar(ToolBar value)
    {
        toolBarProperty.set(value);
    }
}
