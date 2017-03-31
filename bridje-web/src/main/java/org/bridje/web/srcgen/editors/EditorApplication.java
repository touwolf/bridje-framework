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
import java.io.FileInputStream;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.bridje.web.srcgen.models.ControlDefModel;
import org.bridje.web.srcgen.models.FieldDefModel;
import org.bridje.web.srcgen.models.ResourceRefModel;
import org.bridje.web.srcgen.models.UISuiteModel;
import org.bridje.web.srcgen.uisuite.ChildField;
import org.bridje.web.srcgen.uisuite.ChildrenFlield;
import org.bridje.web.srcgen.uisuite.ControlDef;
import org.bridje.web.srcgen.uisuite.FieldDef;
import org.bridje.web.srcgen.uisuite.ResourceRef;
import org.bridje.web.srcgen.uisuite.UISuite;

public class EditorApplication extends Application
{
    @Override
    public void start(Stage stage) throws Exception
    {
        UISuite suite = UISuite.load(new FileInputStream(new File("Himu.xml")));
        UISuiteModel model = createUISuite(suite);
        
        ControlEditor editor = new ControlEditor();
        editor.setUISuite(model);
        editor.setControl(model.getControls().get(0));
        Scene scene = new Scene(editor);
        stage.setScene(scene);
        stage.show();
    }

    private UISuiteModel createUISuite(UISuite suiteModel)
    {
        UISuiteModel model = new UISuiteModel();
        model.setControls(FXCollections.observableArrayList());
        suiteModel.getControls().forEach(c -> model.getControls().add(createControl(c)));
        return model;
    }

    private ControlDefModel createControl(ControlDef control)
    {
        ControlDefModel model = new ControlDefModel();
        model.setName(control.getName());
        model.setBase(control.getBase());
        model.setRender(control.getRender());
        if(control.getBaseTemplate() != null) model.setBaseTemplate(control.getBaseTemplate().getName());
        model.setFields(FXCollections.observableArrayList());
        control.getFields().forEach(f -> model.getFields().add(createField(f)));
        model.setResources(FXCollections.observableArrayList());
        control.getResources().forEach(f -> model.getResources().add(createResource(f)));
        return model;
    }

    private FieldDefModel createField(FieldDef fieldDef)
    {
        FieldDefModel result = new FieldDefModel();
        result.setName(fieldDef.getName());
        result.setChildType(createChildType(fieldDef));
        return result;
    }

    private ResourceRefModel createResource(ResourceRef resourceRef)
    {
        ResourceRefModel result = new ResourceRefModel();
        result.setName(resourceRef.getName());
        return result;
    }

    private String createChildType(FieldDef fieldDef)
    {
        if(fieldDef instanceof ChildField) return "child";
        if(fieldDef instanceof ChildrenFlield) return "children";
        return null;
    }
}
