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
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.bridje.ioc.Ioc;
import org.bridje.vfs.FileSource;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFileInputStream;
import org.bridje.vfs.VfsService;
import org.bridje.web.srcgen.models.UISuiteModel;
import org.bridje.web.srcgen.models.UISuitesModel;
import org.bridje.web.srcgen.uisuite.UISuite;

public class UISuitesEditorApplication extends Application
{
    @Override
    public void start(Stage stage) throws Exception
    {
        Ioc.context().find(VfsService.class).mount(new Path("/data"), new FileSource(new File(".")));
        UISuite suite = UISuite.load(new VFileInputStream(new VFile("/data/Himu.xml")));
        UISuiteConverter converter = new UISuiteConverter();
        UISuiteModel model = converter.toModel(suite);
        model.setFile(new VFile("/data/Himu.xml"));
        UISuitesModel suites = new UISuitesModel();
        suites.setSuites(FXCollections.observableArrayList());
        suites.getSuites().add(model);
        
        UISuitesEditor editor = new UISuitesEditor();
        editor.setSuites(suites);
        Scene scene = new Scene(editor);
        stage.setScene(scene);
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
    }
}
