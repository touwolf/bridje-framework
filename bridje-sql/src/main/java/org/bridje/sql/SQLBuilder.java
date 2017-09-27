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

public interface SQLBuilder
{
    boolean isSimpleColumnNames();

    void setSimpleColumnNames(boolean simpleColumnNames);

    SQLDialect getDialect();

    List<Object> getParameters();

    SQLBuilder append(String str);

    SQLBuilder append(CharSequence s);

    SQLBuilder append(char c);

    SQLBuilder append(int i);

    SQLBuilder append(long lng);

    SQLBuilder append(float f);

    SQLBuilder append(double d);

    SQLBuilder appendObjectName(String name);

    SQLBuilder appendLimit(int offset, int count);

    SQLBuilder append(SQLWritable expression);

    void appendAll(SQLWritable[] expressions, String sep);

    SQLBuilder appendCreateTable(Table table);

    SQLBuilder appendCreateColumn(Column<?> column, boolean isKey);

    SQLBuilder appendPrimaryKey(Column<?> column);

    SQLBuilder appendAlterTable(Table table);

    SQLBuilder appendAddColumn(Column<?> column, boolean isLast);

    SQLBuilder appendDropColumn(Column<?> column, boolean isLast);

    SQLBuilder appendChangeColumn(Column<?> column, String oldColumn, boolean isLast);

    SQLBuilder appendCreateIndex(String name, Table table, Column<?>[] columns);

    SQLBuilder appendCreateUniqueIndex(String name, Table table, Column<?>[] columns);

    SQLBuilder appendDropIndex(String name, Table table);
}
