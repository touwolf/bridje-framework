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

/**
 * A manager services that can proccess web request.
 *
 * This service is the starting point of every web request passed to this api.
 * Usually this service will be used in a HttpServlet service method.
 */
public interface WebManager
{
    /**
     * Proccess a web request, this method will create the WEBREQUEST context an
     * will invoke a WebRequestChain for handling the request.
     *
     * @param req The WebRequest object for this request.
     * @param resp The WebResponse object for this request.
     */
    void proccess(WebRequest req, WebResponse resp);
}
