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

public interface SQLDialect
{
    boolean canHandle(Connection connection);

    void writeObjectName(StringBuilder builder, String name);

    void writeLimit(StringBuilder builder, int offset, int count);

    String createTable(Table table, List<Object> params);

    String addColumn(Column<?> column, List<Object> params);

    String dropColumn(Column<?> column, List<Object> params);

    String changeColumn(String oldName, Column<?> column, List<Object> params);

    String createIndex(Index index, List<Object> params);

    String dropIndex(Index index, List<Object> params);

    String createForeignKey(ForeignKey fk, List<Object> params);

    String dropForeignKey(ForeignKey fk, List<Object> params);
}
