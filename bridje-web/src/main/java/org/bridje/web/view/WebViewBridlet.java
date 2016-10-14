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

import java.io.IOException;
import javax.xml.bind.annotation.XmlTransient;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;
import org.bridje.http.HttpBridletContext;
import org.bridje.http.HttpBridlet;
import org.bridje.http.HttpException;

@Component
@Priority(200)
@XmlTransient
class WebViewBridlet implements HttpBridlet
{
    @Inject
    private WebViewsManager viewsMang;
    
    @InjectNext
    private HttpBridlet nextHandler;

    @Override
    public boolean handle(HttpBridletContext context) throws IOException, HttpException
    {
        WebView view = viewsMang.findUpdateView(context);
        if(view != null)
        {
            viewsMang.updateView(view, context);
            return true;
        }

        if(nextHandler.handle(context))
        {
            return true;
        }

        view = viewsMang.findView(context);
        if(view != null)
        {
            viewsMang.renderView(view, context);
            return true;
        }

        return false;
    }
}
