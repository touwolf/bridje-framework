
package org.bridje.ioc.impl;

import java.io.IOException;
import java.util.logging.Logger;
import org.bridje.ioc.annotations.IocContext;

class IocContextImpl implements IocContext
{
    private static final Logger LOG = Logger.getLogger(IocContextImpl.class.getName());
    
    private final String scope;

    private final ClassList componensClasses;

    private final ComponentCreator componentCreator;
    
    private final ServiceMap serviceMap;
    
    private final IocContainer container;

    public IocContextImpl(String scope) throws IOException
    {
        this.scope = scope;
        componentCreator = new ComponentCreator(this);
        componensClasses = ClassList.loadFromClassPath(scope);
        serviceMap = new ServiceMap(componensClasses);
        container = new IocContainer(this, componentCreator);
    }

    @Override
    public <T> T find(Class<T> service)
    {
        Class<? extends T> component = serviceMap.findOne(service);
        if(component != null)
        {
            return container.create(component);
        }
        return null;
    }

    @Override
    public boolean existsComponent(Class cls)
    {
        return componensClasses.contains(cls);
    }
}
