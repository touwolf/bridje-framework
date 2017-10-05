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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.bridje.sql.Expression;
import org.bridje.sql.SQLResultSet;
import org.bridje.sql.SQLType;
import org.bridje.sql.SQLValueParser;

class SQLResultSetImpl implements SQLResultSet
{
    private final ResultSet rs;

    private final Map<Expression<?, ?>, Integer> fieldsMap;

    public SQLResultSetImpl(ResultSet rs, Expression<?, ?>[] fields)
    {
        this.rs = rs;
        this.fieldsMap = new HashMap<>();
        if(fields != null)
        {
            int index = 0;
            for (Expression<?, ?> field : fields)
            {
                fieldsMap.put(field, index);
                index++;
            }
        }
    }

    @Override
    public boolean next() throws SQLException
    {
        return rs.next();
    }

    @Override
    public <T, E> T get(Expression<T, E> expr) throws SQLException
    {
        Integer index = getIndex(expr);
        if(index != null)
        {
            if(expr.getSQLType() != null)
            {
                return expr.getSQLType().parse(rs.getObject(index+1));
            }
        }
        return null;
    }

    @Override
    public <T, E> T get(int column, SQLType<T, E> sqlType) throws SQLException
    {
        return sqlType.parse(rs.getObject(column));
    }

    @Override
    public void close() throws Exception
    {
        rs.close();
    }

    @Override
    public <T, E> T get(Expression<T, E> expr, SQLValueParser<T, E> parser) throws SQLException
    {
        Integer index = getIndex(expr);
        if(index != null)
        {
            return parser.parse((E)rs.getObject(index+1));
        }
        return null;
    }

    @Override
    public <T, E> T get(int column, SQLType<T, E> type, SQLValueParser<T, E> parser) throws SQLException
    {
        /*
        if(value == null) return null;
        Object val = CastUtils.castValue(type.getJavaType(), value);
        if(adapter != null) return adapter.parse(val);
        return (T)val;
        */
        return parser.parse((E)rs.getObject(column));
    }

    private <T, E> Integer getIndex(Expression<T, E> expr)
    {
        if(fieldsMap == null) return null;
        return fieldsMap.get(expr);
    }
}
