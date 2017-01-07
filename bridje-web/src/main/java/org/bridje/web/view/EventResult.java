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
import org.bridje.ioc.thls.Thls;
import org.bridje.web.view.controls.UIEvent;

/**
 * The result of the invocation of a web event.
 */
@XmlTransient
public class EventResult
{
    private final UIEvent event;

    private final EventResultType type;

    private final String message;

    private final Exception exception;

    private final Object data;

    /**
     * Creates a new info event result object, that will show an information
     * type message to the end user.
     *
     * @param message The message to show to the user.
     * @param data    The data to pass to the web view as the result of the
     *                event.
     *
     * @return An EventResult object of type info.
     */
    public static EventResult info(String message, Object data)
    {
        return new EventResult(EventResultType.INFO, message, data, null);
    }

    /**
     * Creates a new info event result object, that will show an information
     * type message to the end user.
     *
     * @param message The message to show to the user.
     *
     * @return An EventResult object of type info.
     */
    public static EventResult info(String message)
    {
        return new EventResult(EventResultType.INFO, message, null, null);
    }

    /**
     * Creates a new success event result object, that will show an success type
     * message to the end user.
     *
     * @param message The message to show to the user.
     * @param data    The data to pass to the web view as the result of the
     *                event.
     *
     * @return An EventResult object of type success.
     */
    public static EventResult success(String message, Object data)
    {
        return new EventResult(EventResultType.SUCCESS, message, data, null);
    }
    
    /**
     * Creates a new success event result object, that will show an success type
     * message to the end user.
     *
     * @param message The message to show to the user.
     * 
     * @return An EventResult object of type success.
     */
    public static EventResult success(String message)
    {
        return new EventResult(EventResultType.SUCCESS, message, null, null);
    }

    /**
     * Creates a new warning event result object, that will show an warning type
     * message to the end user.
     *
     * @param message The message to show to the user.
     * @param data    The data to pass to the web view as the result of the
     *                event.
     *
     * @return An EventResult object of type warning.
     */
    public static EventResult warn(String message, Object data)
    {
        return new EventResult(EventResultType.WARNING, message, data, null);
    }


    /**
     * Creates a new warning event result object, that will show an warning type
     * message to the end user.
     *
     * @param message The message to show to the user.
     *
     * @return An EventResult object of type warning.
     */
    public static EventResult warn(String message)
    {
        return new EventResult(EventResultType.WARNING, message, null, null);
    }

    /**
     * Creates a new warning event result object, that will show an warning type
     * message to the end user.
     *
     * @param message   The message to show to the user.
     * @param data      The data to pass to the web view as the result of the
     *                  event.
     * @param exception The exception responsable for this warning.
     *
     * @return An EventResult object of type warning.
     */
    public static EventResult warn(String message, Object data, Exception exception)
    {
        return new EventResult(EventResultType.WARNING, message, data, null);
    }

    /**
     * Creates a new error event result object, that will show an error type
     * message to the end user.
     *
     * @param message   The message to show to the user.
     * @param exception The exception responsable for this error.
     *
     * @return An EventResult object of type error.
     */
    public static EventResult error(String message, Exception exception)
    {
        return new EventResult(EventResultType.ERROR, message, null, exception);
    }

    /**
     * Creates a new error event result object, that will show an error type
     * message to the end user.
     *
     * @param message   The message to show to the user.
     *
     * @return An EventResult object of type error.
     */
    public static EventResult error(String message)
    {
        return new EventResult(EventResultType.ERROR, message, null, null);
    }

    /**
     * Creates a new event result, for the given type, that will show a message
     * to the end user.
     *
     * @param type      The type of message to show to the end user.
     * @param message   The message to show to the user.
     * @param data      The data to pass to the web view as the result of the
     *                  event.
     * @param exception The exception responsable for this error.
     *
     * @return An EventResult object of type error.
     */
    public static EventResult of(EventResultType type, String message, Object data, Exception exception)
    {
        return new EventResult(type, message, data, exception);
    }

    private EventResult(EventResultType type, String message, Object data, Exception exception)
    {
        this.event = Thls.get(UIEvent.class);
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
     * @return An Exception or null if not Exception where throw by the event
     *         method.
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
