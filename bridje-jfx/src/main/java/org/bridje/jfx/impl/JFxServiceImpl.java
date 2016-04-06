/*
 * Copyright 2016 Bridje Framework.
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

package org.bridje.jfx.impl;

import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.bridje.ioc.Component;
import org.bridje.ioc.Ioc;
import org.bridje.jfx.JFxService;
import org.bridje.jfx.PanePosition;

/**
 *
 */
@Component
class JFxServiceImpl implements JFxService
{
    private String title;
    
    private LayoutPane mainPane;
    
    @Override
    public void start(Stage stage, String title)
    {
        this.title = title;
        mainPane = Ioc.context().find(LayoutPane.class);
        Scene scene = new Scene(mainPane);
        scene.getStylesheets().add("org/bridje/jfx/impl/default.css");
        stage.setWidth(1024);
        stage.setHeight(768);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    @Override
    public String getTitle()
    {
        return this.title;
    }

    @Override
    public void addPane(String title, Pane pane, PanePosition position)
    {
        mainPane.addPane(title, pane, position);
    }
}
