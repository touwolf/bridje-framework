/*
 * Copyright 2017 Bridje Framework.
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

package org.bridje.orm.impl;

import org.bridje.ioc.Component;
import org.bridje.ioc.thls.Thls;
import org.bridje.ioc.thls.ThlsAction;
import org.bridje.ioc.thls.ThlsActionException;
import org.bridje.ioc.thls.ThlsActionException2;
import org.bridje.orm.ORMConfig;
import org.bridje.orm.ORMEnvironment;
import org.bridje.orm.ORMService;

@Component
class ORMServiceImpl implements ORMService
{
    @Override
    public ORMEnvironment createEnvironment(ORMConfig config)
    {
        return new ORMEnvironmentImpl(config);
    }

    @Override
    public <T> T doWithData(ThlsAction<T> action, ORMConfig config)
    {
        return Thls.doAs(action, ORMEnvironment.class, createEnvironment(config));
    }

    @Override
    public <T, E extends Throwable> T doWithDataEx(ThlsActionException<T, E> action, ORMConfig config) throws E
    {
        return Thls.doAsEx(action, ORMEnvironment.class, createEnvironment(config));
    }

    @Override
    public <T, E extends Throwable, E2 extends Throwable> T doWithDataEx2(ThlsActionException2<T, E, E2> action, ORMConfig config) throws E, E2
    {
        return Thls.doAsEx2(action, ORMEnvironment.class, createEnvironment(config));
    }
}
