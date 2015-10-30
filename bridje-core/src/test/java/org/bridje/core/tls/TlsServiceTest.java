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

package org.bridje.core.tls;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TlsServiceTest
{
    
    public TlsServiceTest()
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
    public void testDoAs() throws Exception
    {
        //No data must be in the thread.
        assertNull("Where did you get this data?", Tls.get(SomeData.class));
        //Create some data
        SomeData d = new SomeData();
        d.setStr("Hi");
        //Execute an action an get the "Hi" value from withing the action.
        String result = Tls.doAs(() ->
        {
            assertNotNull("SomeData must be here.",Tls.get(SomeData.class));
            assertEquals("SomeData must have the \"Hi\" value.", "Hi",Tls.get(SomeData.class).getStr());
            return Tls.get(SomeData.class).getStr();
        }, new Object[]{d});
        assertEquals("Wrong result.", "Hi", result);
        //Again, No data must be in the thread.
        assertNull("Where did you get this data?", Tls.get(SomeData.class));
        //Execute multiple nested actions with the same data.
        result = Tls.doAs(() ->
        {
            assertNotNull("SomeData must be here.",Tls.get(SomeData.class));
            assertEquals("SomeData must have the \"Hi\" value.", "Hi",Tls.get(SomeData.class).getStr());
            
            //Execute the nested action with the same class bu a diferento object with diferent data.
            //Create some diferent data
            SomeData d1 = new SomeData();
            d1.setStr("Hello");
            //Execute an action an get the "Hello" value from withing the action.
            String result1 = Tls.doAs(() ->
            {
                assertNotNull("SomeData must be here.",Tls.get(SomeData.class));
                assertEquals("SomeData must have the \"Hello\" value.", "Hello",Tls.get(SomeData.class).getStr());
                return Tls.get(SomeData.class).getStr();
            }, new Object[]{d1});
            assertEquals("Wrong result.", "Hello", result1);

            //Test that everything is as it was berofe the nested action was executed.
            assertNotNull("SomeData must be here.",Tls.get(SomeData.class));
            assertEquals("SomeData must have the \"Hi\" value.", "Hi",Tls.get(SomeData.class).getStr());
            return Tls.get(SomeData.class).getStr();
        }, new Object[]{d});
        assertEquals("Wrong result.", "Hi", result);
        //No data must be in the thread.
        assertNull("Where did you get this data?", Tls.get(SomeData.class));
    }
}
