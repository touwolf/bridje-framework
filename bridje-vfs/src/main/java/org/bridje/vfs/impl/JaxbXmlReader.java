
package org.bridje.vfs.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import org.bridje.ioc.Component;
import org.bridje.vfs.VirtualFile;
import org.bridje.vfs.VirtualFileReader;

@Component
class JaxbXmlReader implements VirtualFileReader
{
    private static final Logger LOG = Logger.getLogger(JaxbXmlReader.class.getName());

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
    public boolean canRead(VirtualFile vf, Class<?> resultCls)
    {
        return (resultCls.getAnnotation(XmlRootElement.class) != null);
    }

    @Override
    public <T> T read(VirtualFile vf, Class<T> resultCls) throws IOException
    {
        try(InputStream is = vf.open())
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
}
