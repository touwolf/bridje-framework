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

package org.bridje.cfg;

import java.io.IOException;
import org.bridje.ioc.Ioc;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ConfigServiceTest
{
    private ConfigService cfgServ;

    @Before
    public void before()
    {
        cfgServ = Ioc.context().find(ConfigService.class);
        cfgServ.addRepository(cfgServ.createClassPathRepository(ConfigService.class, "/BRIDJE-INF/etc"));
    }

    @Test
    public void test1FindConfigClass() throws IOException
    {
        MyConfig myCfg = cfgServ.findConfig(MyConfig.class);
        assertNotNull(myCfg);
        assertNotNull(myCfg.getName());
        assertNotNull(myCfg.getPort());
        assertEquals("someServer", myCfg.getName());
        assertEquals(8080, myCfg.getPort());

        MyConfig myCfg1 = cfgServ.findConfig("my-config", MyConfig.class);
        assertNotNull(myCfg1);
        assertNotNull(myCfg1.getName());
        assertNotNull(myCfg1.getPort());
        assertEquals("someServer1", myCfg1.getName());
        assertEquals(8081, myCfg1.getPort());

        MyConfig myCfg2 = cfgServ.findConfig("system/server", MyConfig.class);
        assertNotNull(myCfg2);
        assertNotNull(myCfg2.getName());
        assertNotNull(myCfg2.getPort());
        assertEquals("localhost", myCfg2.getName());
        assertEquals(8082, myCfg2.getPort());
    }

    @Test
    public void test2Contexts() throws IOException
    {
        MyConfig myCfg2 = cfgServ.createContext("system").findConfig("server", MyConfig.class);
        assertNotNull(myCfg2);
        assertNotNull(myCfg2.getName());
        assertNotNull(myCfg2.getPort());
        assertEquals("localhost", myCfg2.getName());
        assertEquals(8082, myCfg2.getPort());

        myCfg2 = cfgServ.createContext("system").createContext("local").findConfig("server-local", MyConfig.class);
        assertNotNull(myCfg2);
        assertNotNull(myCfg2.getName());
        assertNotNull(myCfg2.getPort());
        assertEquals("myserver", myCfg2.getName());
        assertEquals(8083, myCfg2.getPort());

        myCfg2 = cfgServ.createContext("system").createContext("local").findConfig("server", MyConfig.class);
        assertNull(myCfg2);

    }

    @Test
    public void test3Properties() throws IOException
    {
        PropConfig prop = cfgServ.findConfig(PropConfig.class);
        assertNotNull(prop);
        assertNotNull(prop.getName());
        assertNotNull(prop.getPort());
        assertEquals("someserver", prop.getName());
        assertEquals(5050, prop.getPort());        
    }
}
