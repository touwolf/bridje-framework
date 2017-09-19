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

package org.bridje.web.srcgen.editor;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import org.bridje.jfx.binding.BiContentConverter;
import org.bridje.jfx.binding.ExBindings;

public final class FtlImportsTreeItem extends TreeItem<Object>
{
    private final ObservableList<FtlImportModel> imports;

    public FtlImportsTreeItem(ObservableList<FtlImportModel> imports)
    {
        super("FTL Imports", UISuitesModel.ftlImport(16));
        this.imports = imports;
        ExBindings.bindContentBidirectional(getChildren(), imports, new BiContentConverter<TreeItem<Object>, FtlImportModel>()
        {
            @Override
            public FtlImportModel convertFrom(TreeItem<Object> value)
            {
                return ((FtlImportTreeItem)value).getFtlImport();
            }

            @Override
            public TreeItem<Object> convertTo(FtlImportModel value)
            {
                return new FtlImportTreeItem(value, UISuitesModel.ftlImport(16));
            }
        });
    }
}
