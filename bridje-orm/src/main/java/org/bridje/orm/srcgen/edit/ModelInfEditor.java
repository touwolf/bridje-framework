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
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.bridje.orm.srcgen.model.ModelInf;

public class ModelInfEditor extends GridPane
{
    private TextField tfName;
    
    private TextField tfPackage;
    
    private TextField tfTablePrefix;
    
    private TextArea tfEntityDesc;
    
    private TextArea tfFieldDesc;

    private final SimpleObjectProperty<ModelInf> modelProperty = new SimpleObjectProperty<>();
    
    private int index = 0;
    
    public ModelInfEditor()
    {
        setAlignment(Pos.TOP_LEFT);
        setPadding(new Insets(25));
        setHgap(10);
        setVgap(10);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(25);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(75);
        getColumnConstraints().addAll(col1,col2);
        tfName = addRow("Name", createTextField());
        tfPackage = addRow("Package", createTextField());
        tfTablePrefix = addRow("Table Prefix", createTextField());
        tfEntityDesc = addRow("Entity Desc.", createTextArea(3));
        tfFieldDesc = addRow("Field Desc.", createTextArea(3));
        modelProperty.addListener((ObservableValue<? extends ModelInf> observable, ModelInf oldValue, ModelInf newValue) ->
        {
            tfName.setText(newValue.getName());
            tfPackage.setText(newValue.getPackage());
            tfTablePrefix.setText(newValue.getTablePrefix());
            tfEntityDesc.setText(newValue.getEntityDescription());
            tfFieldDesc.setText(newValue.getFieldDescription());
        });
    }

    public SimpleObjectProperty<ModelInf> modelProperty()
    {
        return modelProperty;
    }

    public ModelInf getModel()
    {
        return modelProperty.get();
    }

    public void setModel(ModelInf model)
    {
        this.modelProperty.set(model);
    }

    private TextField createTextField()
    {
        TextField result = new TextField();
        result.setFont(Font.font(12d));
        return result;
    }

    private TextArea createTextArea(int rowCount)
    {
        TextArea result = new TextArea();
        result.setPrefRowCount(rowCount);
        result.setFont(Font.font(12d));
        return result;
    }

    private <T extends Node> T addRow(String label, T field)
    {
        Label lb = new Label(label);
        lb.setTextAlignment(TextAlignment.RIGHT);
        addRow(index++, lb, field);
        setFillWidth(field, true);
        return field;
    }
    
    public void updateModel()
    {
        getModel().setName(tfName.getText());
        getModel().setPackage(tfPackage.getText());
        getModel().setTablePrefix(tfTablePrefix.getText());
        getModel().setEntityDescription(tfEntityDesc.getText());
        getModel().setFieldDescription(tfFieldDesc.getText());
    }
}
