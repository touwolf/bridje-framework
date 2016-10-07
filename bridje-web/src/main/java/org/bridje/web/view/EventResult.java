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

package org.bridje.web.view;

import javax.xml.bind.annotation.XmlTransient;
import org.bridje.web.view.widgets.UIEvent;

/**
 * The result of an event invocation.
 */
@XmlTransient
public class EventResult
{
    private final UIEvent event;

    private final EventResultType type;

    private final String message;

    private final Exception exception;

    private final Object data;

    public EventResult(UIEvent event, EventResultType type, String message, Object data, Exception exception)
    {
        this.event = event;
        this.type = type;
        this.message = message;
        this.data = data;
        this.exception = exception;
    }

    /**
     * The invoked event.
     *
     * @return The UIEvent object for the event.
     */
    public UIEvent getEvent()
    {
        return event;
    }

    /**
     * The type of the message that must be show to the user as the result of
     * the invocation. SUCCESS, INFO, WARNING, ERROR.
     *
     * @return An EventResultType object.
     */
    public EventResultType getType()
    {
        return type;
    }

    /**
     * The message to be show as the result of the event invocation.
     *
     * @return An string with the message.
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * The exception (if any) that was throw by the event method.
     * 
     * @return An Exception or null if not Exception where throw by the event method.
     */
    public Exception getException()
    {
        return exception;
    }

    /**
     * The result object returned by the event method.
     * 
     * @return An Object representing the result of the event method invocation.
     */
    public Object getData()
    {
        return data;
    }
}
