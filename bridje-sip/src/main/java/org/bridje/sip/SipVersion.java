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

public class SipVersion
{
    public static SipVersion SIP_2_0 = new SipVersion("SIP", 2, 0);

    private String name;

    private int versionMayor;
    
    private int versionMinor;

    private SipVersion()
    {
    }

    public SipVersion(String name, int versionMayor, int versionMinor)
    {
        this.name = name;
        this.versionMayor = versionMayor;
        this.versionMinor = versionMinor;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVersionNumber()
    {
        return this.versionMayor + "." + this.versionMinor;
    }
    
    public int getVersionMayor()
    {
        return versionMayor;
    }

    public void setVersionMayor(int versionMayor)
    {
        this.versionMayor = versionMayor;
    }

    public int getVersionMinor()
    {
        return versionMinor;
    }

    public void setVersionMinor(int versionMinor)
    {
        this.versionMinor = versionMinor;
    }

    @Override
    public String toString()
    {
        return this.name + "/" + this.getVersionNumber();
    }

    public static SipVersion fromString(String value)
    {
        String[] split = value.split("[/]");
        if(split.length != 2)
        {
            return null;
        }
        SipVersion result = new SipVersion();
        result.setName(split[0]);
        split = split[1].split("[.]");
        if(split.length != 2)
        {
            return null;
        }
        result.setVersionMayor(Integer.valueOf(split[0]));
        result.setVersionMinor(Integer.valueOf(split[1]));
        return result;
    }
}
