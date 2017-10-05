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

import org.bridje.sql.NumberColumn;
import org.bridje.sql.Query;
import org.bridje.sql.SQL;
import org.bridje.sql.SQLType;
import org.bridje.sql.StringColumn;
import org.bridje.sql.Table;

public class Group
{
    public static final SQLType TYPE;

    public static final Table TABLE;

    public static final NumberColumn<Long, Long> ID;

    public static final StringColumn<String, String> TITLE;

    public static final Query SELECT;
    
    public static final Query INSERT;

    public static final Query UPDATE;

    public static final Query DELETE;

    static {
        TYPE = SQL.buildType(Group.class, SQLTypes.LONGID.getJavaReadType(), SQLTypes.LONGID.getJDBCType(), null, (e) -> e.getId());

        ID = SQL.buildAiColumn("id", SQLTypes.LONGID, true, false);
        TITLE = SQL.buildStringColumn("title", SQLTypes.STRING150, false, true, null);

        TABLE = SQL.buildTable("groups")
                        .key(ID)
                        .column(TITLE)
                        .index(SQL.buildIndex(TITLE))
                        .build();

        SELECT = SQL.select(ID)
                    .from(TABLE)
                    .where(ID.eq(ID.asParam()))
                    .toQuery();

        INSERT = SQL.insertInto(TABLE)
                    .columns(TITLE)
                    .values(TITLE.asParam())
                    .toQuery();

        UPDATE = SQL.update(TABLE)
                    .set(TITLE, TITLE.asParam())
                    .where(ID.eq(ID.asParam()))
                    .toQuery();

        DELETE = SQL.delete()
                    .from(TABLE)
                    .where(ID.eq(ID.asParam()))
                    .toQuery();
    }
    
    private Long id;
    
    private String title;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}
