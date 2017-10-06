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

    public static final NumberColumn<Long, Long> ID;

    public static final StringColumn<String, String> EMAIL;

    public static final StringColumn<String, String> PASSWORD;

    public static final BooleanColumn<Boolean, Boolean> ACTIVE;
    
    static {
        ID = SQL.buildAiColumn("id", SQLTypes.LONGID, false);
        EMAIL = SQL.buildStringColumn("email", SQLTypes.STRING150, true, null);
        PASSWORD = SQL.buildStringColumn("password", SQLTypes.PASSWORD, true, null);
        ACTIVE = SQL.buildBoolColumn("active", SQLTypes.BOOLEAN, true, null);

        TABLE = SQL.buildTable("users")
                        .key(ID)
                        .column(EMAIL)
                        .column(PASSWORD)
                        .column(ACTIVE)
                        .index(SQL.buildUnique(EMAIL))
                        .index(SQL.buildIndex(PASSWORD))
                        .index(SQL.buildIndex(ACTIVE))
                        .index(SQL.buildIndex(EMAIL, PASSWORD, ACTIVE))
                        .build();
    }   
}
