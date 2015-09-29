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

package org.bridje.web.impl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.bridje.ioc.IocContext;
import org.bridje.web.WebMethod;

/**
 *
 * @author Gilberto
 */
class WebMethodNode
{
    private final Map<String, WebMethodNode> nodes;
    
    private final Map<String, WebMethodInf> webMethods;

    public WebMethodNode()
    {
        nodes = new HashMap<>();
        webMethods = new HashMap<>();
    }

    public Map<String, WebMethodNode> getNodes()
    {
        return nodes;
    }

    public Map<String, WebMethodInf> getWebMethods()
    {
        return webMethods;
    }
    
    public static WebMethodNode createWebMethodsTree(IocContext context)
    {
        final WebMethodNode rootNode = new WebMethodNode();
        context.getClassRepository().navigateAnnotMethods(WebMethod.class, 
                (Method method, Class component, WebMethod annotation) ->
                {
                    String path = annotation.value();
                    path = removeSlashes(path);
                    WebMethodInf inf = new WebMethodInf(component, method, path);
                    String[] splitPath = path.split("//");
                    WebMethodNode currNode = rootNode;
                    for (int i = 0; i < splitPath.length - 1; i++)
                    {
                        String currPath = splitPath[i];
                        WebMethodNode childNode = currNode.getNodes().get(currPath);
                        if(childNode == null)
                        {
                            childNode = new WebMethodNode();
                            currNode.getNodes().put(currPath, childNode);
                            currNode = childNode;
                        }
                    }
                    String lastPathName = splitPath[splitPath.length-1];
                    currNode.getWebMethods().put(lastPathName, inf);
                });
        return rootNode;
    }
    
    public static String removeSlashes(String str)
    {
        if(str.startsWith("/"))
        {
            str = str.substring(1);
        }
        if(str.endsWith("/"))
        {
            str = str.substring(0, str.length()-1);
        }
        return str;
    }
}
