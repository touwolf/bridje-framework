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

import static org.bridje.sql.User.EMAIL;

public class Group
{
    public static final Table TABLE;

    public static final NumberColumn<Long> ID;

    public static final StringColumn<String> TITLE;

    public static final Index[] INDEXES;

    static {
        TABLE = SQL.buildTable("groups")
                    .autoIncrement("id", SQLTypes.LONGID, true, false)
                    .string("title", SQLTypes.STRING150, false, true, null)
                    .build();

        ID = TABLE.getAsNumber("id", Long.class);
        TITLE = TABLE.getAsString("title", String.class);

        INDEXES = new Index[]{
            SQL.buildIndex(TABLE, EMAIL)
        };
    }
}
