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
import java.sql.SQLException;
import org.bridje.sql.Expression;
import org.bridje.sql.SQL;
import org.bridje.sql.SQLType;
import org.bridje.sql.SQLValueAdapter;

class SQLTypeImpl<T> implements SQLType<T>
{
    private final Class<T> javaType;

    private final JDBCType jdbcType;

    private final int length;

    private final int precision;

    private SQLValueAdapter<T, Object> adapter;

    private Expression<T> param;

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

    @Override
    public SQLValueAdapter<T, Object> getAdapter()
    {
        return adapter;
    }

    @Override
    public T parse(Object value) throws SQLException
    {
        if(value == null) return null;
        if(javaType.isAssignableFrom(value.getClass())) return (T)value;
        if(adapter != null) return adapter.parse(value);
        return (T)value;
    }

    @Override
    public Object write(T value) throws SQLException
    {
        if(value == null) return null;
        if(adapter != null) return adapter.write(value);
        return value;
    }

    @Override
    public Expression<T> asParam()
    {
        if(param == null)
        {
            param = SQL.param(this);
        }
        return param;
    }
}
