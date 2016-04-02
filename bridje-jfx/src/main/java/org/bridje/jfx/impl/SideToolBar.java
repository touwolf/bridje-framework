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

package org.bridje.jfx.impl;

import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

/**
 *
 */
public class SideToolBar extends ToolBar
{
    public SideToolBar(Orientation orientation)
    {
        setOrientation(orientation);
    }

    public void addButton(Button button)
    {
        if(this.getOrientation() == Orientation.VERTICAL)
        {
            Group group = new Group(button);
            button.setRotate(-90);
            getItems().add(group);
        }
        else
        {
            getItems().add(button);
        }
    }

    public void removeButton(Button b)
    {
        if(this.getOrientation() == Orientation.VERTICAL)
        {
            Node toRemove = null;
            for (Node node : getItems())
            {
                if( ((Group)node).getChildren().get(0).equals(b) )
                {
                    toRemove = node;
                }
            }
            getItems().remove(toRemove);
        }
        else
        {
            getItems().remove(b);
        }
    }
}
