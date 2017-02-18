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

import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class EditorBase<T> extends GridPane
{
    private final SimpleObjectProperty<T> modelProperty = new SimpleObjectProperty<>();
    
    private int index = 0;
    
    public EditorBase()
    {
        initGridPane();
    }

    public SimpleObjectProperty<T> modelProperty()
    {
        return modelProperty;
    }

    public T getModel()
    {
        return modelProperty.get();
    }

    public void setModel(T model)
    {
        this.modelProperty.set(model);
    }

    protected TextField createTextField()
    {
        TextField result = new TextField();
        result.setFont(Font.font(12d));
        return result;
    }

    protected TextArea createTextArea(int rowCount)
    {
        TextArea result = new TextArea();
        result.setPrefRowCount(rowCount);
        result.setFont(Font.font(12d));
        return result;
    }

    protected <T> ComboBox createComboBox(Class<T> cls)
    {
        ComboBox<T> result = new ComboBox<>();
        return result;
    }

    protected <T extends Node> T addRow(String label, T field)
    {
        Label lb = new Label(label);
        lb.setTextAlignment(TextAlignment.RIGHT);
        addRow(index++, lb, field);
        setFillWidth(field, true);
        return field;
    }

    private void initGridPane()
    {
        setAlignment(Pos.TOP_LEFT);
        setPadding(new Insets(25));
        setHgap(10);
        setVgap(10);
        getColumnConstraints().addAll(createCol(25),createCol(75));
    }
    
    private ColumnConstraints createCol(int size)
    {
        ColumnConstraints columns = new ColumnConstraints();
        columns.setPercentWidth(size);
        return columns;
    }
}
