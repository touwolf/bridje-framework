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

package org.bridje.srcgen.editor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class EditorApplication extends Application
{
    @Override
    public void start(Stage stage) throws Exception
    {
        EditorMainPane mainPane = new EditorMainPane();
        Scene scene = new Scene(mainPane);
        stage.setScene(scene);
        stage.setWidth(1024);
        stage.setHeight(768);
        stage.setTitle("Source Generation Data Editor");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("generator.png")));
        stage.show();
    }
}
