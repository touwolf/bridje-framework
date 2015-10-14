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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.sshd.common.util.threads.ThreadUtils;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.SessionAware;
import org.apache.sshd.server.session.ServerSession;
import org.bridje.cli.CliService;
import org.bridje.ioc.Ioc;

/**
 *
 * @author gilberto
 */
class SshdCliShell implements Command, SessionAware
{
    private static final Logger LOG = Logger.getLogger(SshdCliShell.class.getName());

    /**
     * default buffer size for the IO pumps.
     */
    public static final int DEFAULT_BUFFER_SIZE = 8192;

    private final Executor executor;

    private final int bufferSize;

    private InputStream in;

    private OutputStream out;

    private OutputStream err;

    private ExitCallback callback;

    private boolean shutdownExecutor;

    public SshdCliShell()
    {
        this(DEFAULT_BUFFER_SIZE);
    }

    public SshdCliShell(Executor executor)
    {
        this(executor, DEFAULT_BUFFER_SIZE);
    }

    public SshdCliShell(int bufferSize)
    {
        this(ThreadUtils.newSingleThreadExecutor("shell[" + UUID.randomUUID().toString() + "]"),
                true,
                bufferSize);
    }

    public SshdCliShell(Executor executor, int bufferSize)
    {
        this(executor, false, bufferSize);
    }

    public SshdCliShell(Executor executor, boolean shutdownExecutor, int bufferSize)
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
    }

    @Override
    public void setErrorStream(OutputStream err)
    {
        this.err = err;
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
        executor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    PrintWriter w = new PrintWriter(out);
                    PrintWriter ew = new PrintWriter(err);
                    ByteBuffer byteBuf = ByteBuffer.allocate(256);
                    byte[] buff = new byte[2];
                    int c = 0;
                    while( (c = in.read(buff)) > 0)
                    {
                        if(buff[0] == '\r')
                        {
                            out.write("\n\r".getBytes());
                            out.flush();
                            String line = new String(byteBuf.array());
                            try
                            {
                                System.out.println(line);
                                if(line != null && !line.trim().isEmpty())
                                {
                                    Ioc.context().find(CliService.class).execute(line.split(" "));
                                }
                            }
                            catch(Exception ex)
                            {
                                ew.print(ex.getMessage());
                                ew.print("\n\r");
                                ew.flush();
                            }
                            byteBuf.clear();
                        }
                        else
                        {
                            byteBuf.put(buff, 0, c);
                            out.write(buff, 0, c);
                            out.flush();
                        }
                    }
                }
                catch(Exception ex)
                {
                    LOG.log(Level.SEVERE, ex.getMessage(), ex);
                }
                /*
                try
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));

                    while(true)
                    {

                    }
                }
                catch(Exception ex)
                {
                    LOG.log(Level.SEVERE, ex.getMessage(), ex);
                }
                */
            }
        });
    }

    @Override
    public synchronized void destroy()
    {
        if (shutdownExecutor && executor instanceof ExecutorService)
        {
            ((ExecutorService) executor).shutdown();
        }
    }
}
