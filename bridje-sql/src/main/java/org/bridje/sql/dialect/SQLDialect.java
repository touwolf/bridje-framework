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

package org.bridje.sql.dialect;

import java.util.List;
import org.bridje.sql.Column;
import org.bridje.sql.Table;

public interface SQLDialect
{
    void writeObjectName(StringBuilder builder, String name);

    void writeLimit(StringBuilder builder, int offset, int count);

    void addColumn(StringBuilder builder, Column<?> column);

    void createTable(StringBuilder builder, Table table);

    void createColumn(StringBuilder builder, List<Object> params, Column<?> column, boolean isKey);
    
    void primaryKey(StringBuilder builder, Column<?> column);

    void alterTable(StringBuilder builder, Table table);

    void dropColumn(StringBuilder builder, Column<?> column);

    void alterColumn(StringBuilder builder, Column<?> column);
}
