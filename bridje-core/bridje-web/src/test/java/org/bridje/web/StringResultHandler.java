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

package org.bridje.web;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.IocContext;
import org.bridje.ioc.annotations.Component;

/**
 *
 * @author Gilberto
 */
@Component
public class StringResultHandler implements WebResultHandler<String>
{
    private static final Logger LOG = Logger.getLogger(StringResultHandler.class.getName());

    @Override
    public Class<String> getHandledClass()
    {
        return String.class;
    }

    @Override
    public void handle(String result, WebResponse response, IocContext reqContext)
    {
        try
        {
            Writer w = new OutputStreamWriter(response.getOutputStream());
            w.write(result);
            w.flush();
        }
        catch(Exception ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
