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

import java.sql.JDBCType;
import org.bridje.sql.dialect.MySQLDialect;
import org.bridje.sql.expr.SQLStatement;

public class Test
{
    public static void main(String[] args)
    {
        Table users = new Table("users");
        NumberColumn<Long> id = new NumberColumn<>(users, "id", true, JDBCType.BIGINT, Long.class, 0, 0, true, null);
        StringColumn<String> email = new StringColumn<>(users, "email", true, JDBCType.VARCHAR, String.class, 150, 0, false, null);
        StringColumn<String> password = new StringColumn<>(users, "password", true, JDBCType.VARCHAR, String.class, 512, 0, false, null);
        BooleanColumn<Boolean> active = new BooleanColumn<>(users, "active", true, JDBCType.BIT, Boolean.class, 0, 0, false, null);
        MySQLDialect mysql = new MySQLDialect();
        SQLStatement stmt = SQL.createTable(users)
                                    .column(id)
                                    .column(email)
                                    .column(password)
                                    .column(active)
                                    .primaryKey(id)
                                    .toSQL(mysql);
        System.out.println(stmt);
    }
}
