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
    public static final SQLType<Long> LONGID_TYPE;
    
    public static final SQLType<String> EMAIL_TYPE;
    
    public static final SQLType<String> PASSWORD_TYPE;
    
    public static final SQLType<Boolean> ACTIVE_TYPE;
    
    public static final Table TABLE;

    public static final NumberColumn<Long> ID;

    public static final StringColumn<String> EMAIL;

    public static final StringColumn<String> PASSWORD;

    public static final BooleanColumn<Boolean> ACTIVE;

    static {
        LONGID_TYPE = SQL.buildType(Long.class, JDBCType.BIGINT);
        EMAIL_TYPE = SQL.buildType(String.class, JDBCType.VARCHAR, 150);
        PASSWORD_TYPE = SQL.buildType(String.class, JDBCType.VARCHAR, 512);
        ACTIVE_TYPE = SQL.buildType(Boolean.class, JDBCType.BIT, 0, 0);

        TABLE = SQL.buildTable("users")
                    .autoIncrement("id", LONGID_TYPE, true, false)
                    .string("email", EMAIL_TYPE, false, true, null)
                    .string("password", PASSWORD_TYPE, false, true, null)
                    .bool("active", ACTIVE_TYPE, false, true, null)
                    .build();

        ID = TABLE.getAsNumber("id", Long.class);
        EMAIL = TABLE.getAsString("email", String.class);
        PASSWORD = TABLE.getAsString("password", String.class);
        ACTIVE = TABLE.getAsBoolean("active", Boolean.class);
    }
}
