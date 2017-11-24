/*
 * Copyright 2016 Bridje Framework.
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

package org.bridje.sip;

import java.io.PrintWriter;

/**
 * Sip server service, you can inject this interface to control SIP server.
 */
public interface SipServer
{
    /**
     * Starts the SIP server, this method does not wait for the server to start
     * it returns inmediatly. The SIP server is started in a diferent thread.
     */
    void start();

    /**
     * Stops the SIP server
     */
    void stop();

    /**
     * Joins the SIP thread, and waits until the server its shutdown.
     */
    void join();

    /**
     * Prints the full list of SipBridlets that are in the class path and its
     * priorities.
     *
     * @param writer The writer to print to.
     */
    void printBridlets(PrintWriter writer);
}
