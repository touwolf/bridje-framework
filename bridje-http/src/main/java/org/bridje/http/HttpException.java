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
 * Exception to be use by the HTTP server to return an status code and message
 * to the client.
 */
public class HttpException extends Exception
{
    private final int status;

    /**
     * Status code only constructor. Message will be null.
     * 
     * @param status The status code to return to the client, must be a valid HTTP status code.
     */
    public HttpException(int status)
    {
        this.status = status;
    }

    /**
     * Status code an message constructor.
     * 
     * @param status The status code to return to the client, must be a valid HTTP status code.
     * @param message The message for this exception.
     */
    public HttpException(int status, String message)
    {
        super(message);
        this.status = status;
    }

    /**
     * All fields constructor.
     * 
     * @param status The status code to return to the client, must be a valid HTTP status code.
     * @param message The message for this exception.
     * @param cause The cause for this exception.
     */
    public HttpException(int status, String message, Throwable cause)
    {
        super(message, cause);
        this.status = status;
    }

    /**
     * Status and cause constructor.
     * 
     * @param status The status code to return to the client, must be a valid HTTP status code.
     * @param cause The cause for this exception.
     */
    public HttpException(int status, Throwable cause)
    {
        super(cause);
        this.status = status;
    }

    /**
     * The status code to return to the client, must be a valid HTTP status code.
     * 
     * @return An integer that represents the HTTP status code.
     */
    public int getStatus()
    {
        return status;
    }
}
