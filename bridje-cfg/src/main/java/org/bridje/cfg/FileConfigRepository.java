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

package org.bridje.cfg;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 *
 */
public abstract class FileConfigRepository implements ConfigRepository
{
    private final File parentFile;

    public FileConfigRepository(File parentFile)
    {
        this.parentFile = parentFile;
        if(parentFile.exists() && !parentFile.isDirectory())
        {
            throw new IllegalArgumentException("The parent file for the repository is not a directory.");
        }
        parentFile.mkdirs();
    }

    @Override
    public Boolean handleContext(String context)
    {
        if(context == null || context.isEmpty())
        {
            return true;
        }
        File f = new File(parentFile.getAbsolutePath() + File.separator + context);
        return (f.exists() && f.isDirectory());
    }

    @Override
    public Reader findConfig(String configName) throws IOException
    {
        File f = new File(parentFile.getAbsolutePath() + File.separator + configName);
        if(!f.exists())
        {
            return null;
        }
        return new FileReader(f);
    }

    @Override
    public Writer saveConfig(String configName) throws IOException
    {
        File f = new File(parentFile.getAbsolutePath() + File.separator + configName);
        if(!f.exists())
        {
            f.createNewFile();
        }
        return new FileWriter(f);
    }

    @Override
    public boolean canSave()
    {
        return true;
    }

    protected File getParentFile()
    {
        return parentFile;
    }
}
