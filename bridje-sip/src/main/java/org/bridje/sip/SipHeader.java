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

package org.bridje.sip;

import java.util.List;

/**
 * A SIP Heather
 */
public class SipHeader
{
    private String name;

    private List<String> values;

    /**
     * The name of the SIP header.
     * 
     * @return The name of the SIP header.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The name of the SIP header.
     * 
     * @param name The name of the SIP header.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * The first (or only) value of the SIP header.
     * 
     * @return The value of the SIP header.
     */
    public String getValue()
    {
        return values.get(0);
    }

    /**
     * The first (or only) value of the SIP header.
     * 
     * @param value The value of the SIP header.
     */
    public void setValue(String value)
    {
        this.values.remove(0);
        this.values.add(0, value);
    }
    
    /**
     * The lis of values for the SIP header.
     * 
     * @return THe list values for the SIP header.
     */
    public List<String> getValues()
    {
        return values;
    }

    /**
     * The list of values for the SIP header.
     * 
     * @param values The list of values for the SIP header.
     */
    public void setValues(List<String> values)
    {
        this.values = values;
    }
}
