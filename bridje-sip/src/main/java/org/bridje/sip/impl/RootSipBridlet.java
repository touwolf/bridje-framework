/*
 * Copyright 2016 Bridje Framework.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      sip://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bridje.sip.impl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.sip.SipBridlet;
import org.bridje.sip.SipException;
import org.bridje.ioc.Component;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;
import org.bridje.sip.SipRequestMessage;
import org.bridje.sip.SipResponseMessage;

@Component
@Priority(Integer.MIN_VALUE)
class RootSipBridlet implements SipBridlet
{
    private static final Logger LOG = Logger.getLogger(RootSipBridlet.class.getName());

    @InjectNext
    private SipBridlet handler;
    
    @Override
    public SipResponseMessage handle(SipRequestMessage req) throws IOException
    {
        LOG.log(Level.INFO, "{0} {1} {2}", new Object[]{req.getMethod(), req.getUri(), req.getVersion()});
        try
        {
            if(handler != null)
            {
                SipResponseMessage resp = handler.handle(req);
                if(resp != null)
                {
                    return resp;
                }
            }
            throw new SipException(404, "Not Found");
        }
        catch (SipException e)
        {
            LOG.log(Level.WARNING, "{0} {1} {2} - {3} {4}", 
                        new Object[]{req.getMethod(), req.getUri(), req.getVersion(), e.getStatus(), e.getMessage()});
            SipResponseMessage resp = new SipResponseMessage();
            resp.setStatusCode(e.getStatus());
            resp.setMessage(e.getMessage());
            resp.setVersion(req.getVersion());
            return resp;
        }
    }
    
}
