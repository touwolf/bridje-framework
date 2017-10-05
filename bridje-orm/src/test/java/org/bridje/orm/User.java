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

import org.bridje.sql.BooleanColumn;
import org.bridje.sql.NumberColumn;
import org.bridje.sql.Query;
import org.bridje.sql.SQL;
import org.bridje.sql.SQLType;
import org.bridje.sql.StringColumn;
import org.bridje.sql.Table;

public class User
{
    public static final SQLType TYPE;
    
    public static final Table TABLE;

    public static final NumberColumn<Long> ID;

    public static final StringColumn<String> EMAIL;

    public static final StringColumn<String> PASSWORD;

    public static final BooleanColumn<Boolean> ACTIVE;
    
    public static final Query SELECT;
    
    public static final Query INSERT;

    public static final Query UPDATE;

    public static final Query DELETE;
    
    static {
        TYPE = SQL.buildType(User.class, SQLTypes.LONGID.getJDBCType());
        
        ID = SQL.buildAiColumn("id", SQLTypes.LONGID, true, false);
        EMAIL = SQL.buildStringColumn("email", SQLTypes.STRING150, false, true, null);
        PASSWORD = SQL.buildStringColumn("password", SQLTypes.PASSWORD, false, true, null);
        ACTIVE = SQL.buildBoolColumn("active", SQLTypes.BOOLEAN, false, true, null);

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
        SELECT = SQL.select(ID)
                    .from(TABLE)
                    .where(ID.eq(ID.asParam()))
                    .toQuery();

        INSERT = SQL.insertInto(TABLE)
                    .columns(EMAIL, PASSWORD, ACTIVE)
                    .values(EMAIL.asParam(), PASSWORD.asParam(), ACTIVE.asParam())
                    .toQuery();

        UPDATE = SQL.update(TABLE)
                    .set(EMAIL, EMAIL.asParam())
                    .set(PASSWORD, PASSWORD.asParam())
                    .set(ACTIVE, ACTIVE.asParam())
                    .where(ID.eq(ID.asParam()))
                    .toQuery();

        DELETE = SQL.delete()
                    .from(TABLE)
                    .where(ID.eq(ID.asParam()))
                    .toQuery();
    }
    
    private Long id;

    private String email;

    private String password;
    
    private Boolean active;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Boolean getActive()
    {
        return active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }
}
