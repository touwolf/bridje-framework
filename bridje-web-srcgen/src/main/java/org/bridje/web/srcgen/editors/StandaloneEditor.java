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

package org.bridje.web.srcgen.editors;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.bridje.jfx.utils.JfxUtils;
import org.bridje.web.srcgen.models.StandaloneDefModel;
import org.bridje.web.srcgen.models.FieldDefModel;
import org.bridje.web.srcgen.models.UISuitesModel;

public final class StandaloneEditor extends GridPane
{
    private final SimpleObjectProperty<StandaloneDefModel> standaloneProperty = new SimpleObjectProperty<>();

    private final FieldsEditor fieldsEditor = new FieldsEditor();

    private final HBox hbToolbar = new HBox();

    public StandaloneEditor()
    {
        setVgap(10);
        setHgap(10);

        setPadding(new Insets(10));

        add(hbToolbar, 0, 0);
        add(fieldsEditor, 0, 1);

        setFillWidth(hbToolbar, true);
        setHgrow(hbToolbar, Priority.ALWAYS);
        setFillWidth(fieldsEditor, true);
        setHgrow(fieldsEditor, Priority.ALWAYS);
        setFillHeight(fieldsEditor, true);
        setVgrow(fieldsEditor, Priority.ALWAYS);
        
        hbToolbar.getChildren().add(JfxUtils.createToolButton(UISuitesModel.add(32), this::addChild));
        hbToolbar.getChildren().add(JfxUtils.createToolButton(UISuitesModel.delete(32), this::deleteField));

        standaloneProperty.addListener((observable, oldValue, newValue) ->
        {
            if(oldValue != null)
            {
                fieldsEditor.fieldsProperty().unbindBidirectional(oldValue.contentProperty());
            }
            if(newValue != null)
            {
                fieldsEditor.fieldsProperty().bindBidirectional(newValue.contentProperty());
            }
        });
    }

    public StringConverter<StandaloneDefModel> createStringConverter(Callback<String, StandaloneDefModel> callback)
    {
        return new StringConverter<StandaloneDefModel>()
        {
            @Override
            public String toString(StandaloneDefModel object)
            {
                if(object == null) return null;
                return object.getName();
            }

            @Override
            public StandaloneDefModel fromString(String name)
            {
                return callback.call(name);
            }
        };
    }

    public SimpleObjectProperty<StandaloneDefModel> standaloneProperty()
    {
        return this.standaloneProperty;
    }

    public StandaloneDefModel getStandalone()
    {
        return this.standaloneProperty.get();
    }

    public void setStandalone(StandaloneDefModel control)
    {
        this.standaloneProperty.set(control);
    }

    public void addChild(ActionEvent event)
    {
        FieldDefModel field = new FieldDefModel();
        field.setField("child");
        field.setName("newChild" + getStandalone().getContent().size());
        getStandalone().getContent().add(field);
    }

    public void deleteField(ActionEvent event)
    {
        if(fieldsEditor.getSelectedParent() != null && fieldsEditor.getSelected() != null)
        {
            fieldsEditor.getSelectedParent().getChilds().remove(fieldsEditor.getSelected());
        }
    }
}
