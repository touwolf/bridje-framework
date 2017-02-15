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

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.bridje.orm.srcgen.model.ModelInf;

public class ModelInfEditor extends EditorBase<ModelInf>
{
    private TextField tfName;
    
    private TextField tfPackage;
    
    private TextField tfTablePrefix;
    
    private TextArea tfEntityDesc;
    
    private TextArea tfFieldDesc;

    public ModelInfEditor()
    {
        super();
        tfName = addRow("Name", createTextField());
        tfPackage = addRow("Package", createTextField());
        tfTablePrefix = addRow("Table Prefix", createTextField());
        tfEntityDesc = addRow("Entity Desc.", createTextArea(3));
        tfFieldDesc = addRow("Field Desc.", createTextArea(3));
        modelProperty().addListener((ObservableValue<? extends ModelInf> observable, ModelInf oldValue, ModelInf newValue) ->
        {
            tfName.setText(newValue.getName());
            tfPackage.setText(newValue.getPackage());
            tfTablePrefix.setText(newValue.getTablePrefix());
            tfEntityDesc.setText(newValue.getEntityDescription());
            tfFieldDesc.setText(newValue.getFieldDescription());
        });
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
