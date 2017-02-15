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
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;
import org.bridje.orm.srcgen.OrmSourceGenerator;
import org.bridje.srcgen.impl.edit.EditableItem;

public class OrmSrcGenTreeItem extends TreeItem<Object> implements EditableItem
{
    private static final SimpleNavEditor EDITOR = new SimpleNavEditor();

    private final OrmSourceGenerator srcGen;
    
    public OrmSrcGenTreeItem(OrmSourceGenerator srcGen)
    {
        super(srcGen, Utils.createImageView(OrmSrcGenTreeItem.class, "database.png"));
        this.srcGen = srcGen;
        setExpanded(true);
        getChildren()
                .addAll(srcGen.findData().entrySet()
                .stream()
                .map(f -> new ModelInfTreeItem(f.getKey(), f.getValue()))
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
        EDITOR.setTitle(getValue().toString());
    }

    @Override
    public void commit()
    {
    }
}
