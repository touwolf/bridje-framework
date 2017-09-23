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
        Table users = SQL.buildTable("users")
                            .number("id", SQL.buildType(Long.class, JDBCType.BIGINT), false, true, null)
                            .string("email", SQL.buildType(String.class, JDBCType.VARCHAR, 150), true, null)
                            .string("password", SQL.buildType(String.class, JDBCType.VARCHAR, 512), true, null)
                            .bool("active", SQL.buildType(Boolean.class, JDBCType.BIT, 0, 0), true, null)
                            .build();
        NumberColumn<Long> id = (NumberColumn<Long>)users.getColumn("id");
        StringColumn<String> email = (StringColumn<String>)users.getColumn("email");
        StringColumn<String> password = (StringColumn<String>)users.getColumn("password");
        BooleanColumn<Boolean> active = (BooleanColumn<Boolean>)users.getColumn("active");
        MySQLDialect mysql = new MySQLDialect();
        SQLStatement stmtCreate = SQL.createTable(users)
                                    .column(id)
                                    .column(email)
                                    .column(password)
                                    .column(active)
                                    .primaryKey(id)
                                    .toSQL(mysql);
        System.out.println(stmtCreate);
        SQLStatement stmtALter = SQL.alterTable(users)
                                    .changeColumn(email, "email")
                                    .changeColumn(password, "password")
                                    .changeColumn(active, "active")
                                    .toSQL(mysql);
        System.out.println(stmtALter);
    }
}
