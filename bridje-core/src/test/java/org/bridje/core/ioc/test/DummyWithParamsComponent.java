
package org.bridje.core.ioc.test;

import org.bridje.core.ioc.annotations.Component;

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
