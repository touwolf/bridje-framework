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

package org.bridje.orm.srcgen.model;

/**
 * This interface represents a custom data type provider, it must be implemente
 * by all components that provices custom data type to the source code
 * generation proccess.
 */
public interface CustomTypesProvider
{
    /**
     * Gets the java type of the given (by its name) custom type.
     *
     * @param type The name of the custom type.
     *
     * @return The full java type name to be use in the source code generarion
     *         for the given custom type.
     */
    public String getJavaType(String type);

    /**
     * Gets the type of column that the custom type field must use. This is the
     * name of a class that extends TableColumn class, ex: TableStringColumn or
     * TableNumberColumn, etc...
     *
     * @param type The name of the custom type.
     *
     * @return The name of the class for the column of the given custom type.
     */
    public String getColumnClass(String type);

}
