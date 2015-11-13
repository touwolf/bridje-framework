package org.bridje.maven.plugin.model.herarchical;

import java.io.File;
import java.io.IOException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

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
