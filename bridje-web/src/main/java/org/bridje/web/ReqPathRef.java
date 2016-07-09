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

import org.bridje.http.HttpServerContext;
import org.bridje.http.HttpServerRequest;

/**
 *
 */
public class ReqPathRef
{
    private String reqPath;

    public ReqPathRef(String reqPath)
    {
        setReqPath(reqPath);
    }
    
    public String getReqPath()
    {
        return reqPath;
    }

    public void setReqPath(String reqPath)
    {
        if(reqPath == null || reqPath.trim().isEmpty())
        {
            this.reqPath = "/";
        }
        if(!reqPath.startsWith("/"))
        {
            this.reqPath = "/" + reqPath;
        }
        this.reqPath = reqPath;
    }
    
    public static String findCurrentPath(HttpServerContext ctx)
    {
        ReqPathRef ref = ctx.get(ReqPathRef.class);
        if(ref != null)
        {
            return ref.getReqPath();
        }
        return ctx.get(HttpServerRequest.class).getPath();
    }
}
