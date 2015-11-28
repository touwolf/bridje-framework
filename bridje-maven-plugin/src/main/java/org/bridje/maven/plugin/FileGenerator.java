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
package org.bridje.maven.plugin;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for files generation with freemarker.
 * <p>
 * <b>EXAMPLE</b>
 * <p>
 * <code>
 * <pre>FileGenerator generator = new FileGenerator(new File("myDir"), true);
 * generator.generateFile(new GenerateFileData(new HashMap(), "sometpl.ftl", "MyClass.java"));</pre>
 * </code>
 */
public class FileGenerator
{

    private final File targetDir;

    private final boolean override;

    public FileGenerator(File targetDir, boolean override)
    {
        this.targetDir = targetDir;
        this.override = override;
    }

    public void generateFile(GenerateFileData toGenerate) throws FileNotFoundException, TemplateException, IOException
    {
        Configuration cfg = new Configuration(Configuration.getVersion());
        cfg.setClassForTemplateLoading(getClass(), "/META-INF/templates");
        Template template = cfg.getTemplate(toGenerate.getTplName());
        File fileToGenerate = new File(targetDir.getPath() + "/" + toGenerate.getDest());
        if (fileToGenerate.exists())
        {
            if (!override)
            {
                return;
            }
            if (!fileToGenerate.delete())
            {
                throw new IOException(String.format("No se pudo eliminar el archivo \"%s\" existente", fileToGenerate.getAbsoluteFile()));
            }
        }
        else if (!fileToGenerate.getParentFile().exists())
        {
            if (!fileToGenerate.getParentFile().getAbsoluteFile().mkdirs())
            {
                throw new IOException(String.format("No se pudo crear la carpeta del archivo \"%s\"", fileToGenerate.getAbsoluteFile()));
            }
        }
        try (OutputStreamWriter streamWriter = new OutputStreamWriter(new FileOutputStream(fileToGenerate), StandardCharsets.UTF_8))
        {
            template.process(toGenerate.getData(), streamWriter);
        }
    }

}
