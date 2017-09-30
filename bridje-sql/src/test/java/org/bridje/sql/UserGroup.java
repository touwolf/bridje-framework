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
import static org.bridje.sql.User.EMAIL;

public class UserGroup
{
    public static final SQLType<Long> LONGID_TYPE;

    public static final Table TABLE;

    public static final NumberColumn<Long> ID;

    public static final NumberColumn<Long> GROUP_ID;

    public static final NumberColumn<Long> USER_ID;

    public static final Index[] INDEXES;

    public static final ForeignKey[] FOREIGN_KEYS;

    static {
        LONGID_TYPE = SQL.buildType(Long.class, JDBCType.BIGINT);

        TABLE = SQL.buildTable("groups")
                    .autoIncrement("id", LONGID_TYPE, true, false)
                    .number("groupId", LONGID_TYPE, false, true, null)
                    .number("userId", LONGID_TYPE, false, true, null)
                    .build();

        ID = TABLE.getAsNumber("id", Long.class);
        GROUP_ID = TABLE.getAsNumber("groupId", Long.class);
        USER_ID = TABLE.getAsNumber("userId", Long.class);

        INDEXES = new Index[]{
            SQL.buildUnique(TABLE, EMAIL),
        };

        FOREIGN_KEYS = new ForeignKey[]{
            SQL.buildForeignKey(TABLE, USER_ID)
                        .references(User.TABLE, ID)
                        .strategy(ForeignKeyStrategy.CASCADE)
                        .build(),
            SQL.buildForeignKey(TABLE, GROUP_ID)
                        .references(User.TABLE, ID)
                        .strategy(ForeignKeyStrategy.CASCADE)
                        .build()
        };
    }
}
