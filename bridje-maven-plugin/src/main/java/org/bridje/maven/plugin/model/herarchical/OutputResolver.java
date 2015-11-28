/*
 * Copyright 2015 Bridje Framework.
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
package org.bridje.maven.plugin.model.herarchical;

import java.io.File;
import java.io.IOException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

/**
 * Utility class to generate the xsd schema file for this model.
 */
public class OutputResolver extends SchemaOutputResolver
{
    private final File file;

    public OutputResolver(File file)
    {
        this.file = file;
    }

    @Override
    public Result createOutput(String nsUri, String fileName) throws IOException
    {
        if (file.exists())
        {
            file.delete();
        }
        
        file.createNewFile();

        return new StreamResult(file);
    }
}
