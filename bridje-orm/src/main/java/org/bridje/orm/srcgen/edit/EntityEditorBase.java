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

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.bridje.orm.srcgen.model.EntityInfBase;
import org.bridje.orm.srcgen.model.EntityInfTemplate;

public class EntityEditorBase<T extends EntityInfBase> extends EditorBase<T>
{
    private TextField tfName;

    private TextArea taDescription;

    private ComboBox<EntityInfTemplate> cbBase;

    public EntityEditorBase()
    {
        super();
        tfName = addRow("Name", createTextField());
        taDescription = addRow("Description", createTextArea(3));
        cbBase = addRow("Base", createComboBox(EntityInfTemplate.class));
        modelProperty().addListener((observable, oldValue, newValue) ->
        {
            if(newValue != null)
            {
                tfName.setText(newValue.getName());
                taDescription.setText(newValue.getDescription());
                cbBase.setValue(newValue.getBase());
            }
        });
    }

    public void updateModel()
    {
        getModel().setName(tfName.getText());
        getModel().setDescription(taDescription.getText());
        getModel().setBase(cbBase.getValue());
    }

    public ObjectProperty<ObservableList<EntityInfTemplate>> baseTemplatesProperty()
    {
        return cbBase.itemsProperty();
    }

    public ObservableList<EntityInfTemplate> getBaseTemplates()
    {
        return cbBase.itemsProperty().get();
    }
}
