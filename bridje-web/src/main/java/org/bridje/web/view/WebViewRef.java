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

package org.bridje.web.view;

import javax.xml.bind.annotation.XmlTransient;

/**
 * This object represents a reference to the web view to be rendered by the
 * current http request. Bridlets may override this object in the current web
 * context to change the current web view that will be rendered to the client.
 */
@XmlTransient
public class WebViewRef
{
    private String viewPath;

    /**
     * Constructor with the view path.
     * 
     * @param viewPath The path for the view.
     */
    public WebViewRef(String viewPath)
    {
        setViewPath(viewPath);
    }

    /**
     * The path for the web view.
     * 
     * @return The path for the web view.
     */
    public String getViewPath()
    {
        return viewPath;
    }

    /**
     * The path for the web view.
     * 
     * @param viewPath The path for the web view.
     */
    public void setViewPath(String viewPath)
    {
        this.viewPath = viewPath;
        if(this.viewPath == null || this.viewPath.trim().isEmpty())
        {
            this.viewPath = "/index";
        }
        if(!this.viewPath.startsWith("/"))
        {
            this.viewPath = "/" + this.viewPath;
        }
    }
}
