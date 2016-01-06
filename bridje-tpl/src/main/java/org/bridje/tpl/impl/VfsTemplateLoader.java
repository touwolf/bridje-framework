/*
 * Copyright 2016 Bridje Framework.
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

package org.bridje.tpl.impl;

import java.io.IOException;
import java.io.InputStream;
import org.bridje.ioc.Ioc;
import org.bridje.tpl.TemplateLoader;
import org.bridje.vfs.Path;
import org.bridje.vfs.VfsService;
import org.bridje.vfs.VirtualFile;

public class VfsTemplateLoader implements TemplateLoader
{
    private final Path basePath;

    public VfsTemplateLoader(String basePath)
    {
        this.basePath = new Path(basePath);
    }
        
    @Override
    public InputStream loadTemplate(Object template) throws IOException
    {
        VirtualFile file = (VirtualFile)template;
        if(file != null)
        {
            return file.open();
        }
        return null;
    }

    @Override
    public Object findTemplate(String path)
    {
        return Ioc.context().find(VfsService.class).findFile(basePath.join(path));
    }
    
}
