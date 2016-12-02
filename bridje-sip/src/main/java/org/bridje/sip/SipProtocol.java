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

public class SipProtocol
{
    private SipVersion version;

    private IPProtocol type;

    public SipVersion getVersion()
    {
        return version;
    }

    public void setVersion(SipVersion version)
    {
        this.version = version;
    }

    public IPProtocol getType()
    {
        return type;
    }

    public void setType(IPProtocol type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return this.version + "/" + this.type.name();
    }
    
    public static SipProtocol fromString(String value)
    {
        String[] split = value.split("[/]");
        if(split.length != 3)
        {
            return null;
        }
        SipProtocol result = new SipProtocol();
        result.setVersion(SipVersion.fromString(split[0] + "/" + split[1]));
        result.setType(IPProtocol.valueOf(split[2]));
        return result;
    }
}
