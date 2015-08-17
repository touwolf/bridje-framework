
package org.bridje.ioc.impl;

import java.io.IOException;
import org.bridje.ioc.test.DummyComponent;
import org.bridje.ioc.test.DummyServiceProvider;
import org.bridje.ioc.test.DummyWithParamsComponent;
import org.bridje.ioc.test.SomeService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class IocContextImplTest
{
    
    public IocContextImplTest()
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
    public void testFind() throws IOException
    {
        IocContextImpl instance = new IocContextImpl("APPLICATION");
        DummyComponent result = instance.find(DummyComponent.class);
        assertNotNull(result);
    }
    
    @Test
    public void testFindWithConstructor() throws IOException
    {
        IocContextImpl instance = new IocContextImpl("APPLICATION");
        DummyWithParamsComponent result = instance.find(DummyWithParamsComponent.class);
        assertNotNull(result);
    }
    
    @Test
    public void testFindByService() throws IOException
    {
        IocContextImpl instance = new IocContextImpl("APPLICATION");
        SomeService result = instance.find(SomeService.class);
        assertNotNull(result);
        assertTrue(result instanceof DummyServiceProvider);
    }
}
