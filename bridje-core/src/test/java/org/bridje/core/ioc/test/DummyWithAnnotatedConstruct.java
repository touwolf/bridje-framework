
package org.bridje.core.ioc.test;

import java.util.List;
import java.util.Map;
import org.bridje.core.ioc.annotations.Component;
import org.bridje.core.ioc.annotations.Construct;

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
