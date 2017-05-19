/*
 * Copyright 2017 Bridje Framework.
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

/**
 * 
 */
public class FieldStatusInf
{
    private final String fieldName;
    
    private final EventResultType status;
    
    private final String message;

    private FieldStatusInf(String fieldName, EventResultType status, String message)
    {
        this.fieldName = fieldName;
        this.status = status;
        this.message = message;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public EventResultType getStatus()
    {
        return status;
    }

    public String getMessage()
    {
        return message;
    }

    public static FieldStatusInf error(String fieldName)
    {
        return custom(fieldName, EventResultType.ERROR, null);
    }

    public static FieldStatusInf warn(String fieldName)
    {
        return custom(fieldName, EventResultType.WARNING, null);
    }

    public static FieldStatusInf success(String fieldName)
    {
        return custom(fieldName, EventResultType.SUCCESS, null);
    }

    public static FieldStatusInf info(String fieldName)
    {
        return custom(fieldName, EventResultType.INFO, null);
    }
    
    public static FieldStatusInf error(String fieldName, String message)
    {
        return custom(fieldName, EventResultType.ERROR, message);
    }

    public static FieldStatusInf warn(String fieldName, String message)
    {
        return custom(fieldName, EventResultType.WARNING, message);
    }

    public static FieldStatusInf success(String fieldName, String message)
    {
        return custom(fieldName, EventResultType.SUCCESS, message);
    }

    public static FieldStatusInf info(String fieldName, String message)
    {
        return custom(fieldName, EventResultType.INFO, message);
    }

    public static FieldStatusInf custom(String fieldName, EventResultType status, String message)
    {
        return new FieldStatusInf(fieldName, status, message);
    }
}
