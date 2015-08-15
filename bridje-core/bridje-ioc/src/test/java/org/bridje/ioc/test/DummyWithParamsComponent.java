
package org.bridje.ioc.test;

import org.bridje.ioc.annotations.Component;

@Component
public class DummyWithParamsComponent
{
    private DummyComponent dummyComponent;
    
    public DummyWithParamsComponent(DummyComponent dummyComponent)
    {
        this.dummyComponent = dummyComponent;
    }
}
