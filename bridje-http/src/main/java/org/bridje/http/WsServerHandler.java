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

package org.bridje.http;

/**
 * Represents a handler for web sockets request, this interface must be
 * implemented by components that will be responsible to handle the messages
 * send to the given path by different clients.
 */
public interface WsServerHandler
{
    /**
     * Determines when ever the given path will be handled by this web socket
     * handler or not.
     *
     * @param path The path to test for.
     * @return true this web socket handler can handle the given path, false
     * otherwise.
     */
    boolean canHandle(String path);

    /**
     * This method will be call for each text message received from any client
     * endpoint connected to the web socket managed by this handler.
     *
     * @param ch The client endpoint channel.
     * @param text The text message received.
     */
    void onText(WsChannel ch, String text);
}
