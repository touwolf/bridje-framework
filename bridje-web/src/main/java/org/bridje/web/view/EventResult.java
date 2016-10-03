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

@XmlTransient
public class EventResult
{
    private final EventResultType type;

    private final String message;
    
    private final Exception exception;
    
    private final Object data;

    public EventResult(EventResultType type, String message, Object data, Exception exception)
    {
        this.type = type;
        this.message = message;
        this.data = data;
        this.exception = exception;
    }
    
    public EventResultType getType()
    {
        return type;
    }

    public String getMessage()
    {
        return message;
    }

    public Exception getException()
    {
        return exception;
    }

    public Object getData()
    {
        return data;
    }
}
