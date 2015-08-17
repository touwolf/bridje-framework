
package org.bridje.ioc.test;

import org.bridje.ioc.annotations.Component;

@Component
public class DummyWithParamsComponent
{
    private final DummyComponent dummyComponent;
    
    public DummyWithParamsComponent(DummyComponent dummyComponent)
    {
        this.dummyComponent = dummyComponent;
    }

    public DummyComponent getDummyComponent()
    {
        return dummyComponent;
    }
}
