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

import org.bridje.ioc.IocContext;

/**
 * A chain controller object for the current web request being processed by the
 * api.
 */
public interface WebRequestChain
{
    /**
     * The web request object for the current request.
     *
     * @return A WebRequest object witch holds the data of the current request.
     */
    public WebRequest getRequest();

    /**
     * The web response object for the current request.
     *
     * @return A WebResponse object witch holds the data of the current request's
     * response.
     */
    public WebResponse getResponse();

    /**
     * The inversion of control context for the current request.
     *
     * @return The IocContext of the "WEBREQUEST" scope created for the current
     * request.
     */
    IocContext getRequestContext();

    /**
     * Continues the chain by calling the next available handler. If no next
     * handler can be found this method will return null.
     *
     * @return The result of the chain, may be null.
     * @throws org.bridje.core.web.HttpException
     */
    public Object procced() throws HttpException;
}
