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

package org.bridje.orm.srcgen.edit;

import java.util.stream.Collectors;
import javafx.scene.layout.Pane;
import org.bridje.orm.srcgen.model.ModelInf;
import org.bridje.vfs.VFile;

public class ModelInfTreeItem extends TreeItemBase
{
    private static final ModelInfEditor EDITOR = new ModelInfEditor();

    public ModelInfTreeItem(ModelInf modelInf, VFile file)
    {
        super(modelInf, modelInf, file, Utils.createImageView(ModelInfEditor.class, "database.png"));
        getChildren()
                .addAll(modelInf.getTemplates()
                .stream()
                .map(f -> new TemplateInfTreeItem(this, f))
                .collect(Collectors.toList()));
        getChildren()
                .addAll(modelInf.getEntities()
                .stream()
                .map(f -> new EntityInfTreeItem(this, f))
                .collect(Collectors.toList()));
        getChildren()
                .addAll(modelInf.getEnums()
                .stream()
                .map(f -> new EnumInfTreeItem(f))
                .collect(Collectors.toList()));
    }

    @Override
    public Pane getEditor()
    {
        return EDITOR;
    }

    @Override
    public void startEdit()
    {
        EDITOR.setModel(getModel());
    }

    @Override
    public void commit()
    {
        EDITOR.updateModel();
        saveModel();
    }
}
