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

package org.bridje.cli;

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
public class CliServiceTest
{
    
    public CliServiceTest()
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
     * Test of execute method, of class CliService.
     */
    @Test
    public void testExecute()
    {
        CliService cliServ = Ioc.context().find(CliService.class);
        // test -aei argument1 argument2
        // or
        // test -a -e -i argument1 argument2
        cliServ.execute(new String[] { "test", "a", "e", "i", "argument1", "argument2" });
        cliServ.execute(new String[] { "test", "a", "e", "o", "argument1", "argument2" });
    }
}
