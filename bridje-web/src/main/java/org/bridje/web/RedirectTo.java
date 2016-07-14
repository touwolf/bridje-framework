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

public class RedirectTo
{
    private int status;

    private String resource;

    public RedirectTo(String resource)
    {
        this(302, resource);
    }

    public RedirectTo(int status, String resource)
    {
        if(status < 300 || status > 308)
        {
            throw new IllegalArgumentException("Invalid redirect code " + status);
        }
        this.status = status;
        this.resource = resource;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        if(status < 300 || status > 308)
        {
            throw new IllegalArgumentException("Invalid redirect code " + status);
        }
        this.status = status;
    }

    public String getResource()
    {
        return resource;
    }

    public void setResource(String resource)
    {
        this.resource = resource;
    }
}
