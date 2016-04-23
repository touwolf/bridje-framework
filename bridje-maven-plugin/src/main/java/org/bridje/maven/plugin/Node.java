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

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.maven.plugin.MojoExecutionException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * An xml xpath expresion with represents the dom nodes that will be taken for
 * the source code generation in a data file.
 */
public class Node
{
    private String expression;

    private WriteFile[] files;

    /**
     * The xpath expresion for this node configuration.
     *
     * @return An string representing the actual xpath to apply to find the
     * nodes for the source code generation.
     */
    public String getExpression()
    {
        return expression;
    }

    /**
     * The files to generate for each node.
     *
     * @return An array of files that will be generated for each node that the
     * xpath expresion returns from the data file.
     */
    public WriteFile[] getFiles()
    {
        return files;
    }

    protected void generate(GenerateMojo mojo, Document doc) throws MojoExecutionException
    {
        try
        {
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodes = (NodeList) xPath.evaluate(expression,
                    doc.getDocumentElement(), XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); ++i)
            {
                org.w3c.dom.Node node = nodes.item(i);
                for (WriteFile file : files)
                {
                    file.generate(mojo, node, doc);
                }
            }
        }
        catch (XPathExpressionException ex)
        {
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
    }
}
