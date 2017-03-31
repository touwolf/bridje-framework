
package org.bridje.jfx.srcgen;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.jfx.srcgen.model.ModelInf;
import org.bridje.jfx.srcgen.model.ObjectInf;
import org.bridje.srcgen.SourceGenerator;
import org.bridje.srcgen.SrcGenService;
import org.bridje.vfs.VFile;

@Component
public class JFxSourceGenerator implements SourceGenerator<ModelInf>
{
    private static final Logger LOG = Logger.getLogger(JFxSourceGenerator.class.getName());

    @Inject
    private SrcGenService srcGen;

    @Override
    public Map<ModelInf, VFile> findData()
    {
        try
        {
            return srcGen.findData(ModelInf.class);
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public void generateSources(ModelInf modelInf, VFile file) throws IOException
    {
        Map<String, Object> data = new HashMap<>();
        data.put("model", modelInf);
        srcGen.createClass(modelInf.getFullName(), "jfx/Model.ftl", data);

        data = new HashMap<>();
        data.put("model", modelInf);
        for (ObjectInf objectInf : modelInf.getObjects())
        {
            data.put("object", objectInf);
            srcGen.createClass(objectInf.getFullName(), "jfx/Object.ftl", data);

            data.put("object", objectInf);
            srcGen.createClass(objectInf.getFullName() + "Table", "jfx/Table.ftl", data);
        }
    }

}
