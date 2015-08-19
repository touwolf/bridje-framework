
package org.bridje.ioc.impl;

import java.io.IOException;
import org.bridje.ioc.Ioc;
import org.bridje.ioc.IocContext;
import org.bridje.ioc.test.ConcreteCompoenent;
import org.bridje.ioc.test.DummyComponent;
import org.bridje.ioc.test.DummyServiceProvider;
import org.bridje.ioc.test.DummyServiceProvider2;
import org.bridje.ioc.test.DummyWithParamsComponent;
import org.bridje.ioc.test.GenericComponent;
import org.bridje.ioc.test.GenericInjectComponent;
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
        IocContext instance = Ioc.context();
        DummyComponent result = instance.find(DummyComponent.class);
        assertNotNull(result);
    }

    @Test
    public void testFindWithConstructor() throws IOException
    {
        IocContext instance = Ioc.context();
        DummyWithParamsComponent result = instance.find(DummyWithParamsComponent.class);
        assertNotNull(result);
        assertNotNull(result.getDummyComponent());
        assertEquals(result.getDummyComponent(), instance.find(DummyComponent.class));
    }
    
    @Test
    public void testFindByService() throws IOException
    {
        IocContext instance = Ioc.context();
        SomeService result = instance.find(SomeService.class);
        assertNotNull(result);
        assertTrue(result instanceof DummyServiceProvider);

        SomeService[] resultArr = instance.findAll(SomeService.class);
        assertNotNull(resultArr);
        assertEquals(2, resultArr.length);
        assertTrue(resultArr[0] instanceof DummyServiceProvider);
        assertTrue(resultArr[1] instanceof DummyServiceProvider2);
    }

    @Test
    public void testInjectAndHerarchy() throws IOException
    {
        IocContext instance = Ioc.context();
        ConcreteCompoenent conComp = instance.find(ConcreteCompoenent.class);
        assertNotNull(conComp.getDummyComponent());
        assertNotNull(conComp.getServices());
        assertTrue(conComp.getServices()[0] instanceof DummyServiceProvider);
        assertTrue(conComp.getServices()[1] instanceof DummyServiceProvider2);
    }

    @Test
    public void testInjectGeneric() throws IOException
    {
        IocContext instance = Ioc.context();
        GenericInjectComponent giComp = instance.find(GenericInjectComponent.class);
        assertNotNull(giComp);
        assertNotNull(giComp.getGsOfStr());
        assertNull(giComp.getGsOfObject());
        assertTrue(giComp.getGsOfStr() instanceof GenericComponent);
    }
}
