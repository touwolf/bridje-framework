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
 * A field status information object that presents the status and message for the given field.
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

    /**
     * The name of the field.
     * 
     * @return The name of the field.
     */
    public String getFieldName()
    {
        return fieldName;
    }

    /**
     * The status of this field.
     * 
     * @return The status of this field.
     */
    public EventResultType getStatus()
    {
        return status;
    }

    /**
     * Returns the message for this field.
     * 
     * @return The message for this field.
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Creates a field status information object with error status.
     * 
     * @param fieldName The name of the field.
     * @return The new FieldStatusInf object.
     */
    public static FieldStatusInf error(String fieldName)
    {
        return custom(fieldName, EventResultType.ERROR, null);
    }

    /**
     * Creates a field status information object with warn status.
     * 
     * @param fieldName The name of the field.
     * @return The new FieldStatusInf object.
     */
    public static FieldStatusInf warn(String fieldName)
    {
        return custom(fieldName, EventResultType.WARNING, null);
    }

    /**
     * Creates a field status information object with success status.
     * 
     * @param fieldName The name of the field.
     * @return The new FieldStatusInf object.
     */
    public static FieldStatusInf success(String fieldName)
    {
        return custom(fieldName, EventResultType.SUCCESS, null);
    }

    /**
     * Creates a field status information object with info status.
     * 
     * @param fieldName The name of the field.
     * @return The new FieldStatusInf object.
     */
    public static FieldStatusInf info(String fieldName)
    {
        return custom(fieldName, EventResultType.INFO, null);
    }
    
    /**
     * Creates a field status information object with error status.
     * 
     * @param fieldName The name of the field.
     * @param message
     * @return The new FieldStatusInf object.
     */
    public static FieldStatusInf error(String fieldName, String message)
    {
        return custom(fieldName, EventResultType.ERROR, message);
    }

    /**
     * Creates a field status information object with warn status.
     * 
     * @param fieldName The name of the field.
     * @param message
     * @return The new FieldStatusInf object.
     */
    public static FieldStatusInf warn(String fieldName, String message)
    {
        return custom(fieldName, EventResultType.WARNING, message);
    }

    /**
     * Creates a field status information object with success status.
     * 
     * @param fieldName The name of the field.
     * @param message
     * @return The new FieldStatusInf object.
     */
    public static FieldStatusInf success(String fieldName, String message)
    {
        return custom(fieldName, EventResultType.SUCCESS, message);
    }

    /**
     * Creates a field status information object with info status.
     * 
     * @param fieldName The name of the field.
     * @param message The message of the field.
     * @return The new FieldStatusInf object.
     */
    public static FieldStatusInf info(String fieldName, String message)
    {
        return custom(fieldName, EventResultType.INFO, message);
    }

    /**
     * Creates a field status information object with the given status an the given messages for the given field.
     * 
     * @param fieldName The name of the field.
     * @param status The status of the field.
     * @param message The message of the field.
     * @return The new FieldStatusInf object.
     */
    public static FieldStatusInf custom(String fieldName, EventResultType status, String message)
    {
        return new FieldStatusInf(fieldName, status, message);
    }
}
