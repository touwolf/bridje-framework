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

package org.bridje.tpl;

import java.io.IOException;
import java.util.HashMap;
import org.bridje.ioc.Ioc;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Gilberto
 */
public class TplServiceTest
{
    
    public TplServiceTest()
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
     * Test of createTplContext method, of class TplService.
     * @throws org.bridje.core.tpl.TplNotFoundException
     * @throws java.io.IOException
     */
    @Test
    public void testAll() throws TplNotFoundException, IOException
    {
        TplService tplServ = Ioc.context().find(TplService.class);
        String result = tplServ.render("/mytemplate.dummy", new HashMap());
        Assert.assertEquals("Hello", result);
    }
    
}
