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

package org.bridje.el;

import java.util.ArrayList;
import java.util.List;
import org.bridje.ioc.Ioc;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ElServiceTest
{
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
    public void testEval()
    {
        ElService elServ = Ioc.context().find(ElService.class);
        ElEnvironment elEnv = elServ.createElEnvironment(Ioc.context());
        String result = elEnv.get("${myModel.name}", String.class);
        assertNotNull(result);
        assertEquals("Some Name", result);
        List lst = elEnv.get("${myModel.list}", List.class);
        assertNotNull(lst);
        assertEquals(1, lst.size());
    }

    @Test
    public void testVars()
    {
        ElService elServ = Ioc.context().find(ElService.class);
        ElEnvironment elEnv = elServ.createElEnvironment(Ioc.context());
        elEnv.setVar("myVar", "Hello");
        String result = elEnv.get("${myVar}", String.class);
        assertNotNull(result);
        assertEquals("Hello", result);
        elEnv.setVar("myList", new ArrayList<>());
        List lst = elEnv.get("${myList}", List.class);
        assertNotNull(lst);
        assertEquals(0, lst.size());
    }

    @Test
    public void testI18n()
    {
        ElService elServ = Ioc.context().find(ElService.class);
        ElEnvironment elEnv = elServ.createElEnvironment(Ioc.context());
        String result = elEnv.get("${i18n.someRes.name}", String.class);
        assertNotNull(result);
        assertEquals("Hola", result);
    }
}
