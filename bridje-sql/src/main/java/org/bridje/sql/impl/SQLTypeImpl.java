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
import org.bridje.sql.SQLValueParser;
import org.bridje.sql.SQLValueWriter;

class SQLTypeImpl<T, E> implements SQLType<T, E>
{
    private final Class<T> javaType;

    private final Class<E> javaReadType;

    private final JDBCType jdbcType;

    private final int length;

    private final int precision;

    private SQLValueParser<T, E> parser;

    private SQLValueWriter<E, T> writer;

    private Expression<T, E> param;

    SQLTypeImpl(Class<T> javaType, Class<E> javaReadType, JDBCType jdbcType, int length, int precision, SQLValueParser<T, E> parser, SQLValueWriter<E, T> writer)
    {
        this.javaType = javaType;
        this.jdbcType = jdbcType;
        this.length = length;
        this.precision = precision;
        this.javaReadType = javaReadType;
        this.writer = writer;
        this.parser = parser;
    }

    SQLTypeImpl(Class<T> javaType, JDBCType jdbcType, int length, int precision, SQLValueParser<T, E> parser, SQLValueWriter<E, T> writer)
    {
        this(javaType, (Class<E>)javaType, jdbcType, length, precision, parser, writer);
    }

    @Override
    public Class<T> getJavaType()
    {
        return javaType;
    }

    @Override
    public Class<E> getJavaReadType()
    {
        return javaReadType;
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
    public SQLValueParser<T, E> getParser()
    {
        return parser;
    }

    @Override
    public SQLValueWriter<E, T> getWriter()
    {
        return writer;
    }
    
    @Override
    public E read(Object value) throws SQLException
    {
        if(value == null) return null;
        return CastUtils.castValue(javaReadType, value);
    }

    @Override
    public T parse(E value) throws SQLException
    {
        if(value == null) return null;
        if(parser != null) return parser.parse(value);
        return (T)value;
    }

    @Override
    public E write(T value)
    {
        if(value == null) return null;
        if(writer != null) return writer.write(value);
        if(javaReadType.isAssignableFrom(value.getClass())) return (E)value;
        return null;
    }

    @Override
    public Expression<T, E> asParam()
    {
        if(param == null)
        {
            param = SQL.param(this);
        }
        return param;
    }
}
