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

public class UserGroup
{
    public static final Table TABLE;

    public static final NumberColumn<Long, Long> ID;

    public static final NumberColumn<Long, Long> GROUP_ID;

    public static final NumberColumn<Long, Long> USER_ID;

    static {
        ID = SQL.buildAiColumn("id", SQLTypes.LONGID, true, false);
        GROUP_ID = SQL.buildNumberColumn("groupId", SQLTypes.LONGID, false, true, null);
        USER_ID = SQL.buildNumberColumn("userId", SQLTypes.LONGID, false, true, null);

        TABLE = SQL.buildTable("user_groups")
                        .key(ID)
                        .column(GROUP_ID)
                        .column(USER_ID)
                    .foreignKey(SQL.buildForeignKey(USER_ID)
                        .references(User.TABLE)
                        .strategy(ForeignKeyStrategy.CASCADE)
                        .build())
                    .foreignKey(SQL.buildForeignKey(GROUP_ID)
                        .references(Group.TABLE)
                        .strategy(ForeignKeyStrategy.CASCADE)
                        .build())
                    .build();
    }
}
