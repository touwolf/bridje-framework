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

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.bridje.jfx.ace.AceEditor;
import org.bridje.jfx.utils.JfxUtils;
import org.bridje.web.srcgen.models.ControlDefModel;
import org.bridje.web.srcgen.models.FieldDefModel;
import org.bridje.web.srcgen.models.UISuiteModel;

public final class ControlEditor extends GridPane
{
    private final SimpleObjectProperty<ControlDefModel> controlProperty = new SimpleObjectProperty<>();

    private final SimpleObjectProperty<UISuiteModel> uiSuiteProperty = new SimpleObjectProperty<>();

    private final FieldsEditor fieldsEditor = new FieldsEditor();

    private final ResourcesSelector resourcesSelector = new ResourcesSelector();

    private final TextField tfName = new TextField();

    private final ComboBox<ControlDefModel> cbBase = new ComboBox<>();

    private final ComboBox<ControlDefModel> cbBaseTemplate = new ComboBox<>();

    private final AceEditor taRender = new AceEditor(AceEditor.Mode.FTL);

    private ChangeListener<String> nameListener;
    
    public ControlEditor()
    {
        setVgap(10);
        setHgap(10);

        setPadding(new Insets(10));

        add(tfName, 0, 0);
        add(cbBase, 1, 0);
        add(cbBaseTemplate, 2, 0);
        add(resourcesSelector, 0, 1);
        add(fieldsEditor, 1, 1, 2, 1);
        add(taRender, 0, 2, 3, 1);

        setFillWidth(fieldsEditor, true);
        setHgrow(fieldsEditor, Priority.ALWAYS);

        setFillWidth(cbBaseTemplate, true);
        setHgrow(cbBaseTemplate, Priority.ALWAYS);

        setFillWidth(taRender, true);
        setHgrow(taRender, Priority.ALWAYS);
        setFillHeight(taRender, true);
        setVgrow(taRender, Priority.ALWAYS);

        getRowConstraints().add(new RowConstraints());
        RowConstraints rowConst = new RowConstraints();
        rowConst.setPercentHeight(45d);
        getRowConstraints().add(rowConst);
        
        nameListener = (observable, oldValue, newValue) -> updateName(oldValue, newValue);

        uiSuiteProperty.addListener((observable, oldValue, newValue) ->
        {
            if(oldValue != null)
            {
                Bindings.unbindContent(cbBase.getItems(), oldValue.getControls());
                Bindings.unbindContent(cbBaseTemplate.getItems(), oldValue.getControlsTemplates());
            }
            if(getUISuite() != null)
            {
                if(getUISuite().getControls() == null) getUISuite().setControls(FXCollections.observableArrayList());
                Bindings.bindContent(cbBase.getItems(), getUISuite().getControls());
                if(getUISuite().getControlsTemplates() == null) getUISuite().setControlsTemplates(FXCollections.observableArrayList());
                Bindings.bindContent(cbBaseTemplate.getItems(), getUISuite().getControlsTemplates());
            }
        });

        controlProperty.addListener((observable, oldValue, newValue) ->
        {
            if(oldValue != null)
            {
                oldValue.nameProperty().removeListener(nameListener);
                tfName.textProperty().unbindBidirectional(oldValue.nameProperty());
                Bindings.unbindBidirectional(cbBase.valueProperty(), oldValue.baseProperty());
                Bindings.unbindBidirectional(cbBaseTemplate.valueProperty(), oldValue.baseTemplateProperty());
                fieldsEditor.fieldsProperty().unbindBidirectional(oldValue.fieldsProperty());
                resourcesSelector.itemsProperty().unbindBidirectional(oldValue.resourcesProperty());
                taRender.textProperty().unbindBidirectional(oldValue.renderProperty());
            }
            if(getControl() != null)
            {
                tfName.textProperty().bindBidirectional(getControl().nameProperty());
                StringConverter<ControlDefModel> contConvert = createStringConverter(name -> findControl(name));
                getControl().baseProperty().bindBidirectional(cbBase.valueProperty(), contConvert);
                StringConverter<ControlDefModel> tmplConverter = createStringConverter(name -> findTemplate(name));
                getControl().baseTemplateProperty().bindBidirectional(cbBaseTemplate.valueProperty(), tmplConverter);
                if(getControl().getFields() == null) getControl().setFields(FXCollections.observableArrayList());
                fieldsEditor.fieldsProperty().bindBidirectional(getControl().fieldsProperty());
                if(getControl().getResources() == null) getControl().setResources(FXCollections.observableArrayList());
                resourcesSelector.itemsProperty().bindBidirectional(getControl().resourcesProperty());
                taRender.textProperty().bindBidirectional(getControl().renderProperty());
                newValue.nameProperty().addListener(nameListener);
            }
        });
        
        taRender.getContextMenu().getItems().add(0, JfxUtils.createMenuItem("Create Child", null, this::createChildFildFromCurrentSelection));
    }

