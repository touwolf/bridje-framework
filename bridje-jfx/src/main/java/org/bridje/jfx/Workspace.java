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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.bridje.ioc.Ioc;
import org.bridje.ioc.IocContext;

/**
 *
 */
public class Workspace extends BorderPane
{
    private final WorkspaceLayout<WorkspaceLayout<WorkspaceArea>> mainLayout;
    
    private final Map<Node, Position> nodePositions;

    private final Map<Node, Position> nodeSubPosition;
    
    public Workspace()
    {
        mainLayout = new WorkspaceLayout();
        nodePositions = new HashMap<>();
        nodeSubPosition = new HashMap<>();
        setCenter(mainLayout);
    }

    public void addContext(IocContext context)
    {
        WorkspacePanel[] panels = context.findAll(WorkspacePanel.class);
        for (WorkspacePanel panel : panels)
        {
            DockOn dockOn = panel.getClass().getAnnotation(DockOn.class);
            if(dockOn != null)
            {
                addPanel(panel, dockOn.position(), dockOn.subPosition());
            }            
        }
    }
    
    public void addPanel(WorkspacePanel node, Position position, Position subpos)
    {
        if(node == null)
        {
            throw new IllegalArgumentException("node");
        }
        WorkspaceLayout<WorkspaceArea> container = mainLayout.get(position);
        if(container == null)
        {
            container = new WorkspaceLayout<>();
            mainLayout.set(position, container);
        }
        Node prev = container.get(subpos);
        if(prev != null)
        {
            nodePositions.remove(prev);
            nodeSubPosition.remove(prev);
        }
        WorkspaceArea area = container.get(subpos);
        if(area == null)
        {
            area = new WorkspaceArea();
            container.set(subpos, area);
        }
        area.add(node);
        nodePositions.put(node, position);
        nodeSubPosition.put(node, subpos);
    }
    
    public void removePanel(WorkspacePanel node)
    {
        Position position = nodePositions.get(node);
        Position subpos = nodeSubPosition.get(node);
        if(position == null || subpos == null)
        {
            return;
        }
        WorkspaceLayout<WorkspaceArea> container = mainLayout.get(position);
        if(container != null)
        {
            WorkspaceArea area = container.get(subpos);
            if(area != null)
            {
                area.remove(node);
                if(area.isEmpty())
                {
                    container.set(subpos, null);
                }
            }
            if(container.isEmpty())
            {
                mainLayout.set(position, null);
            }
        }
    }
}
