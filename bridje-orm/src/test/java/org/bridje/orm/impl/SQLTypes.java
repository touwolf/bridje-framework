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

package org.bridje.orm.impl;

import java.sql.JDBCType;
import org.bridje.sql.SQL;
import org.bridje.sql.SQLType;

public class SQLTypes
{
    public static final SQLType<Long> LONGID;

    public static final SQLType<String> STRING150;

    public static final SQLType<String> PASSWORD;

    public static final SQLType<Boolean> BOOLEAN;

    static {
        LONGID = SQL.buildType(Long.class, JDBCType.BIGINT);
        STRING150 = SQL.buildType(String.class, JDBCType.VARCHAR, 150);
        PASSWORD = SQL.buildType(String.class, JDBCType.VARCHAR, 512);
        BOOLEAN = SQL.buildType(Boolean.class, JDBCType.BIT, 0, 0);
    }
}
