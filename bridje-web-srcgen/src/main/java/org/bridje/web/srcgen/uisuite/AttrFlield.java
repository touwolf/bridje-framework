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

/**
 * This class defines an attribute field of a control.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AttrFlield extends BaseDataFlield
{
    @Override
    public String getJavaType()
    {
        return getType();
    }

    @Override
    public String getFieldType()
    {
        return "attribute";
    }

    @Override
    public boolean getIsInput()
    {
        return false;
    }

    @Override
    public boolean getIsInputFile()
    {
        return false;
    }
}
