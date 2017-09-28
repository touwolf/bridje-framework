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

public class User
{
    public static final Table TABLE;

    public static final NumberColumn<Long> ID;

    public static final StringColumn<String> EMAIL;

    public static final StringColumn<String> PASSWORD;

    public static final BooleanColumn<Boolean> ACTIVE;

    static {
        TABLE = SQL.buildTable("users")
                    .number("id", SQL.buildType(Long.class, JDBCType.BIGINT), false, true, null)
                    .string("email", SQL.buildType(String.class, JDBCType.VARCHAR, 150), true, null)
                    .string("password", SQL.buildType(String.class, JDBCType.VARCHAR, 512), true, null)
                    .bool("active", SQL.buildType(Boolean.class, JDBCType.BIT, 0, 0), true, null)
                    .build();
        ID = TABLE.getNumberColumn("id", Long.class);
        EMAIL = TABLE.getStringColumn("email", String.class);
        PASSWORD = TABLE.getStringColumn("password", String.class);
        ACTIVE = TABLE.getBooleanColumn("active", Boolean.class);
    }
}
