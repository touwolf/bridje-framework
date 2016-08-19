
package org.bridje.vfs.impl.adapters;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import org.bridje.ioc.Component;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFileAdapter;

@Component
class JaxbXmlAdapter implements VFileAdapter
{
    private static final Logger LOG = Logger.getLogger(JaxbXmlAdapter.class.getName());

    @Override
    public String[] getExtensions()
    {
        return new String[]{"xml"};
    }

    @Override
    public Class<?>[] getClasses()
    {
        return null;
    }

    @Override
    public boolean canHandle(VFile vf, Class<?> resultCls)
    {
        return (resultCls.getAnnotation(XmlRootElement.class) != null);
    }

    @Override
    public <T> T read(VFile vf, Class<T> resultCls) throws IOException
    {
        try(InputStream is = vf.openForRead())
        {
            JAXBContext ctx = JAXBContext.newInstance(resultCls);
            Unmarshaller unm = ctx.createUnmarshaller();
            Object unmarsh = unm.unmarshal(is);
            return (T)unmarsh;
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public <T> void write(VFile vf, T contentObj) throws IOException
    {
        if(vf.canOpenForWrite())
        {
            try(OutputStream os = vf.openForWrite())
            {
                JAXBContext ctx = JAXBContext.newInstance(contentObj.getClass());
                Marshaller unm = ctx.createMarshaller();
                unm.marshal(contentObj, os);
            }
            catch (Exception e)
            {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        else
        {
            throw new IOException("Cannot open the file for writing.");
        }
    }
}
