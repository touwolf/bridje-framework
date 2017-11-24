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
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import static javafx.scene.layout.GridPane.setFillWidth;
import static javafx.scene.layout.GridPane.setHgrow;
import javafx.scene.layout.Priority;
import org.bridje.jfx.ace.AceEditor;
import org.bridje.jfx.ace.AceMode;
import org.bridje.web.srcgen.models.UISuiteModel;

public final class UISuiteEditor extends GridPane
{
    private final TextField tfName = new TextField();

    private final TextField tfNamespace = new TextField();

    private final TextField tfPackage = new TextField();

    private final AceEditor taRenderBody = new AceEditor(AceMode.FTL);

    private final AceEditor taRenderHead = new AceEditor(AceMode.FTL);

    private final AceEditor taRenderView = new AceEditor(AceMode.FTL);

    private final SimpleObjectProperty<UISuiteModel> uiSuiteProperty = new SimpleObjectProperty<>();

    public UISuiteEditor()
    {
        setVgap(10);
        setHgap(10);

        setPadding(new Insets(10));

        add(tfName, 0, 0);
        add(tfNamespace, 1, 0);
        add(tfPackage, 2, 0);
        add(new Label("Render Head"), 0, 1);
        add(taRenderHead, 0, 2, 3, 1);
        add(new Label("Render Body"), 0, 3);
        add(taRenderBody, 0, 4, 3, 1);
        add(new Label("Render View"), 0, 5);
        add(taRenderView, 0, 6, 3, 1);

        setFillWidth(tfName, true);
        setHgrow(tfName, Priority.ALWAYS);
        setFillWidth(tfNamespace, true);
        setHgrow(tfNamespace, Priority.ALWAYS);
        setFillWidth(tfPackage, true);
        setHgrow(tfPackage, Priority.ALWAYS);

        setFillWidth(taRenderHead, true);
        setHgrow(taRenderHead, Priority.ALWAYS);
        setFillHeight(taRenderHead, true);
        setVgrow(taRenderHead, Priority.ALWAYS);

        setFillWidth(taRenderBody, true);
        setHgrow(taRenderBody, Priority.ALWAYS);
        setFillHeight(taRenderBody, true);
        setVgrow(taRenderBody, Priority.ALWAYS);

        setFillWidth(taRenderView, true);
        setHgrow(taRenderView, Priority.ALWAYS);
        setFillHeight(taRenderView, true);
        setVgrow(taRenderView, Priority.ALWAYS);

        uiSuiteProperty.addListener((observable, oldValue, newValue) ->
        {
            if(oldValue != null)
            {
                tfName.textProperty().unbindBidirectional(oldValue.nameProperty());
                tfNamespace.textProperty().unbindBidirectional(oldValue.namespaceProperty());
                tfPackage.textProperty().unbindBidirectional(oldValue.packageNameProperty());
                taRenderHead.textProperty().unbindBidirectional(oldValue.renderHeadProperty());
                taRenderBody.textProperty().unbindBidirectional(oldValue.renderBodyProperty());
                taRenderView.textProperty().unbindBidirectional(oldValue.renderViewContainerProperty());
            }
            if(getUISuite() != null)
            {
                tfName.textProperty().bindBidirectional(getUISuite().nameProperty());
                tfNamespace.textProperty().bindBidirectional(getUISuite().namespaceProperty());
                tfPackage.textProperty().bindBidirectional(getUISuite().packageNameProperty());
                taRenderHead.textProperty().bindBidirectional(getUISuite().renderHeadProperty());
                taRenderBody.textProperty().bindBidirectional(getUISuite().renderBodyProperty());
                taRenderView.textProperty().bindBidirectional(getUISuite().renderViewContainerProperty());
            }
        });
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
}
