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

import java.io.IOException;

/**
 * Represents a handler of SIP request, this interface must be implemented by
 * components that will be call in chain to handle the request. Handlers must
 * provide a Priority greater than 0 and it is recommended that it is used with
 * the @InjectNext annotation to create a chain of handlers that can handler
 * properly all SIP request for the application.
 */
public interface SipBridlet
{
    /**
     * Handles the SIP request, This method must call the same method from the
     * next handler if it exists an return it's result or it can handle the
     * request it self and return true in that case.
     *
     * @param reqMsg The sip message received.
     *
     * @return The response message to be sent to the sender in response to the
     *         request message received.
     *
     * @throws IOException                 If any IOException occurs during this
     *                                     call.
     * @throws org.bridje.sip.SipException An SIP exception with the code and
     *                                     message that must be return to the
     *                                     client.
     */
    SipResponse handle(SipRequest reqMsg) throws IOException, SipException;

}
