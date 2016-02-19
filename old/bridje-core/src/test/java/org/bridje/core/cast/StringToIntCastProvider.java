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

package org.bridje.core.cast;

import org.bridje.ioc.Component;

/**
 *
 * @author Gilberto
 */
@Component
public class StringToIntCastProvider implements CastProvider<Integer>
{

    @Override
    public Class<?>[] getSrcClasses()
    {
        return new Class<?>[] { String.class };
    }

    @Override
    public Class<Integer> getDestClass()
    {
        return Integer.class;
    }

    @Override
    public Integer cast(Object object)
    {
        String str = (String)object;
        return Integer.valueOf(str);
    }
    
}
