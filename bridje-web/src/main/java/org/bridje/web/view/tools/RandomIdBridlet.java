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

package org.bridje.web.view.tools;

import java.io.IOException;
import javax.xml.bind.annotation.XmlTransient;
import org.bridje.http.HttpBridlet;
import org.bridje.http.HttpBridletContext;
import org.bridje.http.HttpException;
import org.bridje.ioc.Component;
import org.bridje.ioc.InjectNext;
import org.bridje.ioc.Priority;
import org.bridje.ioc.thls.Thls;
import org.bridje.ioc.thls.ThlsActionException2;

@Component
@Priority(110)
@XmlTransient
class RandomIdBridlet implements HttpBridlet
{
    @InjectNext
    private HttpBridlet nextHandler;

    @Override
    public boolean handle(HttpBridletContext context) throws IOException, HttpException
    {
        return Thls.doAsEx2(new ThlsActionException2<Boolean, IOException, HttpException>()
        {
            @Override
            public Boolean execute() throws IOException, HttpException
            {
                if(nextHandler != null) return nextHandler.handle(context);
                return false;
            }
        }, RandomIdGenerator.class, new RandomIdGenerator());
    }
}
