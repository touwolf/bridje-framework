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

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 *
 */
class TopWindow extends Tab
{
    private TopWindowButton tabBottom;

    private TopWindowsTabs container;
    
    public TopWindow(TopWindowsTabs container, String title, Node node)
    {
        super(title, node);
        this.container = container;
        tabBottom = new TopWindowButton(this, container.getTabsBar(), title);
    }
    
    public TopWindow(TopWindowsTabs container, String title, Node node, Image image)
    {
        this(container, title, node);
        if(image != null)
        {
            ImageView imgView = new ImageView(image);
            imgView.setFitHeight(18);
            imgView.setFitWidth(18);
            setGraphic(imgView);
        }
    }
    
    public TopWindowButton getTabBottom()
    {
        return tabBottom;
    }

    protected void open()
    {
        if(!container.getTabs().contains(this))
        {
            container.getTabs().add(this);
        }
    }

    protected void close()
    {
        if(container.getTabs().contains(this))
        {
            container.getTabs().remove(this);
        }
    }
    
    protected void remove()
    {
        container.removePane((Pane)getContent());
    }
}
