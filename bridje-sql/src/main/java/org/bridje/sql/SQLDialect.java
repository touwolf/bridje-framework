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

package org.bridje.sql;

import java.sql.Connection;
import java.util.List;

/**
 * 
 */
public interface SQLDialect
{
    /**
     * 
     * @param connection
     * @return 
     */
    boolean canHandle(Connection connection);

    /**
     * 
     * @param builder
     * @param name 
     */
    void writeObjectName(StringBuilder builder, String name);

    /**
     * 
     * @param builder
     * @param offset
     * @param count 
     */
    void writeLimit(StringBuilder builder, int offset, int count);

    /**
     * 
     * @param table
     * @param params
     * @return 
     */
    String createTable(Table table, List<Object> params);

    /**
     * 
     * @param column
     * @param params
     * @return 
     */
    String addColumn(Column<?, ?> column, List<Object> params);

    /**
     * 
     * @param column
     * @param params
     * @return 
     */
    String dropColumn(Column<?, ?> column, List<Object> params);

    /**
     * 
     * @param oldName
     * @param column
     * @param params
     * @return 
     */
    String changeColumn(String oldName, Column<?, ?> column, List<Object> params);

    /**
     * 
     * @param index
     * @param params
     * @return 
     */
    String createIndex(Index index, List<Object> params);

    /**
     * 
     * @param index
     * @param params
     * @return 
     */
    String dropIndex(Index index, List<Object> params);

    /**
     * 
     * @param fk
     * @param params
     * @return 
     */
    String createForeignKey(ForeignKey fk, List<Object> params);

    /**
     * 
     * @param fk
     * @param params
     * @return 
     */
    String dropForeignKey(ForeignKey fk, List<Object> params);
}
