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

package org.bridje.web.impl;

import java.io.IOException;
import org.bridje.http.HttpBridlet;
import org.bridje.http.HttpBridletContext;
import org.bridje.http.HttpException;
import org.bridje.ioc.*;
import org.bridje.ioc.thls.Thls;
import org.bridje.ioc.thls.ThlsActionException2;
import org.bridje.web.WebScope;

@Component
@Priority(0)
class ScopeBridlet implements HttpBridlet
{
    @Inject
    private IocContext<Application> appCtx;

    @InjectNext
    private HttpBridlet nextHandler;

    @Override
    public boolean handle(HttpBridletContext context) throws IOException, HttpException
    {
        IocContext<WebScope> wrsCtx = appCtx.createChild(new WebScope(context));
        context.set(WebScope.class, wrsCtx.getScope());
        return Thls.doAsEx2(new ThlsActionException2<Boolean, IOException, HttpException>()
        {
            @Override
            public Boolean execute() throws IOException, HttpException
            {
                return nextHandler.handle(context);
            }
        }, WebScope.class, wrsCtx.getScope() );
    }
}
