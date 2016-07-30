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

package org.bridje.web.el;

import org.bridje.ioc.IocContext;
import org.bridje.web.WebRequestScope;

/**
 * A service for the expression language system.
 */
public interface ElService
{
    /**
     * Creates a new expression language environment.
     *
     * @param context The IocContext instance for the current request. The new
     * created EL environment will take the values from the components of this
     * context.
     * @return The new create expression language environment.
     */
    ElEnvironment createElEnvironment(IocContext<WebRequestScope> context);
}
