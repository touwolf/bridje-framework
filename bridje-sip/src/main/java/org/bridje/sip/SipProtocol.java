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

/**
 * The SIP protocol definition for a SIP message.
 */
public class SipProtocol
{
    private SipVersion version;

    /**
     * 
     */
    private IPProtocol type;

    /**
     * The version of the SIP protocol for this message.
     * 
     * @return The version of the SIP protocol for this message.
     */
    public SipVersion getVersion()
    {
        return version;
    }

    /**
     * The version of the SIP protocol for this message.
     * 
     * @param version The version of the SIP protocol for this message.
     */
    public void setVersion(SipVersion version)
    {
        this.version = version;
    }

    /**
     * The type of IP protocol for this SIP message.
     * 
     * @return The type of IP protocol for this SIP message.
     */
    public IPProtocol getType()
    {
        return type;
    }

    /**
     * The type of IP protocol for this SIP message.
     * 
     * @param type The type of IP protocol for this SIP message.
     */
    public void setType(IPProtocol type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return this.version + "/" + this.type.name();
    }
    
    /**
     * Creates a new SIP protocol definition from the corresponding text.
     * 
     * @param value The text taht defines the SIP protocol definition.
     * @return The SIP protocol definition object.
     */
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
