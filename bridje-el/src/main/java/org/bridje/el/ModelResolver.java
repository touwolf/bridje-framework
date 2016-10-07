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

package org.bridje.el;

import java.util.Map;
import org.bridje.ioc.IocContext;

/**
 * An interface that allow other APIs to fill the global enviroment variables
 * for the given IoC context.
 */
@FunctionalInterface
public interface ModelResolver
{
    /**
     * Called when the ElEnviroment need to resolve the names of the components
     * that will participate in the expressions.
     *
     * @param ctx The context that the models will be resolved out of.
     * @param result The result map with the names and class of all the
     * components that are models to the given EL enviroment.
     */
    void resolveAllModels(IocContext<?> ctx, Map<String, Class<?>> result);
}
