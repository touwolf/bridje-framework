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

import java.util.HashMap;
import java.util.Map;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 *
 */
class TopWindowsTabs extends TabPane
{
    private TopWindowsBar tabsBar;
    
    private Map<Pane, TopWindow> windows = new HashMap<>();

    private LayoutPane layout;
    
    public TopWindowsTabs(LayoutPane layout)
    {
        this.layout = layout;
    }
    
    public TopWindowsTabs(LayoutPane layout, TopWindowsBar tabsBar)
    {
        this(layout);
        this.tabsBar = tabsBar;
    }

    public TopWindowsBar getTabsBar()
    {
        return tabsBar;
    }
    
    public void addPane(String title, Pane pane)
    {
        addPane(title, null, pane);
    }
    
    public void removePane(Pane pane)
    {
        if(!this.windows.containsKey(pane))
        {
            return; 
        }
        Tab toRemove = windows.get(pane);
        windows.remove(pane);
        getTabs().remove(toRemove);
        if(windows.isEmpty())
        {
            layout.removeFromLayout(this);
        }
    }
    
    public void addPane(String title, Image img, Pane pane)
    {
        if(this.windows.containsKey(pane))
        {
            return; 
        }
        TopWindow pWindow = new TopWindow(this, title, pane, img);
        windows.put(pane, pWindow);
        getTabs().add(pWindow);
    }
}
