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

import org.bridje.ioc.IocContext;
import org.bridje.web.WebRequestScope;

class UIExpContext
{
    static final ThreadLocal<UIExpContext> TL_CTXS = new ThreadLocal<>();

    public static UIExpContext getCurrent()
    {
        return TL_CTXS.get();
    }
    
    private final IocContext<WebRequestScope> iocCtx;

    public UIExpContext(IocContext<WebRequestScope> iocCtx)
    {
        this.iocCtx = iocCtx;
    }
    
    public <T> T eval(String exp, Class<T> result)
    {
        return null;
    }

    public <T> void update(String expression, T value)
    {
        
    }
}
