
package org.bridje.jfx.srcgen;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TreeItem;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.jfx.srcgen.model.JFxComponent;
import org.bridje.jfx.srcgen.model.ModelInf;
import org.bridje.jfx.srcgen.model.ObjectInf;
import org.bridje.srcgen.SourceGenerator;
import org.bridje.srcgen.SrcGenService;
import org.bridje.vfs.VFile;

@Component
class JFxSourceGenerator implements SourceGenerator<ModelInf>
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
        if(modelInf.getObjects() != null)
        {
            for (ObjectInf objectInf : modelInf.getObjects())
            {
                data.put("object", objectInf);
                srcGen.createClass(objectInf.getFullName(), "jfx/Object.ftl", data);

                List<JFxComponent> components = objectInf.getComponents();
                if(objectInf.getComponents() != null)
                {
                    for (JFxComponent component : components)
                    {
                        data.put("object", objectInf);
                        data.put("component", component);
                        srcGen.createClass(objectInf.getFullName() + component.getType(), "jfx/" + component.getType() + ".ftl", data);
                    }
                }
            }
        }
    }

    @Override
    public TreeItem<Object> createTreeItem(VFile file)
    {
        return null;
    }
}
