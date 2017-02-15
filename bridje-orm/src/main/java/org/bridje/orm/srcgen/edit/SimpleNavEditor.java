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

package org.bridje.orm.srcgen.edit;

import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class SimpleNavEditor extends VBox
{
    private Label titleLable;

    public SimpleNavEditor()
    {
        super(10d);
        setPadding(new Insets(10d));
        titleLable = new Label();
        titleLable.setFont(Font.font(20d));
        getChildren().add(titleLable);
    }
    
    public StringProperty titleProperty()
    {
        return titleLable.textProperty();
    }
    
    public String getTitle()
    {
        return titleLable.getText();
    }

    public void setTitle(String value)
    {
        titleLable.setText(value);
    }
}
