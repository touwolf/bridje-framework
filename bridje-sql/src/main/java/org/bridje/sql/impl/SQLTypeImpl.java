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

package org.bridje.sql.impl;

import java.sql.JDBCType;
import org.bridje.sql.SQLType;

class SQLTypeImpl<T> implements SQLType<T>
{
    private final Class<T> javaType;

    private final JDBCType jdbcType;

    private final int length;

    private final int precision;

    SQLTypeImpl(Class<T> javaType, JDBCType jdbcType, int length, int precision)
    {
        this.javaType = javaType;
        this.jdbcType = jdbcType;
        this.length = length;
        this.precision = precision;
    }

    @Override
    public Class<T> getJavaType()
    {
        return javaType;
    }

    @Override
    public JDBCType getJDBCType()
    {
        return jdbcType;
    }

    @Override
    public int getLength()
    {
        return length;
    }

    @Override
    public int getPrecision()
    {
        return precision;
    }
}
