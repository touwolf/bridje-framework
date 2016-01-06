
package org.bridje.ioc.test;

import java.util.List;
import java.util.Map;
import org.bridje.ioc.Component;
import org.bridje.ioc.Construct;

@Component
public class DummyWithAnnotatedConstruct
{
    private DummyComponent dummyComponent;
    
    private GenericService<Map<String,List<Integer>>> complexInject;

    public DummyWithAnnotatedConstruct(DummyComponent dummyComponent)
    {
        this.dummyComponent = dummyComponent;
    }

    @Construct
    public DummyWithAnnotatedConstruct(GenericService<Map<String,List<Integer>>> complexInject)
    {
        this.complexInject = complexInject;
    }

    public DummyWithAnnotatedConstruct()
    {
    }
    
    public DummyComponent getDummyComponent()
    {
        return dummyComponent;
    }

    public GenericService<Map<String, List<Integer>>> getComplexInject()
    {
        return complexInject;
    }
}
