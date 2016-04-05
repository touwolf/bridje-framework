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

import java.util.function.Consumer;
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
class LayoutPane extends BorderPane
{
    @Inject
    private TopMenuBar mainMenu;
    
    private TopWindowsTabs left;
    
    private TopWindowsTabs center;
    
    private TopWindowsTabs right;
    
    private TopWindowsTabs bottom;
    
    private TopWindowsBar rightPanes;

    private TopWindowsBar bottomPanes;

    private TopWindowsBar leftPanes;

    private SplitPane spVert;

    private SplitPane spHori;

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
                if(!spVert.getItems().contains(bottom))
                {
                    spVert.getItems().add(bottom);
                }
                spVert.setDividerPosition(0, 0.7d);
                break;
            case RIGHT:
                right.addPane(title, pane);
                if(!spHori.getItems().contains(right))
                {
                    spHori.getItems().add(right);
                }
                spHori.setDividerPosition(spHori.getDividers().size()-1, 0.8d);
                break;
            case LEFT:
                left.addPane(title, pane);
                if(!spHori.getItems().contains(left))
                {
                    spHori.getItems().add(0, left);
                }
                spHori.setDividerPosition(0, 0.2d);
                break;
            case CENTER:
                center.addPane(title, pane);
                break;
        }
    }

    private void createPaneContainers()
    {
        left = new TopWindowsTabs(this, leftPanes);
        center = new TopWindowsTabs(this);
        right = new TopWindowsTabs(this, rightPanes);
        bottom = new TopWindowsTabs(this, bottomPanes);

        spVert = new SplitPane(center);
        spVert.setOrientation(Orientation.VERTICAL);

        spHori = new SplitPane(spVert);
        spHori.setOrientation(Orientation.HORIZONTAL);

        setCenter(spHori);
    }

    private void createSideToolBars()
    {
        rightPanes = new TopWindowsBar(Orientation.VERTICAL);
        bottomPanes = new TopWindowsBar(Orientation.HORIZONTAL);
        leftPanes = new TopWindowsBar(Orientation.VERTICAL);

        setLeft(leftPanes);
        setRight(rightPanes);
        setBottom(bottomPanes);
    }
    
    protected void removeFromLayout(TopWindowsTabs tabs)
    {
        if(tabs != center)
        {
            if(spVert.getItems().contains(tabs))
            {
                spVert.getItems().remove(tabs);
            }
            if(spHori.getItems().contains(tabs))
            {
                spHori.getItems().remove(tabs);
            }
        }
    }
}
