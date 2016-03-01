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

package org.bridje.ioc.test;

import java.util.List;
import java.util.Map;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;

@Component
public class GenericInjectComponent
{
    //TODO test for array injection of generic services.
    private GenericService<String>[] gsOfStrArr;

    @Inject
    private GenericService<String> gsOfStr;

    @Inject
    private GenericService<Object> gsOfObject;
    
    @Inject
    private GenericService<Map<String,List<Integer>>> complexInject;

    public GenericService<String> getGsOfStr()
    {
        return gsOfStr;
    }

    public GenericService<Object> getGsOfObject()
    {
        return gsOfObject;
    }

    public GenericService<Map<String, List<Integer>>> getComplexInject()
    {
        return complexInject;
    }
}
