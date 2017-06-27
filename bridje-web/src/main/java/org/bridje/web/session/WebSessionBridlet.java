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

package org.bridje.web.session;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.UUID;
import org.bridje.http.HttpBridlet;
import org.bridje.http.HttpBridletContext;
import org.bridje.http.HttpCookie;
import org.bridje.http.HttpException;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.IocContext;
import org.bridje.ioc.Priority;
import org.bridje.web.WebScope;

@Component
@Priority(200)
class WebSessionBridlet implements HttpBridlet
{
    @InjectNext
    private HttpBridlet nextHandler;
    
    @Inject
    private WebSessionManager sessionManager;

    @Inject
    private WebSessionProvider sessionProvider;

    @Override
    public boolean handle(HttpBridletContext context) throws IOException, HttpException
    {
        IocContext<WebScope> ctx = context.get(IocContext.class);
        String sessionId = findSessionId(ctx.getScope());
        WebSessionImpl webSess = new WebSessionImpl(sessionId, sessionProvider);
        context.set(WebSession.class, webSess);
        if(nextHandler == null)
        {
            return false;
        }
        WebSessionListener listener = ctx.find(WebSessionListener.class);
        if(nextHandler.handle(context))
        {
            listener.getInjectedClasses().forEach((Class<?> cls, Set<Field> fields) ->
            {
                sessionManager.storeValue(ctx, cls, ctx.find(cls), webSess);
            });
            return true;
        }
        return false;
    }

    public String findSessionId(WebScope req)
    {
        HttpCookie sessionIdCookie = req.getCookie("sessionid");
        if(sessionIdCookie != null)
        {
            return sessionIdCookie.getValue();
        }
        else
        {
            String sessionId = UUID.randomUUID().toString();
            sessionIdCookie = req.addCookie("sessionid", sessionId);
            return sessionId;
        }
    }
}