    public StringConverter<ControlDefModel> createStringConverter(Callback<String, ControlDefModel> callback)
    {
        return new StringConverter<ControlDefModel>()
        {
            @Override
            public String toString(ControlDefModel object)
            {
                if(object == null) return null;
                return object.getName();
            }

            @Override
            public ControlDefModel fromString(String name)
            {
                return callback.call(name);
            }
        };
    }

    public ControlDefModel findTemplate(String base)
    {
        if(getUISuite() == null) return null;
        if(getUISuite().getControlsTemplates() == null) return null;
        if(getControl() == null) return null;
        if(getControl().getBaseTemplate() == null) return null;
        return getUISuite()
                    .getControlsTemplates()
                    .stream()
                    .filter(c -> c.getName().equals(base))
                    .findFirst()
                    .orElse(null);
    }

    public ControlDefModel findControl(String base)
    {
        if(getUISuite() == null) return null;
        if(getUISuite().getControls() == null) return null;
        if(getControl() == null) return null;
        if(getControl().getBase() == null) return null;
        return getUISuite()
                    .getControls()
                    .stream()
                    .filter(c -> c.getName().equals(getControl().getBase()))
                    .findFirst()
                    .orElse(null);
    }

    public SimpleObjectProperty<ControlDefModel> controlsProperty()
    {
        return this.controlProperty;
    }

    public ControlDefModel getControl()
    {
        return this.controlProperty.get();
    }

    public void setControl(ControlDefModel control)
    {
        this.controlProperty.set(control);
    }

    public SimpleObjectProperty<UISuiteModel> uiSuiteProperty()
    {
        return this.uiSuiteProperty;
    }

    public UISuiteModel getUISuite()
    {
        return this.uiSuiteProperty.get();
    }

    public void setUISuite(UISuiteModel control)
    {
        this.uiSuiteProperty.set(control);
    }

    public void addField(ActionEvent event)
    {
        FieldDefModel field = new FieldDefModel();
        getControl().getFields().add(field);
    }
    
    public void createChildFildFromCurrentSelection(ActionEvent event)
    {
        String selection = taRender.findSelection();
        if(selection != null && !selection.isEmpty())
        {
            ControlDefModel ctrl = new ControlDefModel();
            ctrl.setName("NewControl" + getUISuite().getControls().size());
            ctrl.setRender(selection);
            getUISuite().getControls().add(ctrl);

            FieldDefModel field = new FieldDefModel();
            field.setName("newChildField" + getControl().getFields().size());
            field.setType(ctrl.getName());
            field.setField("child");
            getControl().getFields().add(field);

            taRender.replaceSelection("<#if control.newChild??>\n\t<@renderControl control." + field.getName() + " />\n</#if>");
        }
    }

    private void updateName(String oldValue, String newValue)
    {
        getUISuite().getControls().forEach(c -> updateChildTypes(c, oldValue, newValue));
    }

    private void updateChildTypes(ControlDefModel control, String oldValue, String newValue)
    {
        control.getFields()
                .stream()
                .filter(f -> "child".equalsIgnoreCase(f.getField()))
                .filter(f -> oldValue.equals(f.getType()))
                .forEach(f -> f.setType(newValue));

        control.getFields()
                .stream()
                .filter(f -> "children".equalsIgnoreCase(f.getField()))
                .forEach(f -> updateChildrenTypes(f, oldValue, newValue));
    }
    
    private void updateChildrenTypes(FieldDefModel field, String oldValue, String newValue)
    {
        if(field.getChilds() == null) return;
        field.getChilds()
                .stream()
                .filter(f -> oldValue.equals(f.getType()))
                .forEach(f -> f.setType(newValue));
    }
}
