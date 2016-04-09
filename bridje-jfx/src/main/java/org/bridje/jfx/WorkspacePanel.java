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

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javax.annotation.PostConstruct;
import org.bridje.ioc.ClassRepository;
import org.bridje.ioc.Inject;
import org.bridje.ioc.IocContext;

/**
 *
 */
public class WorkspacePanel extends BorderPane
{
    private final StringProperty title;
    
    private Tab parentTab;
    
    private VBox topBox;

    private WorkspaceMenuBar topMenu;
    
    private ToolBar topTools;
    
    @Inject
    private IocContext context;
    
    public WorkspacePanel(String title)
    {
        this.title = new SimpleStringProperty(this, "title", title);
        this.title.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) ->
        {
            if(parentTab != null)
            {
                parentTab.setText(newValue);
            }
        });
    }    
    
    @PostConstruct
    private void init()
    {
        if(context == null)
        {
            return;
        }
        ClassRepository clsRepo = context.getClassRepository();
        clsRepo.forEachMethod(MenuAction.class, 
            (method, component, annotation) -> {
                if( (annotation.on().equals(Object.class) 
                        && this.getClass().equals(component)) 
                    || annotation.on().equals(this.getClass()))
                {
                    addMenuItem(new CompMethodMenuItem(method, component, annotation, context), annotation);
                }
            });

        clsRepo.forEachMethod(ToolBarAction.class, 
            (method, component, annotation) -> {
                if( (annotation.on().equals(Object.class) 
                        && this.getClass().equals(component)) 
                    || annotation.on().equals(this.getClass()))
                {
                    addToolsButton(new CompMethodButton(method, component, annotation, context));
                }
            });
    }
    
    public WorkspacePanel(String title, Node center)
    {
        super(center);
        this.title = new SimpleStringProperty(this, "title", title);
    }

    public String getTitle()
    {
        return title.get();
    }

    public void setTitle(String title)
    {
        this.title.set(title);
    }

    public StringProperty titleProperty()
    {
        return title;
    }

    Tab getParentTab()
    {
        return parentTab;
    }

    void setParentTab(Tab parentTab)
    {
        this.parentTab = parentTab;
    }

    private void addMenuItem(MenuItem menuItem, MenuAction menuAction)
    {
        if(topBox == null)
        {
            topBox = new VBox();
            setTop(topBox);
        }
        if(topMenu == null)
        {
            topMenu = new WorkspaceMenuBar();
            topBox.getChildren().add(0, topMenu);
        }
        topMenu.addMenuItem(menuItem, menuAction);
    }

    private void addToolsButton(Button button)
    {
        if(topBox == null)
        {
            topBox = new VBox();
            setTop(topBox);
        }
        if(topTools == null)
        {
            topTools = new ToolBar();
            topBox.getChildren().add(topTools);
        }
        topTools.getItems().add(button);
    }
}
