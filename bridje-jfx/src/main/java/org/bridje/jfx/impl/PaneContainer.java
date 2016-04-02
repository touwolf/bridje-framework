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
import java.util.List;
import java.util.Map;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 *
 */
public class PaneContainer extends TabPane
{
    private SideToolBar sideToolBar;
    
    private Map<Pane, Button> paneToButtons = new HashMap<>();
    
    private Map<Pane, Tab> paneToTabs = new HashMap<>();

    public PaneContainer()
    {
    }
    
    public PaneContainer(SideToolBar sideToolBar)
    {
        this.sideToolBar = sideToolBar;
    }

    public void addPane(String title, Pane pane)
    {
        addPane(title, null, pane);
    }
    
    public void removePane(Pane pane)
    {
        Tab toRemove = paneToTabs.get(pane);
        if(toRemove != null)
        {
            getTabs().remove(toRemove);
        }
        paneToTabs.remove(pane);
        paneToButtons.remove(pane);
    }
    
    public void addPane(String title, Image img, Pane pane)
    {
        if(paneToTabs.containsKey(pane))
        {
            return; 
        }
        Tab tab = new Tab(title, pane);
        if(img != null)
        {
            ImageView imgView = new ImageView();
            imgView.setFitHeight(18);
            imgView.setFitWidth(18);
            tab.setGraphic(imgView);
        }
        getTabs().add(tab);
        paneToTabs.put(pane, tab);
        getTabs().addListener((ListChangeListener.Change<? extends Tab> change) ->
        {
            while(change.next())
            {
                change.getRemoved().stream()
                        .forEach( (t) -> addPaneButton(title, (Pane)t.getContent()) );
                change.getAddedSubList().stream()
                        .forEach( (t) -> removePaneButton((Pane)t.getContent()) );
            }
        });
    }

    private void addPaneButton(String title, Pane pane)
    {
        if(sideToolBar != null)
        {
            Button b = new Button(title);
            if(!paneToButtons.containsKey(pane))
            {
                paneToButtons.put(pane, b);
            }
            b.setOnAction((ActionEvent t) ->
            {
                getTabs().add(new Tab(title, pane));
                sideToolBar.removeButton(b);
            });
            sideToolBar.addButton(b);
        }
    }
    

    private void removePaneButton(Pane pane)
    {
        if(sideToolBar != null)
        {
            if(paneToButtons.containsKey(pane))
            {
                Button button = paneToButtons.get(pane);
                sideToolBar.removeButton(button);
            }
        }
    }
}
