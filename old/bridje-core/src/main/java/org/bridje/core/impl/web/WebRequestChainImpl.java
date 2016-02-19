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

package org.bridje.core.impl.web;

import org.bridje.ioc.IocContext;
import org.bridje.core.web.HttpException;
import org.bridje.core.web.WebRequest;
import org.bridje.core.web.WebRequestChain;
import org.bridje.core.web.WebRequestHandler;
import org.bridje.core.web.WebResponse;

class WebRequestChainImpl implements WebRequestChain
{
    private final WebRequest request;
    
    private final WebResponse response;

    private final IocContext context;

    private final WebRequestHandler[] handlers;
    
    private int current;

    public WebRequestChainImpl(WebRequest request, WebResponse response, IocContext context, WebRequestHandler[] handlers)
    {
        this.request = request;
        this.response = response;
        this.context = context;
        this.handlers = handlers;
        this.current = this.handlers.length;
    }
    
    @Override
    public WebRequest getRequest()
    {
        return request;
    }

    @Override
    public WebResponse getResponse()
    {
        return response;
    }

    @Override
    public IocContext getRequestContext()
    {
        return context;
    }

    @Override
    public Object procced() throws HttpException
    {
        this.current--;
        if(this.current >= 0)
        {
            return this.handlers[this.current].proccess(this);
        }
        else
        {
            return null;
        }
    }
}
