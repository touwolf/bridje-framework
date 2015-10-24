
package org.bridje.core.ioc.impl;

import java.io.IOException;
import org.bridje.core.ioc.Ioc;
import org.bridje.core.ioc.IocContext;
import org.bridje.core.ioc.test.ComplexInjectComponent;
import org.bridje.core.ioc.test.ConcreteComponent;
import org.bridje.core.ioc.test.DefaultComponent2;
import org.bridje.core.ioc.test.DefaultService;
import org.bridje.core.ioc.test.DummyComponent;
import org.bridje.core.ioc.test.DummyServiceProvider;
import org.bridje.core.ioc.test.DummyServiceProvider2;
import org.bridje.core.ioc.test.DummyWithAnnotatedConstruct;
import org.bridje.core.ioc.test.DummyWithParamsComponent;
import org.bridje.core.ioc.test.GenericComponent;
import org.bridje.core.ioc.test.GenericInjectComponent;
import org.bridje.core.ioc.test.SomeService;
import org.bridje.core.ioc.test.priority.PriorityComp1;
import org.bridje.core.ioc.test.priority.PriorityComp2;
import org.bridje.core.ioc.test.priority.PriorityComp3;
import org.bridje.core.ioc.test.priority.PriorityComp4;
import org.bridje.core.ioc.test.priority.PriorityService;
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
        Ioc.init(Service.
                forThis(DefaultService.class).
                implementBy(DefaultComponent2.class));
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

        DummyWithAnnotatedConstruct result1 = instance.find(DummyWithAnnotatedConstruct.class);
        assertNotNull(result1);
        assertNotNull(result1.getComplexInject());
        assertEquals(result1.getComplexInject(), instance.find(ComplexInjectComponent.class));
        assertNull(result1.getDummyComponent());
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
        ConcreteComponent conComp = instance.find(ConcreteComponent.class);
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
        assertNotNull(giComp.getComplexInject());
        assertTrue(giComp.getComplexInject() instanceof ComplexInjectComponent);
    }

    @Test
    public void testPriority() throws IOException
    {
        IocContext instance = Ioc.context();
        PriorityService[] prorityArr = instance.findAll(PriorityService.class);
        assertTrue(prorityArr[0] instanceof PriorityComp3);
        assertTrue(prorityArr[1] instanceof PriorityComp1);
        assertTrue(prorityArr[2] instanceof PriorityComp4);
        assertTrue(prorityArr[3] instanceof PriorityComp2);

        assertTrue(instance.find(PriorityService.class) instanceof PriorityComp3);

    }

    @Test
    public void testDefineService() throws IOException
    {
        IocContext instance = Ioc.context();

        DefaultService service = instance.find(DefaultService.class);

        assertTrue(service instanceof DefaultComponent2);
    }
}
