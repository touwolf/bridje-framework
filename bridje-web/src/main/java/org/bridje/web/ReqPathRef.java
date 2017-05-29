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

import org.bridje.http.HttpBridletContext;
import org.bridje.http.HttpBridletRequest;

/**
 * This object represents a reference to the path to be handle by the current
 * http request. Bridlets may override this object in the current web context to
 * change the requested path asked by the client.
 */
public final class ReqPathRef
{
    private String reqPath;

    /**
     * The only constructor for this object, an string representing the path
     * must be provided.
     *
     * @param reqPath An string representing the path.
     */
    public ReqPathRef(String reqPath)
    {
        setReqPath(reqPath);
    }

    /**
     * Gets the string representation of the current path for the request.
     *
     * @return The string representation of the current path for the request.
     */
    public String getReqPath()
    {
        return reqPath;
    }

    /**
     * Sets the string representation of the current path for the request.
     *
     * @param reqPath The string representation of the current path for the
     * request.
     */
    public void setReqPath(String reqPath)
    {
        if (reqPath == null || reqPath.trim().isEmpty())
        {
            this.reqPath = "/";
            return;
        }
        if (!reqPath.startsWith("/"))
        {
            this.reqPath = "/" + reqPath;
            return;
        }
        this.reqPath = reqPath;
    }

    /**
     * A convinient method that returns the current path for the http request,
     * if any ReqPathRef was put in the HttpBridletContext this method will
     * return the path for that object, if no ReqPathRef was HttpBridletContext
     * this method will return the original requested path.
     *
     * @param ctx The current bridlet context.
     * @return The path that must be used to handle the current http request.
     */
    public static String findCurrentPath(HttpBridletContext ctx)
    {
        ReqPathRef ref = ctx.get(ReqPathRef.class);
        if (ref != null)
        {
            return ref.getReqPath();
        }
        return ctx.getRequest().getPath();
    }
}
