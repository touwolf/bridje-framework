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
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.sshd.common.FactoryManagerUtils;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.io.IoServiceFactory;
import org.apache.sshd.common.io.mina.MinaServiceFactory;
import org.apache.sshd.common.io.nio2.Nio2ServiceFactory;
import org.apache.sshd.common.util.SecurityUtils;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.ServerFactoryManager;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.pubkey.AcceptAllPublickeyAuthenticator;
import org.apache.sshd.server.forward.AcceptAllForwardingFilter;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;
import org.bridje.ioc.annotations.Component;
import org.bridje.ioc.annotations.Inject;
import org.bridje.procs.ApplicationProcess;
import org.bridje.sshd.SshdConfigProvider;
import org.bridje.sshd.SshdPassAuthenticator;
import org.bridje.sshd.SshdProvider;
import org.bridje.sshd.SshdShellFactory;

/**
 *
 * @author gilberto
 */
@Component
class SshdAppProcess implements ApplicationProcess
{
    private static final Logger LOG = Logger.getLogger(SshdAppProcess.class.getName());

    private SshServer sshd;
    
    @Inject
    private SshdConfigProvider config;
    
    @Inject 
    private SshdPassAuthenticator auth;
    
    @Inject
    private SshdShellFactory shellFact;
    
    @Override
    public String getName()
    {
        return "sshd";
    }

    @Override
    public void stop()
    {
        if(sshd != null)
        {
            sshd.close(false);
        }
    }

    @Override
    public void run()
    {
        try
        {
            int port = config.getPort();
            SshdProvider provider = config.getProvider();

            if (provider == SshdProvider.MINA)
            {
                System.setProperty(IoServiceFactory.class.getName(), MinaServiceFactory.class.getName());
            }
            else if (provider == SshdProvider.NIO2)
            {
                System.setProperty(IoServiceFactory.class.getName(), Nio2ServiceFactory.class.getName());
            }

            sshd = SshServer.setUpDefaultServer();
            Map<String, Object> props = sshd.getProperties();
            FactoryManagerUtils.updateProperty(props, ServerFactoryManager.WELCOME_BANNER, config.getWelcomeBanner() + "\n");
            props.putAll(config.getOptions());
            sshd.setPort(port);
            sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(config.getSerKey()));
            sshd.setShellFactory(shellFact);
            sshd.setPasswordAuthenticator(auth);
            sshd.start();
            while(sshd.isOpen())
            {
                Thread.sleep(2000l);
            }
        }
        catch(IOException | InterruptedException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
}
