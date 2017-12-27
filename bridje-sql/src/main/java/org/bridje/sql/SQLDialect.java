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
 * Represents an SQL dialect for any especific RDBM or version.
 */
public interface SQLDialect
{
    /**
     * Determines if this dialect can handle the given connection.
     *
     * @param connection The connection to test.
     *
     * @return true if this dialect can handle the connection, false otherwise.
     */
    boolean canHandle(Connection connection);

    /**
     * Writes the name of the object with this dialect rules for names.
     *
     * @param builder The string builder for the query.
     * @param name    The name to write.
     */
    void writeObjectName(StringBuilder builder, String name);

    /**
     * Writes a limit statement.
     *
     * @param builder The string builder for the query.
     * @param offset  The offset for the limit.
     * @param count   The quantity of records to return.
     */
    void writeLimit(StringBuilder builder, int offset, int count);

    /**
     * Writes a limit statement.
     *
     * @param builder The string builder for the query.
     * @param count   The quantity of records to return.
     */
    void writeLimit(StringBuilder builder, int count);

    /**
     * Creates the given table DDLs.
     *
     * @param table  The table to create.
     * @param params The params
     *
     * @return The DDL statement.
     */
    String createTable(Table table, List<Object> params);

    /**
     * Creates the given column add DDLs.
     * 
     * @param column The column to add.
     * @param params The list of parameters.
     *
     * @return The DDL statement.
     */
    String addColumn(Column<?, ?> column, List<Object> params);

    /**
     * Creates the given column drop DDLs.
     *
     * @param column The colum to drop.
     * @param params The list of parameters.
     *
     * @return The DDL statement.
     */
    String dropColumn(Column<?, ?> column, List<Object> params);

    /**
     * Creates the given column change DDLs.
     *
     * @param oldName The old name of the column.
     * @param column The colum to change.
     * @param params The list of parameters.
     * 
     * @return The DDL statement.
     */
    String changeColumn(String oldName, Column<?, ?> column, List<Object> params);

    /**
     * Creates the given index DDLs.
     *
     * @param index The index to create.
     * @param params The list of parameters.
     *
     * @return The DDL statement.
     */
    String createIndex(Index index, List<Object> params);

    /**
     * Creates the given index drop DDLs.
     *
     * @param index The index to drop.
     * @param params The list of parameters.
     *
     * @return The DDL statement.
     */
    String dropIndex(Index index, List<Object> params);

    /**
     * Creates the given foreign key DDLs.
     *
     * @param fk The foreign key to create.
     * @param params The list of parameters.
     *
     * @return The DDL statement.
     */
    String createForeignKey(ForeignKey fk, List<Object> params);

    /**
     * Creates the given foreign key drop DDLs.
     *
     * @param fk The foreign key to drop.
     * @param params The list of parameters.
     *
     * @return The DDL statement.
     */
    String dropForeignKey(ForeignKey fk, List<Object> params);

}
