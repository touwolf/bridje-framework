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

import org.bridje.core.procs.ApplicationProcess;
import org.bridje.core.procs.ProcessInstance;

/**
 *
 */
class ProcessInstanceImpl implements ProcessInstance
{
    private Thread procThread;
    
    private final ApplicationProcess target;

    public ProcessInstanceImpl(ApplicationProcess target)
    {
        this.target = target;
    }

    @Override
    public String getName()
    {
        return target.getName();
    }

    @Override
    public boolean isRunning()
    {
        return procThread != null && procThread.isAlive();
    }

    @Override
    public void start()
    {
        if(!isRunning())
        {
            if(procThread == null)
            {
                procThread = new Thread(target);
            }
            procThread.start();
        }
    }

    @Override
    public void stop()
    {
        if(isRunning())
        {
            target.stop();
        }
        if(!isRunning() && procThread == null)
        {
            procThread = null;
        }
    }

    @Override
    public void join() throws InterruptedException
    {
        if(isRunning())
        {
            procThread.join();
        }
    }
}
