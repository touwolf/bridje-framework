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
import java.sql.SQLException;

public interface SQLType<T, E>
{
    public static final SQLType<Boolean, Boolean> BOOLEAN = SQL.buildType(Boolean.class, JDBCType.BOOLEAN);

    public static final SQLType<Long, Long> LONG = SQL.buildType(Long.class, JDBCType.BIGINT);

    public static final SQLType<Integer, Integer> INTEGER = SQL.buildType(Integer.class, JDBCType.INTEGER);

    public static final SQLType<Short, Short> SHORT = SQL.buildType(Short.class, JDBCType.SMALLINT);

    public static final SQLType<Character, Character> CHAR = SQL.buildType(Character.class, JDBCType.CHAR);

    public static final SQLType<String, String> STRING = SQL.buildType(String.class, JDBCType.VARCHAR, 255);
    
    Class<T> getJavaType();
    
    Class<E> getJavaReadType();

    JDBCType getJDBCType();

    int getLength();

    int getPrecision();

    Expression<T, E> asParam();

    SQLValueAdapter<T, Object> getAdapter();
    
    T parse(Object object) throws SQLException;

    Object write(T object) throws SQLException;
}
