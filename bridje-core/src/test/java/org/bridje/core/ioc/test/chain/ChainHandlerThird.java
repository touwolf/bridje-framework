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

package org.bridje.core.ioc.test.chain;

import org.bridje.core.ioc.annotations.Component;
import org.bridje.core.ioc.annotations.InjectNext;
import org.bridje.core.ioc.annotations.Priority;

/**
 *
 * @author Gilberto
 */
@Component
@Priority(3)
public class ChainHandlerThird implements MyChainHandler<String>
{
    @InjectNext
    private MyChainHandler<String> next;

    @Override
    public String execute(String prev)
    {
        String c = "3";
        if(prev != null)
        {
            c = prev + " " + c;
        }
        if(next != null)
        {
            return next.execute(c);
        }
        return c;
    }
}
