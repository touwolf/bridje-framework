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
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

/**
 *
 */
public class WorkspacePanel extends BorderPane
{
    private final StringProperty title;
    
    private Tab parentTab;
    
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
}
