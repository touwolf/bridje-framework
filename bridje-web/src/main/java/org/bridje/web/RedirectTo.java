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

package org.bridje.web;

/**
 * This object represents an HTTP redirection instruction. Any web method or
 * handler can retrieve or put this into the context respectibly to indicate that
 * the result of the request should be a redirection to the specified resource.
 */
public class RedirectTo
{
    private int status;

    private String resource;

    /**
     * Constructor that puts the status to 302 code.
     * 
     * @param resource The url of the resource to redirect to.
     */
    public RedirectTo(String resource)
    {
        this(302, resource);
    }

    /**
     * Constructor for the status code an the resource url.
     * 
     * @param status The status code for the redirect must be a valid redirect 3xx code.
     * @param resource The resource to redirect to.
     */
    public RedirectTo(int status, String resource)
    {
        if (status < 300 || status > 308)
        {
            throw new IllegalArgumentException("Invalid redirect code " + status);
        }
        this.status = status;
        this.resource = resource;
    }

    /**
     * The status code for the redirect must be a valid redirect 3xx code.
     * 
     * @return The status code for the redirect must be a valid redirect 3xx code.
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * The status code for the redirect must be a valid redirect 3xx code.
     * 
     * @param status The status code for the redirect must be a valid redirect 3xx code.
     */
    public void setStatus(int status)
    {
        if (status < 300 || status > 308)
        {
            throw new IllegalArgumentException("Invalid redirect code " + status);
        }
        this.status = status;
    }

    /**
     * The url of the resource to redirect to.
     * 
     * @return The url of the resource to redirect to.
     */
    public String getResource()
    {
        return resource;
    }

    /**
     * The url of the resource to redirect to.
     * 
     * @param resource The url of the resource to redirect to.
     */
    public void setResource(String resource)
    {
        this.resource = resource;
    }
}
