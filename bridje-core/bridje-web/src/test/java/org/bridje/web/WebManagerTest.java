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

package org.bridje.web;

import org.bridje.ioc.Ioc;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Gilberto
 */
public class WebManagerTest
{
    
    public WebManagerTest()
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

    /**
     * Test of proccess method, of class WebManager.
     */
    @Test
    public void testProccess()
    {
        WebManager wm = Ioc.context().find(WebManager.class);
        MockWebResponse resp = new MockWebResponse();
        //if i call http://somehost/index
        wm.proccess(new MockWebRequest("/index"), resp);
        assertEquals("hello", resp.getResult());
        //if i call http://somehost/user/shopping/car
        resp = new MockWebResponse();
        wm.proccess(new MockWebRequest("/user/shopping/car"), resp);
        assertEquals("hello fron shopping car", resp.getResult());
    }
}
