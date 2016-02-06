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

package org.bridje.core.cfg;

import org.bridje.ioc.Ioc;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigServiceTest
{
    
    public ConfigServiceTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    @Test
    public void testFindConfig_Class() throws Exception
    {
        ConfigService instance = Ioc.context().find(ConfigService.class);
        MyConfig myCfg = instance.findConfig(MyConfig.class);
        assertNotNull(myCfg);
        assertNotNull(myCfg.getName());
        assertNotNull(myCfg.getPort());
        assertEquals("someServer", myCfg.getName());
        assertEquals(8080, myCfg.getPort());

        MyConfig myCfg1 = instance.findConfig("my-config", MyConfig.class);
        assertNotNull(myCfg1);
        assertNotNull(myCfg1.getName());
        assertNotNull(myCfg1.getPort());
        assertEquals("someServer1", myCfg1.getName());
        assertEquals(8081, myCfg1.getPort());

        MyConfig myCfg2 = instance.findConfig("system/server", MyConfig.class);
        assertNotNull(myCfg2);
        assertNotNull(myCfg2.getName());
        assertNotNull(myCfg2.getPort());
        assertEquals("localhost", myCfg2.getName());
        assertEquals(8082, myCfg2.getPort());
    }
}
