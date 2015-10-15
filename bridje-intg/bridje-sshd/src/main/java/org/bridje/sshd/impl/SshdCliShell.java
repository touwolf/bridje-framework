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
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.cli.CliService;
import org.bridje.cli.CliSession;
import org.bridje.cli.exceptions.InvalidCliCommandException;
import org.bridje.cli.exceptions.NoCliParserException;
import org.bridje.cli.exceptions.NoSuchCommandException;
import org.bridje.ioc.Ioc;

/**
 *
 * @author gilberto
 */
class SshdCliShell implements Runnable
{
    private static final Logger LOG = Logger.getLogger(SshdCliShell.class.getName());

    private final CliSession session;

    public SshdCliShell(CliSession session)
    {
        this.session = session;
    }
    
    @Override
    public void run()
    {
        try
        {
            printPront();
            ByteBuffer byteBuf = ByteBuffer.allocate(256);
            byte[] buff = new byte[2];
            int c;
            while( (c = session.getIn().read(buff)) > 0)
            {
                if(buff[0] == '\r')
                {
                    session.getOut().write("\n\r".getBytes());
                    session.getOut().flush();
                    String line = new String(byteBuf.array(), "ASCII");
                    try
                    {
                        System.out.println(line);
                        if(!line.trim().isEmpty())
                        {
                            Ioc.context().find(CliService.class).execute(line, session);
                            session.getOut().write("\n\r".getBytes());
                        }
                    }
                    catch(NoCliParserException | InvalidCliCommandException | NoSuchCommandException ex)
                    {
                        session.getErr().print(ex.getMessage());
                        session.getErr().print("\n\r");
                        session.getErr().flush();
                    }
                    byteBuf = ByteBuffer.allocate(256);
                    printPront();
                }
                else
                {
                    byteBuf.put(buff, 0, c);
                    session.getOut().write(buff, 0, c);
                    session.getOut().flush();
                }
            }
        }
        catch(IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private void printPront()
    {
        session.getOut().print("> ");
        session.getOut().flush();
    }
}
