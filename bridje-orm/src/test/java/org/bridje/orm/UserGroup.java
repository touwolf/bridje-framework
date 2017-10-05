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

package org.bridje.orm;

import org.bridje.sql.Column;
import org.bridje.sql.ForeignKeyStrategy;
import org.bridje.sql.NumberColumn;
import org.bridje.sql.Query;
import org.bridje.sql.SQL;
import org.bridje.sql.Table;

public class UserGroup
{
    public static final Table TABLE;

    public static final NumberColumn<Long, Long> ID;

    public static final Column<Group, Long> GROUP;

    public static final Column<User, Long> USER;

    public static final Query SELECT;
    
    public static final Query INSERT;

    public static final Query UPDATE;

    public static final Query DELETE;

    static {
        ID = SQL.buildAiColumn("id", SQLTypes.LONGID, true, false);
        GROUP = SQL.buildNumberColumn("group", Group.TYPE, false, true, null);
        USER = SQL.buildNumberColumn("user", User.TYPE, false, true, null);

        TABLE = SQL.buildTable("user_groups")
                        .key(ID)
                        .column(GROUP)
                        .column(USER)
                    .foreignKey(SQL.buildForeignKey(USER)
                        .references(User.TABLE)
                        .strategy(ForeignKeyStrategy.CASCADE)
                        .build())
                    .foreignKey(SQL.buildForeignKey(GROUP)
                        .references(Group.TABLE)
                        .strategy(ForeignKeyStrategy.CASCADE)
                        .build())
                    .build();

        SELECT = SQL.select(ID)
                    .from(TABLE)
                    .where(ID.eq(ID.asParam()))
                    .toQuery();

        INSERT = SQL.insertInto(TABLE)
                    .columns(GROUP, USER)
                    .values(GROUP.asParam(), USER.asParam())
                    .toQuery();

        UPDATE = SQL.update(TABLE)
                    .set(GROUP, GROUP.asParam())
                    .set(USER, USER.asParam())
                    .where(ID.eq(ID.asParam()))
                    .toQuery();

        DELETE = SQL.delete()
                    .from(TABLE)
                    .where(ID.eq(ID.asParam()))
                    .toQuery();
    }

    private Long id;

    private Group group;

    private User user;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Group getGroup()
    {
        return group;
    }

    public void setGroup(Group group)
    {
        this.group = group;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }
}
