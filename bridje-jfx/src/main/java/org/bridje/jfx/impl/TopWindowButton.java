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

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

/**
 *
 */
public class TopWindowButton extends Group
{
    private Button button;
    
    private TopWindowsBar container;
    
    private TopWindow window;

    public TopWindowButton(TopWindow window, TopWindowsBar container, String title)
    {
        this.container = container;
        this.window = window;
        button = new Button(title);
        if(container.getOrientation() == Orientation.VERTICAL)
        {
            button.setRotate(-90d);
        }
        getChildren().add(button);
        window.setOnClosed((Event t) ->
        {
            if(!container.containsButton(this))
            {
                container.addButton(this);
            }
        });
        button.setOnAction((ActionEvent t) ->
        {
            window.open();
            if(container.containsButton(this))
            {
                container.removeButton(this);
            }
        });
        MenuItem close = new MenuItem("Close");
        close.setOnAction((ActionEvent t) ->
        {
            button.setOnAction(null);
            window.setOnClosed(null);
            window.close();
            if(container.containsButton(this))
            {
                container.removeButton(this);
            }
        });
        button.setContextMenu(new ContextMenu(close));
        
    }
}
