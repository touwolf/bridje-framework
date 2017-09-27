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

import java.util.List;

public interface SQLDialect
{
    void writeObjectName(StringBuilder builder, String name);

    void writeLimit(StringBuilder builder, int offset, int count);

    void createTable(StringBuilder builder, Table table);

    void createColumn(StringBuilder builder, List<Object> params, Column<?> column, boolean isKey);
    
    void primaryKey(StringBuilder builder, Column<?> column);

    void alterTable(StringBuilder builder, Table table);

    void addColumn(StringBuilder builder, List<Object> params, Column<?> column, boolean isLast);

    void dropColumn(StringBuilder builder, Column<?> column, boolean isLast);

    void changeColumn(StringBuilder builder, List<Object> params, Column<?> column, String oldColumn, boolean isLast);

    void createIndex(StringBuilder builder, String name, Table table, Column<?>[] columns);
    
    void createUniqueIndex(StringBuilder builder, String name, Table table, Column<?>[] columns);
    
    void dropIndex(StringBuilder builder, String name, Table table);
}
