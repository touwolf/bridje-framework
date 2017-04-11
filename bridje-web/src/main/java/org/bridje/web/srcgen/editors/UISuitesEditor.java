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

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javax.xml.bind.JAXBException;
import org.bridje.jfx.utils.BiContentConverter;
import org.bridje.jfx.utils.ExBindings;
import org.bridje.vfs.VFileOutputStream;
import org.bridje.web.srcgen.models.ControlDefModel;
import org.bridje.web.srcgen.models.UISuiteModel;
import org.bridje.web.srcgen.models.UISuitesModel;
import org.bridje.web.srcgen.uisuite.UISuite;

public class UISuitesEditor extends BorderPane
{
    private static final Logger LOG = Logger.getLogger(UISuitesEditor.class.getName());

    private final TreeView<Object> tree;

    private final TreeItem<Object> root;

    private final ControlEditor ctrlEditor;

    private final UISuiteEditor stEditor;

    private final StackPane editArea;

    private final ToolBar toolBar;

    private final Button btSave;

    private final Button btAddControl;

    private final Button btAddTemplate;
    
    private final Button btAddField;

    private final SimpleObjectProperty<UISuitesModel> suitesProperty = new SimpleObjectProperty<>();

    public UISuitesEditor()
    {
        tree = new TreeView<>();
        ctrlEditor = new ControlEditor();
        stEditor = new UISuiteEditor();
        editArea = new StackPane();

        SplitPane sp = new SplitPane(tree, editArea);
        sp.setDividerPositions(0.3d);
        setCenter(sp);

        root = new TreeItem<>();
        tree.setRoot(root);
        tree.setShowRoot(false);

        btSave = new Button("Save");
        btSave.setOnAction(this::save);

        btAddControl = new Button("Add Control");
        btAddControl.setOnAction(this::addControl);

        btAddTemplate = new Button("Add Template");
        btAddTemplate.setOnAction(this::addTemplate);

        btAddField = new Button("Add Field");
        btAddField.setOnAction(ctrlEditor::addField);
        
        toolBar = new ToolBar(btSave, btAddControl, btAddTemplate, btAddField);
        setTop(toolBar);

        suitesProperty
                .addListener((observable, oldValue, newValue) -> 
                {
                    if(oldValue != null)
                    {
                        ExBindings.unbindContentBidirectional(root.getChildren(), oldValue.getSuites());
                    }
                    if(newValue != null)
                    {
                        ExBindings.bindContentBidirectional(root.getChildren(), newValue.getSuites(), createSuitesTreeConverter());
                    }
                });

        tree.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->
                {
                    if(newValue != null)
                    {
                        Object value = newValue.getValue();
                        if(value instanceof UISuiteModel)
                        {
                            editArea.getChildren().clear();
                            editArea.getChildren().add(stEditor);
                            stEditor.setUISuite((UISuiteModel)value);
                        }
                        else if(value instanceof ControlDefModel)
                        {
                            editArea.getChildren().clear();
                            editArea.getChildren().add(ctrlEditor);
                            TreeItem<Object> parent = newValue.getParent().getParent();
                            ctrlEditor.setUISuite((UISuiteModel)parent.getValue());
                            ctrlEditor.setControl((ControlDefModel)value);
                        }
                    }
                });
    }

    public void addTemplate(ActionEvent event)
    {
        UISuiteModel uiSuite = getCurrentUISuite();
        if(uiSuite != null)
        {
            ControlDefModel ctrl = new ControlDefModel();
            ctrl.setName("NewTemplate");
            ctrl.setFields(FXCollections.observableArrayList());
            uiSuite.getControlsTemplates().add(ctrl);
        }        
    }

    public void addControl(ActionEvent event)
    {
        UISuiteModel uiSuite = getCurrentUISuite();
        if(uiSuite != null)
        {
            ControlDefModel ctrl = new ControlDefModel();
            ctrl.setName("NewControl");
            ctrl.setFields(FXCollections.observableArrayList());
            uiSuite.getControls().add(ctrl);
        }
    }

    public void save(ActionEvent event)
    {
        UISuiteModel uiSuite = getCurrentUISuite();
        if(uiSuite != null)
        {
            UISuite data = ModelUtils.fromModel(uiSuite);
            try(OutputStream os = new VFileOutputStream(uiSuite.getFile()))
            {
                UISuite.save(os, data);
            }
            catch(JAXBException | IOException ex)
            {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    public SimpleObjectProperty<UISuitesModel> suitesProperty()
    {
        return this.suitesProperty;
    }

    public UISuitesModel getSuites()
    {
        return this.suitesProperty.get();
    }

    public void setSuites(UISuitesModel suites)
    {
        this.suitesProperty.set(suites);
    }

    private TreeItem<Object> toTreeItem(ControlDefModel control)
    {
        TreeItem<Object> tiControl = new TreeItem<>(control);
        return tiControl;
    }

    private TreeItem<Object> toTreeItem(UISuiteModel suite)
    {
        TreeItem<Object> tiSuite = new TreeItem<>(suite);
        tiSuite.setGraphic(createIcon("uisuite.png"));
        TreeItem<Object> tiTemplates = new TreeItem<>("Templates");
        TreeItem<Object> tiControls = new TreeItem<>("Controls");
        tiSuite.getChildren().addAll(tiTemplates, tiControls);
        BiContentConverter<TreeItem<Object>, ControlDefModel> ctrlConverter = createCtrlConverter();
        ExBindings.bindContentBidirectional(tiTemplates.getChildren(), suite.getControlsTemplates(), ctrlConverter);
        ExBindings.bindContentBidirectional(tiControls.getChildren(), suite.getControls(), ctrlConverter);
        
        return tiSuite;
    }

    private UISuiteModel getCurrentUISuite()
    {
        TreeItem<Object> selectedItem = tree.getSelectionModel().getSelectedItem();
        while(selectedItem != null)
        {
            if(selectedItem.getValue() instanceof UISuiteModel)
            {
                return (UISuiteModel)selectedItem.getValue();
            }
            selectedItem = selectedItem.getParent();
        }
        return null;
    }

    private BiContentConverter<TreeItem<Object>, ControlDefModel> createCtrlConverter()
    {
        return new BiContentConverter<TreeItem<Object>, ControlDefModel>()
        {
            @Override
            public ControlDefModel convertFrom(TreeItem<Object> value)
            {
                return (ControlDefModel)value.getValue();
            }

            @Override
            public TreeItem<Object> convertTo(ControlDefModel value)
            {
                return toTreeItem(value);
            }
        };
    }
    
    private BiContentConverter<TreeItem<Object>, UISuiteModel> createSuitesTreeConverter()
    {
        return new BiContentConverter<TreeItem<Object>, UISuiteModel>()
        {
            @Override
            public UISuiteModel convertFrom(TreeItem<Object> value)
            {
                return (UISuiteModel)value.getValue();
            }

            @Override
            public TreeItem<Object> convertTo(UISuiteModel value)
            {
                return toTreeItem(value);
            }
        };
    }

    private Node createIcon(String resource)
    {
        Image img = new Image(getClass().getResourceAsStream(resource));
        ImageView iv = new ImageView(img);
        iv.setFitHeight(16d);
        iv.setFitWidth(16d);
        return iv;
    }
}
