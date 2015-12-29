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

package org.bridje.core.web;

import org.bridje.core.ioc.IocContext;

/**
 * This interface represents a result handler. the web api will use all
 * components providing this interface to resolve witch one will handle the
 * result of the request.
 *
 * @param <T> The type of objects this handler will proccess.
 */
public interface WebResultHandler<T>
{
    /**
     * Gets the class of objects that this handler will proccess.
     *
     * @return A class instance representing the class of the objects that this
     * handler can proccess.
     */
    Class<T> getHandledClass();

    /**
     * This method will be invoked by the api when a result object for this
     * handler is returned from the execution chain.
     *
     * @param result The result object to proccess.
     * @param response The current WebResponse.
     * @param reqContext The IocContext for the current request.
     */
    void handle(T result, WebResponse response, IocContext reqContext);
}
