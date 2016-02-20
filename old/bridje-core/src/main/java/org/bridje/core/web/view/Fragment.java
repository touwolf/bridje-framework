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

package org.bridje.core.web.view;

import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "fragment")
@XmlAccessorType(XmlAccessType.FIELD)
public class Fragment extends AbstractView
{
    private static final Logger LOG = Logger.getLogger(Fragment.class.getName());

    @XmlAttribute(name = "category")
    @XmlJavaTypeAdapter(StringArrayAdapter.class)
    private String[] category;

    @XmlAttribute(name = "priority")
    private Integer priority;

    public String[] getCategory()
    {
        return category;
    }

    public Integer getPriority()
    {
        return priority;
    }
}
