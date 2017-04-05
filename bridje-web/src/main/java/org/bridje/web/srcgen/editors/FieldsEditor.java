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
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import org.bridje.web.srcgen.models.FieldDefModel;
import org.bridje.web.srcgen.models.FieldDefModelTable;

public class FieldsEditor extends StackPane
{
    private final SimpleObjectProperty<ObservableList<FieldDefModel>> fieldsProperty = new SimpleObjectProperty<>();

    private final FieldDefModelTable table;

    public FieldsEditor()
    {
        table = new FieldDefModelTable();
        table.addFieldColumn("Field");
        table.editableFieldColumn(fieldEditor(), null);
        table.addNameColumn("Name");
        table.editableNameColumn(null);
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
        
        table.itemsProperty().bindBidirectional(fieldsProperty());
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

    private Callback<TableColumn<FieldDefModel, Boolean>, TableCell<FieldDefModel, Boolean>> boolEditor()
    {
        return ComboBoxTableCell.forTableColumn(true, false);
    }

    private Callback<TableColumn<FieldDefModel, String>, TableCell<FieldDefModel, String>> fieldEditor()
    {
        return ComboBoxTableCell.forTableColumn(
            "outAttr",
            "inAttr",
            "eventAttr",
            "attr",
            "outEl",
            "inEl",
            "eventEl",
            "el",
            "outValue",
            "inValue",
            "eventValue",
            "value",
            "child",
            "children"
        );
    }
}
