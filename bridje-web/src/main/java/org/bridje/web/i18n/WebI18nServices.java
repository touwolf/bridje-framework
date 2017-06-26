/*
 * Copyright 2017 Bridje Framework.
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

package org.bridje.web.i18n;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;

/**
 * This service allows to gets an inject i1n resources for the web views.
 */
@Component
public class WebI18nServices
{
    private Map<String,ResourceBundle> i18nMap;

    @Inject
    private WebI18nProvider[] webI18nProv;

    @PostConstruct
    private void init()
    {
        initI18nMap();
    }

    private void initI18nMap()
    {
        i18nMap = new HashMap<>();
        for (WebI18nProvider webI18nProvider : webI18nProv)
        {
            Map<String, ResourceBundle> map = webI18nProvider.findResourceBundles();
            i18nMap.putAll(map);
        }
        i18nMap = Collections.unmodifiableMap(i18nMap);
    }

    /**
     * All the i18n resources.
     * 
     * @return All the i18n resources.
     */
    public Map<String, ResourceBundle> getI18nMap()
    {
        return i18nMap;
    }
}
