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

package org.bridje.maven.plugin;

import freemarker.ext.dom.NodeModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.maven.plugin.MojoExecutionException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A file to generate.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class WriteFile
{
    private String name;

    private String template;

    private String script;
    
    @XmlElementWrapper(name = "resources")
    @XmlElements(
    {
        @XmlElement(name = "resource", type = String.class)
    })
    private List<String> resources;

    /**
     * The name of the file to generate, it can contain freemarker expressions.
     *
     * @return The file name template in freemarker.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The path to the template to be use to generate this file.
     *
     * @return An String representing the relative path to the template to be
     * use in the generation.
     */
    public String getTemplate()
    {
        return template;
    }

    /**
     * A list of resource files that must be loaded to generate this file.
     * 
     * @return A list of resource files that must be loaded to generate this file.
     */
    public List<String> getResources()
    {
        return resources;
    }

    /**
     * 
     * @return 
     */
    public String getScript()
    {
        return script;
    }

    /**
     * Generates the files described by this data file.
     *
     * @param mojo The GenerateMojo instance.
     * @param node The current XML node.
     * @param doc The XML document.
     * @throws MojoExecutionException If any exception occurs.
     */
    protected void generate(GenerateMojo mojo, Node node, Document doc) throws MojoExecutionException
    {
        try
        {
            NodeModel wrap = NodeModel.wrap(node);
            NodeModel docWrap = NodeModel.wrap(doc);
            Map model = new HashMap();
            model.put("doc", docWrap);
            model.put("node", wrap);
            model.put("resources", loadResources(mojo));
            
            Map scModel = new HashMap();
            scModel.put("docInput", nodeToInputStream(doc));
            scModel.put("nodeInput", nodeToInputStream(node));
            Object data = processScript(mojo.findScript(script), scModel);
            model.put("data", data);
            Configuration cfg = mojo.getFreeMarkerConfiguration();

            //File name
            Template fileNameTpl = new Template(name, new StringReader(name), cfg);
            StringWriter out = new StringWriter();
            fileNameTpl.process(model, out);
            String fileName = out.toString();

            //Generate file
            File toGenerate = new File(mojo.getOutputBasePath() + "/" + fileName);
            if (toGenerate.exists())
            {
                toGenerate.delete();
            }
            toGenerate.getAbsoluteFile().getParentFile().mkdirs();
            toGenerate.createNewFile();
            Template tmpl = cfg.getTemplate(template);
            tmpl.process(model, new OutputStreamWriter(new FileOutputStream(toGenerate), "UTF-8"));
        }
        catch (TransformerException | ScriptException | TemplateException | IOException ex)
        {
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
    }

    private List<NodeModel> loadResources(GenerateMojo mojo)
    {
        List<NodeModel> models = new LinkedList<>();
        for (String resource : resources)
        {
            try
            {
                Enumeration<URL> urls = mojo.findResources(resource);
                if(urls != null)
                {
                    models.addAll(parseAll(urls));
                }
            }
            catch (Exception e)
            {
                mojo.getLog().error(e.getMessage(), e);
            }
        }
        return models;
    }

    private Collection<? extends NodeModel> parseAll(Enumeration<URL> urls) throws IOException, SAXException, ParserConfigurationException
    {
        List<NodeModel> result = new ArrayList<>();
        while (urls.hasMoreElements())
        {
            URL nextElement = urls.nextElement();
            try(InputStream is = nextElement.openStream())
            {
                result.add(NodeModel.parse(new InputSource(is)));
            }
        }
        return result;
    }
    
    private Object processScript(Reader script, Map<String, Object> params) throws ScriptException
    {
        ScriptEngineManager factory = new ScriptEngineManager();

        ScriptEngine engine = factory.getEngineByName("groovy");
        Bindings bindings = engine.createBindings();

        bindings.putAll(params);

        engine.put("result", new Object());
        return engine.eval(script, bindings);
    }

    private InputStream nodeToInputStream(Node node) throws TransformerException
    {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Result outputTarget = new StreamResult(outputStream);
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.transform(new DOMSource(node), outputTarget);
            return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
