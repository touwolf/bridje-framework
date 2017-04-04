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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javax.xml.bind.JAXBException;
import org.bridje.vfs.VFileOutputStream;
import org.bridje.web.srcgen.models.ControlDefModel;
import org.bridje.web.srcgen.models.UISuiteModel;
import org.bridje.web.srcgen.models.UISuitesModel;
import org.bridje.web.srcgen.uisuite.UISuite;

public class UISuitesEditor extends BorderPane
{
    private static final Logger LOG = Logger.getLogger(UISuitesEditor.class.getName());

    private UISuiteConverter converter;

    private final TreeView<Object> tree;

    private final TreeItem<Object> root;

    private final ControlEditor ctrlEditor;

    private final UISuiteEditor stEditor;

    private final StackPane editArea;

    private final ToolBar toolBar;

    private final Button btSave;

    private final SimpleObjectProperty<UISuitesModel> suitesProperty = new SimpleObjectProperty<>();

    public UISuitesEditor()
    {
        converter = new UISuiteConverter();
        btSave = new Button("Save");
        btSave.setOnAction(this::save);
        toolBar = new ToolBar(btSave);
        setTop(toolBar);
        
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

        suitesProperty.addListener((observable, oldValue, newValue) ->
        {
            initChildrens(newValue);
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

    public void save(ActionEvent event)
    {
        UISuiteModel uiSuite = getCurrentUISuite();
        if(uiSuite != null)
        {
            UISuite data = converter.fromModel(uiSuite);
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

    private List<TreeItem<Object>> toTreeItems(UISuitesModel newValue)
    {
        List<TreeItem<Object>> result = new ArrayList<>();
        newValue.getSuites().forEach(s -> result.add(toTreeItem(s)));
        return result;
    }

    private TreeItem<Object> toTreeItem(ControlDefModel control)
    {
        TreeItem<Object> tiControl = new TreeItem<>(control);
        return tiControl;
    }

    private TreeItem<Object> toTreeItem(UISuiteModel suite)
    {
        TreeItem<Object> tiSuite = new TreeItem<>(suite);
        TreeItem<Object> tiControls = new TreeItem<>("Controls");
        suite.getControls().forEach(c -> tiControls.getChildren().add(toTreeItem(c)));
        tiSuite.getChildren().add(tiControls);
        return tiSuite;
    }

    private void initChildrens(UISuitesModel newValue)
    {
        root.getChildren().clear();
        if(newValue != null)
        {
            root.getChildren().addAll(toTreeItems(newValue));
        }
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
}
