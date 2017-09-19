/*
 * Copyright 2017 Bridje Framework.
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

package org.bridje.web.srcgen.editor;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.bridje.ioc.Component;

@Component
public class UISuiteEditor extends StackPane
{
    private final SimpleObjectProperty<UISuiteBaseModel> uisuiteProperty = new SimpleObjectProperty<>();

    public UISuiteEditor()
    {
        GridPane grid = new GridPane();

        grid.add(new Label("Nombre"), 0, 0);
        grid.add(new TextField(), 1, 0);

        getChildren().add(grid);
    }
    
    public SimpleObjectProperty<UISuiteBaseModel> uisuiteProperty()
    {
        return this.uisuiteProperty;
    }

    public UISuiteBaseModel getUISuite()
    {
        return this.uisuiteProperty.get();
    }

    public void setUISuite(UISuiteBaseModel value)
    {
        this.uisuiteProperty.set(value);
    }
}
