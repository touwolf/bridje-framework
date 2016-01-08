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

package org.bridje.core.impl.web.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.bridje.core.web.WebResponse;

public class WebResponseImpl implements WebResponse
{
    private static final Logger LOG = Logger.getLogger(WebResponseImpl.class.getName());

    private final HttpServletResponse resp;
    
    private boolean processed;

    public WebResponseImpl(HttpServletResponse resp)
    {
        this.resp = resp;
    }

    @Override
    public OutputStream getOutputStream()
    {
        try
        {
            return resp.getOutputStream();
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public void processed()
    {
        if(processed)
        {
            throw new IllegalStateException("The request is allready procesed.");
        }
        processed = true;
    }

    @Override
    public boolean isProcessed()
    {
        return processed;
    }

    @Override
    public void setStatusCode(int code)
    {
        resp.setStatus(code);
    }
}
