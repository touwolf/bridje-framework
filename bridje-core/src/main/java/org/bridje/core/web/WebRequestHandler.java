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
 * A handler for the web request chain of the framework.
 */
public interface WebRequestHandler
{
    /**
     * This methods will procced with the execution of the chain, if the
     * implementation of this method needs to continue the chain it will need to
     * call the procced method of the chain parameter passed to it. otherwise
     * this method may return the result of the chain ignoring the rest of it.
     *
     * @param chain The current chain being proccessed.
     * @return The result of the execution.
     */
    Object proccess(WebRequestChain chain);
}
