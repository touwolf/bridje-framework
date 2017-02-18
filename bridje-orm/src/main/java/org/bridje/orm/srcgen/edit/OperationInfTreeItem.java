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

import javafx.scene.layout.Pane;
import org.bridje.orm.srcgen.model.OperationInfBase;

public class OperationInfTreeItem extends TreeItemBase<OperationInfBase>
{
    private static final OperationInfEditor EDITOR = new OperationInfEditor();
    
    public OperationInfTreeItem(ModelInfTreeItem modelItem, OperationInfBase fieldInf)
    {
        super(fieldInf, modelItem.getModel(), modelItem.getFile(), Utils.createImageView(OperationInfTreeItem.class, "operations.png"));
    }

    @Override
    public Pane getEditor()
    {
        return EDITOR;
    }

    @Override
    public void startEdit()
    {
        EDITOR.setModel(getData());
    }

    @Override
    public void commit()
    {
        saveModel();
    }
}
