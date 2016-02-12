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

package org.bridje.web;

import javax.servlet.Servlet;

/**
 * Represents a web container this interface it´s mean to be implemented in
 * order to provide the functionality of a http servlets and web sockets
 * container.
 */
public interface WebContainer
{
    /**
     * Adds an http servlet to the container. This method should read the http
     * servlet especification from its annotations.
     *
     * @param servlet The servlet to be deploy in the container.
     */
    void addServlet(Servlet servlet);

    /**
     * Remove an http servlet from the container. The WebContainer instance must
     * keep track of all the Servlets that has being deploy on it and remove the
     * specified servlet when this method is call.
     *
     * @param servlet The servlet to be remove from the container.
     */
    void removeServlet(Servlet servlet);

    /**
     * This method should register a the especified class as a web socket server end point
     * in the container. The specification should be readed from the annotations of the web socket class.
     * 
     * @param cls The web socket class to be register in the container.
     * @param name The name of the web socket.
     * @param pathSpec The path specification for this web socket.
     */
    void registerWebSocket(Class<?> cls, String name, String pathSpec);

    /**
     * This method should register a the especified class as a web socket server end point
     * in the container. The specification should be readed from the annotations of the web socket class.
     * 
     * @param cls The web socket class to be register in the container.
     * @param name The name of the web socket.
     * @param pathSpec The path specifications for this web socket.
     */
    void registerWebSocket(Class<?> cls, String name, String[] pathSpec);

    /**
     * Remove a previous register web socket class from the container, 
     * the WebContainer implementation should keep track of all the web socket 
     * classes that it has register and this method should remove the web socket 
     * registration.
     * 
     * @param cls 
     */
    void unregisterWebSocket(Class<?> cls);

    /**
     * Starts the web container.
     */
    void start();

    /**
     * Stops the web container.
     */
    void stop();

    /**
     * Joins the web container thread.
     */
    void join();
}
