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

package org.bridje.sshd.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;
import org.apache.sshd.common.util.threads.ThreadUtils;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.SessionAware;
import org.apache.sshd.server.session.ServerSession;
import org.bridje.cli.CliSession;

/**
 *
 * @author gilberto
 */
class SshdShellCommand implements Command, SessionAware, CliSession
{
    private static final Logger LOG = Logger.getLogger(SshdShellCommand.class.getName());

    /**
     * default buffer size for the IO pumps.
     */
    public static final int DEFAULT_BUFFER_SIZE = 8192;

    private final Executor executor;

    private final int bufferSize;

    private InputStream in;

    private OutputStream out;

    private OutputStream err;
    
    private PrintStream pout;

    private PrintStream perr;

    private ExitCallback callback;

    private boolean shutdownExecutor;

    public SshdShellCommand()
    {
        this(DEFAULT_BUFFER_SIZE);
    }

    public SshdShellCommand(Executor executor)
    {
        this(executor, DEFAULT_BUFFER_SIZE);
    }

    public SshdShellCommand(int bufferSize)
    {
        this(ThreadUtils.newSingleThreadExecutor("shell[" + UUID.randomUUID().toString() + "]"),
                true,
                bufferSize);
    }

    public SshdShellCommand(Executor executor, int bufferSize)
    {
        this(executor, false, bufferSize);
    }

    public SshdShellCommand(Executor executor, boolean shutdownExecutor, int bufferSize)
    {
        this.executor = executor;
        this.bufferSize = bufferSize;
        this.shutdownExecutor = shutdownExecutor;
    }

    @Override
    public void setInputStream(InputStream in)
    {
        this.in = in;
    }

    @Override
    public void setOutputStream(OutputStream out)
    {
        this.out = out;
        this.pout = new PrintStream(out);
    }

    @Override
    public void setErrorStream(OutputStream err)
    {
        this.err = err;
        this.perr = new PrintStream(err);
    }

    @Override
    public void setExitCallback(ExitCallback callback)
    {
        this.callback = callback;
    }

    @Override
    public void setSession(ServerSession session)
    {
    }

    @Override
    public synchronized void start(Environment env) throws IOException
    {
        executor.execute(new SshdCliShell(this));
    }

    @Override
    public synchronized void destroy()
    {
        if (shutdownExecutor && executor instanceof ExecutorService)
        {
            ((ExecutorService) executor).shutdown();
        }
    }

    @Override
    public InputStream getIn()
    {
        return in;
    }

    @Override
    public PrintStream getOut()
    {
        return pout;
    }

    @Override
    public PrintStream getErr()
    {
        return perr;
    }
}
