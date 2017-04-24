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

import java.io.File;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import org.bridje.jfx.utils.JfxUtils;
import org.bridje.web.srcgen.models.AssetModel;
import org.bridje.web.srcgen.models.AssetModelTable;
import org.bridje.web.srcgen.models.ResourceModel;
import org.bridje.web.srcgen.models.UISuiteModel;
import org.bridje.web.srcgen.models.UISuitesModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public final class ResourceEditor extends GridPane
{
    private final SimpleObjectProperty<ResourceModel> resourceProperty = new SimpleObjectProperty<>();

    private final SimpleObjectProperty<UISuiteModel> uiSuiteProperty = new SimpleObjectProperty<>();

    private final TextField tfName = new TextField();

    private AssetModelTable tbAssets = new AssetModelTable();

    private final HBox tbActions = new HBox();

    public ResourceEditor()
    {
        setVgap(10);
        setHgap(10);

        setPadding(new Insets(10));

        tbAssets.addTypeColumn("Type");
        tbAssets.editableTypeColumn(typeEditor(), null);
        tbAssets.addHrefColumn("Href");
        tbAssets.editableHrefColumn(null);
        tbAssets.addRelColumn("Rel");
        tbAssets.editableRelColumn(null);
        tbAssets.addSizesColumn("Sizes");
        tbAssets.editableSizesColumn(null);

        tbActions.getChildren().add(JfxUtils.createToolButton(UISuitesModel.add(32), this::addAsset));
        tbActions.getChildren().add(JfxUtils.createToolButton(UISuitesModel.resource(32), this::parseAssets));
        tbActions.getChildren().add(JfxUtils.createToolButton(UISuitesModel.delete(32), this::deleteAsset));

        add(tfName, 0, 0);
        add(tbActions, 0, 1);
        add(tbAssets, 0, 2);

        setFillWidth(tfName, true);
        setHgrow(tfName, Priority.ALWAYS);
        setFillWidth(tbAssets, true);
        setHgrow(tbAssets, Priority.ALWAYS);
        setFillHeight(tbAssets, true);
        setVgrow(tbAssets, Priority.ALWAYS);

        resourceProperty.addListener((observable, oldValue, newValue) ->
        {
            if(oldValue != null)
            {
                tfName.textProperty().unbindBidirectional(oldValue.nameProperty());
                Bindings.unbindContentBidirectional(tbAssets.getItems(), oldValue.getContent());
            }
            if(newValue != null)
            {
                tfName.textProperty().bindBidirectional(newValue.nameProperty());
                Bindings.bindContentBidirectional(tbAssets.getItems(), newValue.getContent());
            }
        });
    }

    public SimpleObjectProperty<ResourceModel> controlsProperty()
    {
        return this.resourceProperty;
    }

    public ResourceModel getResource()
    {
        return this.resourceProperty.get();
    }

    public void setResource(ResourceModel control)
    {
        this.resourceProperty.set(control);
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

    public void addAsset(ActionEvent event)
    {
        AssetModel asset = new AssetModel();
        asset.setType("style");
        asset.setHref("http://somelink.com");
        asset.setParent(getResource());
        getResource().getContent().add(asset);
    }

    public void parseAssets(ActionEvent event)
    {
        String html = inputHtml();
        if(html != null && !html.isEmpty())
        {
            parseHtml(html);
        }
    }

    public void deleteAsset(ActionEvent event)
    {
        if(getResource() != null)
        {
            AssetModel asset = tbAssets.getSelectionModel().getSelectedItem();
            if(asset != null)
            {
                getResource().getContent().remove(asset);
            }
        }
    }

    private Callback<TableColumn<AssetModel, String>, TableCell<AssetModel, String>> typeEditor()
    {
        return ComboBoxTableCell.forTableColumn("style", "script", "link");
    }

    private String inputHtml()
    {
        InputHtmlDialog dialog = new InputHtmlDialog();
        return dialog.showAndWait().orElse(null);
    }

    private void parseHtml(String html)
    {
        Document doc = Jsoup.parse(html);
        Elements links = doc.select("link");
        for (Element link : links)
        {
            if("stylesheet".equals(link.attr("rel")))
            {
                System.out.println(link.attr("href"));
            }
            else
            {
                System.out.println(link.attr("rel"));
                System.out.println(link.attr("href"));
                System.out.println(link.attr("sizes"));
            }
        }
        Elements styles = doc.select("style");
        for (Element style : styles)
        {
            System.out.println(style.attr("src"));
        }
        Elements scripts = doc.select("script");
        for (Element script : scripts)
        {
            System.out.println(script.attr("src"));
        }
    }
}
