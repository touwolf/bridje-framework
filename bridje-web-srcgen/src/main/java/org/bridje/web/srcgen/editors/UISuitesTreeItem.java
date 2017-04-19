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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import org.bridje.jfx.binding.BiContentConverter;
import org.bridje.jfx.binding.ExBindings;
import org.bridje.jfx.utils.JfxUtils;
import org.bridje.srcgen.editor.EditorTreeItem;
import org.bridje.web.srcgen.models.UISuiteModel;
import org.bridje.web.srcgen.models.UISuitesModel;

public final class UISuitesTreeItem extends EditorTreeItem
{
    private final SimpleObjectProperty<UISuitesModel> suitesProperty = new SimpleObjectProperty<>();

    public UISuitesTreeItem()
    {
        super("UI Suites", UISuitesModel.uisuite(16));
        setContextMenu(createMenu());
        setToolBar(createToolBar());
        suitesProperty
                .addListener((observable, oldValue, newValue) -> 
                {
                    if(oldValue != null) ExBindings.unbindContentBidirectional(getChildren(), oldValue.getSuites());
                    if(newValue != null) ExBindings.bindContentBidirectional(getChildren(), newValue.getSuites(), createSuitesTreeConverter());
                });
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

    private TreeItem<Object> toTreeItem(UISuiteModel suite)
    {
        UISuiteTreeItem tiSuite = new UISuiteTreeItem(suite);
        return tiSuite;
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

    private ContextMenu createMenu()
    {
        ContextMenu result = new ContextMenu();
        result.getItems().add(JfxUtils.createMenuItem("Create a new UI Suite", UISuitesModel.add(24), this::addUISuite));
        return result;
    }

    public void addUISuite(ActionEvent event)
    {
        UISuiteModel model = new UISuiteModel();
        model.setName("NewUISuite");
        getSuites().getSuites().add(model);
    }

    public void saveAllSuites(ActionEvent event)
    {
        getSuites().getSuites().forEach(s -> ModelUtils.saveUISuite(s));
    }

    private ToolBar createToolBar()
    {
        ToolBar mBar = new ToolBar();
        mBar.getItems().add(JfxUtils.createToolButton(UISuitesModel.save(32), this::saveAllSuites));
        mBar.getItems().add(JfxUtils.createToolButton(UISuitesModel.addUISuite(32), this::addUISuite));
        return mBar;
    }
}
