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
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.ComboBoxTreeTableCell;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.bridje.jfx.utils.BiContentConverter;
import org.bridje.jfx.utils.ExBindings;
import org.bridje.web.srcgen.models.FieldDefModel;
import org.bridje.web.srcgen.models.FieldDefModelTreeTable;

public final class FieldsEditor extends StackPane
{
    private final SimpleObjectProperty<ObservableList<FieldDefModel>> fieldsProperty = new SimpleObjectProperty<>();

    private final FieldDefModelTreeTable table;

    private final BiContentConverter<TreeItem<FieldDefModel>, FieldDefModel> converter;
    
    private ChangeListener<String> nameListener;
    
    public FieldsEditor()
    {
        table = new FieldDefModelTreeTable();
        table.addNameColumn("Name");
        table.editableNameColumn(null);
        table.addFieldColumn("Field");
        table.editableFieldColumn(fieldEditor(), null);
        table.addTypeColumn("Type");
        table.editableTypeColumn(null);
        table.addDefaultValueColumn("Default");
        table.editableDefaultValueColumn(null);
        table.addAllowPlaceHolderColumn("Allow Place Holder");
        table.editableAllowPlaceHolderColumn(boolEditor(), null);
        table.addWrapperColumn("Wrapper");
        table.editableWrapperColumn(null);
        table.addSingleColumn("Single");
        table.editableSingleColumn(boolEditor(), null);

        getChildren().add(table);
        converter = createConverter(createChildsConverter());

        FieldDefModel rootField = new FieldDefModel();
        rootField.setChilds(FXCollections.observableArrayList());
        TreeItem<FieldDefModel> root = new TreeItem<>(rootField);
        ExBindings.bindContentBidirectional(root.getChildren(), rootField.getChilds(), converter);
        fieldsProperty.addListener((observable, oldValue, newValue) ->
        {
            if(oldValue != null)
            {
                if(nameListener != null) oldValue.stream().forEach(f -> f.nameProperty().removeListener(nameListener));
                Bindings.unbindContentBidirectional(rootField.getChilds(), oldValue);
            }
            if(newValue != null)
            {
                if(nameListener != null) newValue.stream().forEach(f -> f.nameProperty().addListener(nameListener));
                Bindings.bindContentBidirectional(rootField.getChilds(), newValue);
            }
        });
        table.setRoot(root);
        table.setShowRoot(false);
    }

    public SimpleObjectProperty<ObservableList<FieldDefModel>> fieldsProperty()
    {
        return this.fieldsProperty;
    }

    public ObservableList<FieldDefModel> getFields()
    {
        return this.fieldsProperty.get();
    }

    public void setFields(ObservableList<FieldDefModel> fields)
    {
        this.fieldsProperty.set(fields);
    }

    public ChangeListener<String> getNameListener()
    {
        return nameListener;
    }

    public void setNameListener(ChangeListener<String> nameListener)
    {
        this.nameListener = nameListener;
    }
    
    private Callback<TreeTableColumn<FieldDefModel, Boolean>, TreeTableCell<FieldDefModel, Boolean>> boolEditor()
    {
        return ComboBoxTreeTableCell.forTreeTableColumn(true, false);
    }

    private Callback<TreeTableColumn<FieldDefModel, String>, TreeTableCell<FieldDefModel, String>> fieldEditor()
    {
        return ComboBoxTreeTableCell.forTreeTableColumn(
            "outAttr", "inAttr", "eventAttr", "attr", 
            "outEl", "inEl", "eventEl", "el", 
            "outValue", "inValue", "eventValue", "value", 
            "child", "children");
    }

    private BiContentConverter<TreeItem<FieldDefModel>, FieldDefModel> createConverter(BiContentConverter<TreeItem<FieldDefModel>, FieldDefModel> childsConverter)
    {
        return new BiContentConverter<TreeItem<FieldDefModel>, FieldDefModel>()
        {
            @Override
            public FieldDefModel convertFrom(TreeItem<FieldDefModel> value)
            {
                return value.getValue();
            }

            @Override
            public TreeItem<FieldDefModel> convertTo(FieldDefModel value)
            {
                TreeItem<FieldDefModel> result = new TreeItem<>(value);
                if(value.getChilds() == null) value.setChilds(FXCollections.observableArrayList());
                ExBindings.bindContentBidirectional(result.getChildren(), value.getChilds(), childsConverter);
                return result;
            }
        };
    }

    private BiContentConverter<TreeItem<FieldDefModel>, FieldDefModel> createChildsConverter()
    {
        return new BiContentConverter<TreeItem<FieldDefModel>, FieldDefModel>()
        {
            @Override
            public TreeItem<FieldDefModel> convertTo(FieldDefModel value)
            {
                TreeItem<FieldDefModel> result = new TreeItem<>(value);
                return result;
            }

            @Override
            public FieldDefModel convertFrom(TreeItem<FieldDefModel> value)
            {
                return value.getValue();
            }
        };
    }
}
