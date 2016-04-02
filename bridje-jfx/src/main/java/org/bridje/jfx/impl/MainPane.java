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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javax.annotation.PostConstruct;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.jfx.PanePosition;

/**
 *
 */
@Component
class MainPane extends BorderPane
{
    @Inject
    private MainMenu mainMenu;
    
    private PaneContainer left;
    
    private PaneContainer center;
    
    private PaneContainer right;
    
    private PaneContainer bottom;
    
    private SideToolBar rightPanes;
    
    private SideToolBar bottomPanes;
    
    private SideToolBar leftPanes;
    
    @PostConstruct
    private void init()
    {
        setTop(mainMenu);
        createSideToolBars();
        createPaneContainers();
    }

    protected void addPane(String title, Pane pane, PanePosition position)
    {
        switch(position)
        {
            case BOTTOM:
                bottom.addPane(title, pane);
                break;
            case RIGHT:
                right.addPane(title, pane);
                break;
            case LEFT:
                left.addPane(title, pane);
                break;
            case CENTER:
                center.addPane(title, pane);
                break;
        }
        createLayout1();
    }

    private void createPaneContainers()
    {
        left = new PaneContainer(leftPanes);
        center = new PaneContainer();
        right = new PaneContainer(rightPanes);
        bottom = new PaneContainer(bottomPanes);

        createLayout1();
    }

    private void createSideToolBars()
    {
        rightPanes = new SideToolBar(Orientation.VERTICAL);
        bottomPanes = new SideToolBar(Orientation.HORIZONTAL);
        leftPanes = new SideToolBar(Orientation.VERTICAL);

        setLeft(leftPanes);
        setRight(rightPanes);
        setBottom(bottomPanes);
    }

    private void createLayout1()
    {
        Node first;
        if(bottom.getTabs().isEmpty())
        {
            first = center;
        }
        else
        {
            SplitPane spVert = new SplitPane(center, bottom);
            spVert.setOrientation(Orientation.VERTICAL);
            first = spVert;
        }
        
        Node second;
        if(left.getTabs().isEmpty() && right.getTabs().isEmpty())
        {
            second = first;
        }
        else 
        {
            SplitPane spHori = new SplitPane();
            spHori.setOrientation(Orientation.HORIZONTAL);
            if(!left.getTabs().isEmpty())
            {
                spHori.getItems().add(left);
            }

            spHori.getItems().add(first);

            if(!right.getTabs().isEmpty())
            {
                spHori.getItems().add(right);
            }
            second = spHori;
        }
        
        setCenter(second);
    }

    private void createLayout2()
    {
        Node first;
        if(left.getTabs().isEmpty() && right.getTabs().isEmpty())
        {
            first = center;
        }
        else 
        {
            SplitPane spHori = new SplitPane();
            spHori.setOrientation(Orientation.HORIZONTAL);
            if(!left.getTabs().isEmpty())
            {
                spHori.getItems().add(left);
            }

            spHori.getItems().add(center);

            if(!right.getTabs().isEmpty())
            {
                spHori.getItems().add(right);

            }
            first = spHori;
        }

        Node second;
        if(bottom.getTabs().isEmpty())
        {
            second = first;
        }
        else
        {
            SplitPane spVert = new SplitPane(first, bottom);
            spVert.setOrientation(Orientation.VERTICAL);
            second = spVert;
        }
        
        setCenter(second);
    }

}
