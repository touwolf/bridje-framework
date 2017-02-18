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
import org.bridje.orm.srcgen.model.BooleanFieldInf;
import org.bridje.orm.srcgen.model.CustomFieldInf;
import org.bridje.orm.srcgen.model.DateTimeFieldInf;
import org.bridje.orm.srcgen.model.DecimalFieldInf;
import org.bridje.orm.srcgen.model.EnumFieldInf;
import org.bridje.orm.srcgen.model.FieldInfBase;
import org.bridje.orm.srcgen.model.IntegerFieldInf;
import org.bridje.orm.srcgen.model.RelationFieldInf;
import org.bridje.orm.srcgen.model.StringFieldInf;

public class FieldInfTreeItem extends TreeItemBase<FieldInfBase>
{
    private static final FieldInfEditor EDITOR = new FieldInfEditor();
    
    public FieldInfTreeItem(ModelInfTreeItem modelItem, FieldInfBase fieldInf)
    {
        super(fieldInf, modelItem.getModel(), modelItem.getFile(), Utils.createImageView(EntityInfTreeItem.class, findFieldIcon(fieldInf)));
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

    private static String findFieldIcon(FieldInfBase fieldInf)
    {
        if(fieldInf instanceof IntegerFieldInf)
        {
            return "integer-field.png";
        }
        else if(fieldInf instanceof DecimalFieldInf)
        {
            return "decimal-field.png";
        }
        else if(fieldInf instanceof EnumFieldInf)
        {
            return "enumeration.png";
        }
        else if(fieldInf instanceof DateTimeFieldInf)
        {
            return "date-field.png";
        }
        else if(fieldInf instanceof CustomFieldInf)
        {
            return "custom-field.png";
        }
        else if(fieldInf instanceof BooleanFieldInf)
        {
            return "boolean-field.png";
        }
        else if(fieldInf instanceof StringFieldInf)
        {
            return "string-field.png";
        }
        else if(fieldInf instanceof RelationFieldInf)
        {
            return "relation-field.png";
        }
        return "field.png";
    }
}
