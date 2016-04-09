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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javax.annotation.PostConstruct;
import org.bridje.ioc.ClassRepository;
import org.bridje.ioc.Inject;
import org.bridje.ioc.IocContext;

/**
 * Extending from this class will allow you to create a dockable workspace for
 * the all the {@link WorkspacePanel} in the current
 * {@link org.bridje.ioc.IocContext}. This component will automaticalle add all
 * of the {@link WorkspacePanel}, {@link ToolBarAction} and {@link MenuAction}
 * to this workspace. This must class must be extended from an IoC component.
 */
public class Workspace extends BorderPane
{
    private final WorkspaceLayout<WorkspaceLayout<WorkspaceArea>> mainLayout;

    private final Map<Node, Position> nodePositions;

    private final Map<Node, Position> nodeSubPosition;

    private VBox topBox;

    private WorkspaceMenuBar topMenu;

    private ToolBar topTools;

    @Inject
    private IocContext context;

    public Workspace()
    {
        mainLayout = new WorkspaceLayout();
        nodePositions = new HashMap<>();
        nodeSubPosition = new HashMap<>();
        setCenter(mainLayout);
    }

    @PostConstruct
    private void init()
    {
        if (context == null)
        {
            return;
        }
        ClassRepository clsRepo = context.getClassRepository();
        clsRepo.forEachClass(DockOn.class, WorkspacePanel.class,
                (comp, dockOn)
                -> 
                {
                    WorkspacePanel wsPanel = context.find(comp);
                    addPanel(wsPanel, dockOn.position(), dockOn.subPosition());
        });
        clsRepo.forEachMethod(MenuAction.class,
                (method, component, annotation)
                -> 
                {
                    if ((annotation.on().equals(Object.class)
                            && !WorkspacePanel.class.isAssignableFrom(component))
                            || annotation.on().equals(Workspace.class))
                    {
                        addMenuItem(new CompMethodMenuItem(method, component, annotation, context), annotation);
                    }
        });

        clsRepo.forEachMethod(ToolBarAction.class,
                (method, component, annotation)
                -> 
                {
                    if ((annotation.on().equals(Object.class)
                            && !WorkspacePanel.class.isAssignableFrom(component))
                            || annotation.on().equals(Workspace.class))
                    {
                        addToolsButton(new CompMethodButton(method, component, annotation, context));
                    }
        });
    }

    /**
     * Add a new {@link WorkspacePanel} to the workspace in the specified position.
     * 
     * @param node The panel to add.
     * @param position The main position of the panel.
     * @param subpos The secondary prosition of the panel.
     */
    public void addPanel(WorkspacePanel node, Position position, Position subpos)
    {
        if (node == null)
        {
            throw new IllegalArgumentException("node");
        }
        WorkspaceLayout<WorkspaceArea> container = mainLayout.get(position);
        if (container == null)
        {
            container = new WorkspaceLayout<>();
            mainLayout.set(position, container);
        }
        Node prev = container.get(subpos);
        if (prev != null)
        {
            nodePositions.remove(prev);
            nodeSubPosition.remove(prev);
        }
        WorkspaceArea area = container.get(subpos);
        if (area == null)
        {
            area = new WorkspaceArea();
            container.set(subpos, area);
        }
        area.add(node);
        nodePositions.put(node, position);
        nodeSubPosition.put(node, subpos);
    }

    /**
     * Remove the given paenl from the workspace.
     * 
     * @param node The panel to remove.
     */
    public void removePanel(WorkspacePanel node)
    {
        Position position = nodePositions.get(node);
        Position subpos = nodeSubPosition.get(node);
        if (position == null || subpos == null)
        {
            return;
        }
        WorkspaceLayout<WorkspaceArea> container = mainLayout.get(position);
        if (container != null)
        {
            WorkspaceArea area = container.get(subpos);
            if (area != null)
            {
                area.remove(node);
                if (area.isEmpty())
                {
                    container.set(subpos, null);
                }
            }
            if (container.isEmpty())
            {
                mainLayout.set(position, null);
            }
        }
    }

    private void addMenuItem(MenuItem menuItem, MenuAction menuAction)
    {
        if (topBox == null)
        {
            topBox = new VBox();
            setTop(topBox);
        }
        if (topMenu == null)
        {
            topMenu = new WorkspaceMenuBar();
            topBox.getChildren().add(0, topMenu);
        }
        topMenu.addMenuItem(menuItem, menuAction);
    }

    private void addToolsButton(Button button)
    {
        if (topBox == null)
        {
            topBox = new VBox();
            setTop(topBox);
        }
        if (topTools == null)
        {
            topTools = new ToolBar();
            topBox.getChildren().add(topTools);
        }
        topTools.getItems().add(button);
    }
}
