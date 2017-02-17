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

package org.bridje.srcgen.impl.edit;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SrcEditorApplication extends Application
{
    private static SrcEditorApplication app;
    
    private static Stage stage;

    public static synchronized void showDataEditor()
    {
        if(app == null)
        {
            Application.launch(SrcEditorApplication.class);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        app = this;
        stage = primaryStage;
        stage.setHeight(600);
        stage.setWidth(800);
        Scene scene = new Scene(new SrcEditorMainPane());
        stage.setTitle("Bridje Source Generation Data Editor");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("generator.png")));
        stage.show();
    }
}
