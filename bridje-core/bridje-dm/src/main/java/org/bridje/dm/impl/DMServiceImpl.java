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

package org.bridje.dm.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bridje.dm.DMProvider;
import org.bridje.dm.DMService;
import org.bridje.dm.DomainModel;
import org.bridje.ioc.annotations.Component;
import org.bridje.ioc.annotations.Inject;
import org.bridje.ioc.annotations.PostConstruct;

/**
 *
 * @author gilberto
 */
@Component
public class DMServiceImpl implements DMService
{
    private List<DomainModel> dmList;
    
    private Map<String, DomainModel> dmMap;
    
    @Inject
    private DMProvider[] providers;
    
    @PostConstruct
    public void init()
    {
        dmMap = new HashMap<>();
        dmList = new ArrayList<>();
        for (DMProvider p : providers)
        {
            List<DomainModel> dms = p.findDomainModels();
            dmList.addAll(dms);
            for (DomainModel dm : dms)
            {
                dmMap.put(dm.getName(), dm);
            }
        }
    }
    
    @Override
    public List<DomainModel> findAll()
    {
        return Collections.unmodifiableList(dmList);
    }

    @Override
    public DomainModel find(String name)
    {
        return dmMap.get(name);
    }
}
