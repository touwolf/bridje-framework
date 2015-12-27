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

package org.bridje.core.impl.procs;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.bridje.core.ioc.annotations.Component;
import org.bridje.core.ioc.annotations.Inject;
import org.bridje.core.procs.ApplicationProcess;
import org.bridje.core.procs.ProcessInstance;
import org.bridje.core.procs.ProcessService;

/**
 *
 */
@Component
class ProcessServiceImpl implements ProcessService
{
    @Inject
    private ApplicationProcess[] appProcess;

    private ProcessInstance[] allProcess;

    private Map<String, ProcessInstance> procMap;

    @PostConstruct
    public void init()
    {
        procMap = new HashMap<>();
        if (appProcess == null)
        {
            appProcess = new ApplicationProcess[]
            {
            };
        }
        allProcess = new ProcessInstance[appProcess.length];
        for (int i = 0; i < appProcess.length; i++)
        {
            allProcess[i] = new ProcessInstanceImpl(appProcess[i]);
            procMap.put(allProcess[i].getName(), allProcess[i]);
        }
    }

    @Override
    public ProcessInstance[] getAllProcess()
    {
        return allProcess;
    }

    @Override
    public ProcessInstance findProcess(String name)
    {
        return procMap.get(name);
    }

    @Override
    public void startAll()
    {
        for (ProcessInstance process : allProcess)
        {
            if (!process.isRunning())
            {
                process.start();
            }
        }
    }

    @Override
    public void shutdown()
    {
        for (ProcessInstance process : allProcess)
        {
            if (process.isRunning())
            {
                process.stop();
            }
        }
    }

    @Override
    public void join() throws InterruptedException
    {
        for (ProcessInstance process : allProcess)
        {
            if (process.isRunning())
            {
                process.join();
            }
        }
    }

}
