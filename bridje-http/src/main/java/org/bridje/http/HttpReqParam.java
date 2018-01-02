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
 * Represents an HTTP POST or GET parameter sent to the server.
 */
public interface HttpReqParam
{
    /**
     * The name of this parameter.
     *
     * @return An string representing the name.
     */
    String getName();

    /**
     * Determines when ever this parameter has a single value. This method will
     * return the contrary of the isMultiple method.
     *
     * @return true if only one value were sent to the server.
     */
    boolean isSingle();

    /**
     * Determines when ever this parameter has multiple values, this is if the
     * client sent several values with the name of this parameter. In this case
     * the getAllValues will return all of this values. This method will return
     * the contrary of the isSingle method.
     *
     * @return true if several values were sent to the server with the name of
     *         this parameter.
     */
    boolean isMultiple();

    /**
     * Gets the first value sent to the server with the name of this parameter.
     *
     * @return The string representation of the value.
     */
    String getFirst();
    
    /**
     * 
     * @return 
     */
    String getValue();

    /**
     * 
     * @return 
     */
    String popValue();

    /**
     * 
     */
    void reset();

    /**
     * If this parameter is mulitple this method will return all of the values
     * that were sent to the server under it´s name.
     *
     * @return The string array with all the values for this parameter.
     */
    String[] getAllValues();

    /**
     * Determines if the first value sent to the server is an empty string.
     *
     * @return true the first value sent to the server is an empty string, false
     *         otherwise.
     */
    boolean isEmpty();
}
