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

/**
 * Parent class for all the resources assets for an UI suite.
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Script extends AssetBase
{
    @XmlAttribute
    private Boolean async;

    @XmlAttribute
    private Boolean defer;
    
    @XmlAttribute
    private Boolean inview;

    /**
     * If this asset must be declared as async.
     * 
     * @return If this asset must be declared as async.
     */
    public Boolean getAsync()
    {
        if (async == null)
        {
            async = false;
        }
        return async;
    }

    /**
     * If this asset must be declared as async.
     * 
     * @param async If this asset must be declared as async.
     */
    public void setAsync(Boolean async)
    {
        this.async = async;
    }

    /**
     * If this asset must be declared as defer.
     * 
     * @return If this asset must be declared as defer.
     */
    public Boolean getDefer()
    {
        if (defer == null)
        {
            defer = false;
        }
        return defer;
    }

    /**
     * If this asset must be declared as defer.
     * 
     * @param defer If this asset must be declared as defer.
     */
    public void setDefer(Boolean defer)
    {
        this.defer = defer;
    }

    /**
     * If the script need to be declared inside the view.
     * 
     * @return If the script need to be declared inside the view.
     */
    public Boolean getInview()
    {
        return inview;
    }

    /**
     * If the script need to be declared inside the view.
     * 
     * @param inview If the script need to be declared inside the view.
     */
    public void setInview(Boolean inview)
    {
        this.inview = inview;
    }
}
