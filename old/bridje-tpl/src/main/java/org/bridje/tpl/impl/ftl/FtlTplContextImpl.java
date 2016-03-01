
package org.bridje.tpl.impl.ftl;

import freemarker.cache.TemplateLoader;
import freemarker.core.Environment;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.tls.Tls;
import org.bridje.tpl.TplEngineContext;
import org.bridje.tpl.TplNotFoundException;
import org.bridje.tpl.TplParserException;

final class FtlTplContextImpl implements TplEngineContext
{
    private static final Logger LOG = Logger.getLogger(FtlTplContextImpl.class.getName());

    private final FtlCtxImplExceptionHandler exceptionHandler;

    private final Configuration cf;

    public FtlTplContextImpl(org.bridje.tpl.TemplateLoader loader)
    {
        TemplateLoader ftlLoader = new FtlVfsTemplateLoader(loader);

        exceptionHandler = new FtlCtxImplExceptionHandler(isErrorIgnored());
        cf = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        cf.setTemplateLoader(ftlLoader);
        cf.setTemplateExceptionHandler(exceptionHandler);

        System.setProperty(freemarker.log.Logger.SYSTEM_PROPERTY_NAME_LOGGER_LIBRARY, freemarker.log.Logger.LIBRARY_NAME_JUL);
    }

    @Override
    public void render(String template, Map data, Writer writer) throws TplNotFoundException, IOException
    {
        try
        {
            Template tmpl = findTemplate(template);
            tmpl.process(data, writer);
        }
        catch (TemplateException | TplParserException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Override
    public String render(String template, Map data) throws TplNotFoundException
    {
        StringWriter writer = null;
        try
        {
            writer = new StringWriter();
            Template tmpl = findTemplate(template);
            tmpl.process(data, writer);
            return writer.toString();
        }
        catch (IOException | TemplateException | TplParserException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        finally
        {
            if (writer != null)
            {
                try
                {
                    writer.close();
                }
                catch (IOException ex)
                {
                    LOG.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        }
        return "";
    }

    @Override
    public void render(String template, Map data, File file) throws TplNotFoundException, IOException
    {
        try (FileWriter fw = new FileWriter(file))
        {
            render(template, data, fw);
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Override
    public void render(String template, Map data, OutputStream os) throws TplNotFoundException, IOException
    {
        render(template, data, new OutputStreamWriter(os, "UTF-8"));
    }

    @Override
    public boolean exists(String template)
    {
        try
        {
            Template tmp = findTemplate(template);
            return tmp != null;
        }
        catch (TplNotFoundException | TplParserException ex)
        {
            return false;
        }
    }

    protected Template findTemplate(String template) throws TplNotFoundException, TplParserException
    {
        try
        {
            Locale locale = Tls.get(Locale.class);
            if (locale != null)
            {
                return cf.getTemplate(template, locale);
            }

            return cf.getTemplate(template, Locale.getDefault());
        }
        catch (ParseException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            throw new TplParserException(template, ex);
        }
        catch (IOException e)
        {
            throw new TplNotFoundException(template);
        }
    }

    protected boolean isErrorIgnored()
    {
        return true;
    }

    private static class FtlCtxImplExceptionHandler implements TemplateExceptionHandler
    {
        private final boolean errIgnored;

        public FtlCtxImplExceptionHandler(boolean errIgnored)
        {
            this.errIgnored = errIgnored;
        }

        @Override
        public void handleTemplateException(TemplateException te, Environment env, java.io.Writer out) throws TemplateException
        {
            try
            {
                if (!errIgnored)
                {
                    out.write("[ERROR: " + te.getMessage() + "]");
                }
            }
            catch (IOException e)
            {
                throw new TemplateException("Failed to print error message. Cause: " + e.getMessage(), env);
            }
        }
    }
}
