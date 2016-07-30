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

package org.bridje.web.el.impl;

import org.bridje.ioc.Component;
import org.bridje.ioc.IocContext;
import org.bridje.web.WebRequestScope;
import org.bridje.web.el.ElService;
import org.bridje.web.el.ElEnvironment;

@Component
class ElServiceImpl implements ElService
{
    @Override
    public ElEnvironment createElEnvironment(IocContext<WebRequestScope> context)
    {
        return new IocEnviromentImpl(context);
    }
}
