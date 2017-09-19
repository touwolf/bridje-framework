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

import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import org.bridje.ioc.Ioc;
import org.bridje.jfx.utils.JfxUtils;
import org.bridje.srcgen.SourceGenerator;
import org.bridje.vfs.VFile;

public class FolderTreeItem extends TreeItem<Object>
{
    private final VFile file;

    public FolderTreeItem(VFile value)
    {
        super(value.getName(), folder(16));
        this.file = value;
        VFile[] files = this.file.listFiles();
        for (VFile child : files)
        {
            if(child.isDirectory()) 
            {
                getChildren().add(new FolderTreeItem(child));
            }
        }
        for (VFile child : files)
        {
            if(child.isFile())
            {
                SourceGenerator[] sources = Ioc.context().findAll(SourceGenerator.class);
                for (SourceGenerator source : sources)
                {
                    TreeItem<Object> item = source.createTreeItem(child);
                    if(item != null)
                    {
                        getChildren().add(item);
                        break;
                    }
                }
            }
        }
    }

    public static ImageView folder(int size)
    {
        return JfxUtils.loadImage(FolderTreeItem.class, "folder.png", size, size);
    }

    public VFile getFile()
    {
        return file;
    }
}
