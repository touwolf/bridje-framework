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

import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.stream.Collectors;

public class SipViaInfo
{
    private SipProtocol protocol;
    
    private InetSocketAddress address;

    private Properties properties;

    public SipProtocol getProtocol()
    {
        return protocol;
    }

    public void setProtocol(SipProtocol protocol)
    {
        this.protocol = protocol;
    }

    public InetSocketAddress getAddress()
    {
        return address;
    }

    public void setAddress(InetSocketAddress address)
    {
        this.address = address;
    }

    public Properties getProperties()
    {
        if(properties == null)
        {
            properties = new Properties();
        }
        return properties;
    }

    public void setProperties(Properties properties)
    {
        this.properties = properties;
    }

    @Override
    public String toString()
    {
        String props = properties.entrySet().stream()
                .map((entry) -> entry.getValue().toString().isEmpty() ? entry.getKey().toString() : entry.getKey().toString() + "=" + entry.getValue().toString())
                .collect(Collectors.joining(";"));
        return protocol + " " + address + ";" + props;
    }

    public static SipViaInfo fromString(String value)
    {
        String[] split = value.split(";");
        if(split.length == 0)
        {
            return null;
        }
        Properties props = new Properties();
        if(split.length > 1)
        {
            for(int i = 1; i < split.length; i++)
            {
                String[] pair = split[i].split("=");
                if(pair.length == 2)
                {
                    props.put(pair[0], pair[1]);
                }
                if(pair.length == 1 && !pair[0].isEmpty())
                {
                    props.put(pair[0], "");
                }
            }
        }
        split = split[0].split(" ");
        if(split.length != 2)
        {
            return null;
        }
        SipViaInfo sipViaInfo = new SipViaInfo();
        sipViaInfo.setProtocol(SipProtocol.fromString(split[0]));
        String addr = split[1];
        split = addr.split(":");
        if(split.length != 2)
        {
            return null;
        }
        sipViaInfo.setAddress(InetSocketAddress.createUnresolved(split[0], Integer.valueOf(split[1])));
        sipViaInfo.setProperties(props);
        return sipViaInfo;
    }
}
