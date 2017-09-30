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

public class User
{
    public static final Table TABLE;

    public static final NumberColumn<Long> ID;

    public static final StringColumn<String> EMAIL;

    public static final StringColumn<String> PASSWORD;

    public static final BooleanColumn<Boolean> ACTIVE;

    public static final Index[] INDEXES;

    static {
        TABLE = SQL.buildTable("users")
                    .autoIncrement("id", SQLTypes.LONGID, true, false)
                    .string("email", SQLTypes.STRING150, false, true, null)
                    .string("password", SQLTypes.PASSWORD, false, true, null)
                    .bool("active", SQLTypes.BOOLEAN, false, true, null)
                    .build();

        ID = TABLE.getAsNumber("id", Long.class);
        EMAIL = TABLE.getAsString("email", String.class);
        PASSWORD = TABLE.getAsString("password", String.class);
        ACTIVE = TABLE.getAsBoolean("active", Boolean.class);

        INDEXES = new Index[]
        {
            SQL.buildIndex(TABLE, EMAIL),
            SQL.buildIndex(TABLE, PASSWORD),
            SQL.buildIndex(TABLE, ACTIVE),
            SQL.buildIndex(TABLE, EMAIL, PASSWORD, ACTIVE)
        };
    }
}
