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

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * A provider for i18n resources.
 */
public interface WebI18nProvider
{
    /**
     * Finds all the resources for this provider.
     * 
     * @return Finds all the resources for this provider.
     */
    Map<String, ResourceBundle> findResourceBundles();
    
    /**
     * Finds the resources for the specified locale and this provider.
     *
     * @return The resources for the specified locale and this provider.
     */
    Map<String, ResourceBundle> findResourceBundles(Locale locale);
}
