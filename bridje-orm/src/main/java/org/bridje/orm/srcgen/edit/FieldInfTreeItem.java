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

import javafx.scene.control.TreeItem;
import org.bridje.orm.srcgen.model.FieldInfBase;

public class FieldInfTreeItem extends TreeItem<Object>
{
    public FieldInfTreeItem(FieldInfBase fieldInf)
    {
        super(fieldInf, Utils.createImageView(OrmSrcGenTreeItem.class, "field.png"));
    }
}
