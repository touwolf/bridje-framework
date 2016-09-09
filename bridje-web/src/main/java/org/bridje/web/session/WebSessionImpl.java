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

package org.bridje.web.session;

class WebSessionImpl implements WebSession
{
    private final String id;
    
    private final WebSessionProvider provider;

    public WebSessionImpl(String id, WebSessionProvider provider)
    {
        this.id = id;
        this.provider = provider;
    }
    
    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public String find(String name)
    {
        return provider.find(id, name);
    }

    @Override
    public void save(String name, String value)
    {
        provider.save(id, name, value);
    }    
}
