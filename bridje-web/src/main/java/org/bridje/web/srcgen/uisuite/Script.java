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

package org.bridje.web.srcgen.uisuite;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Script extends AssetBase
{
    @XmlAttribute
    private String href;

    @XmlAttribute
    private Boolean async;

    @XmlAttribute
    private Boolean defer;

    public String getHref()
    {
        return href;
    }

    public void setHref(String href)
    {
        this.href = href;
    }

    public Boolean getAsync()
    {
        if (async == null)
        {
            async = false;
        }
        return async;
    }

    public void setAsync(Boolean async)
    {
        this.async = async;
    }

    public Boolean getDefer()
    {
        if (defer == null)
        {
            defer = false;
        }
        return defer;
    }

    public void setDefer(Boolean defer)
    {
        this.defer = defer;
    }
}
