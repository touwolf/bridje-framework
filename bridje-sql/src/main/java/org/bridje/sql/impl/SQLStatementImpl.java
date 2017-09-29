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

import org.bridje.sql.Expression;
import org.bridje.sql.SQLStatement;

class SQLStatementImpl implements SQLStatement
{
    private final Expression<?>[] resultFields;

    private final String sql;

    private final Object[] parameters;
    
    private final boolean generatedKeys;

    public SQLStatementImpl(Expression<?>[] resultFields, String sql, Object[] parameters, boolean generatedKeys)
    {
        this.resultFields = resultFields;
        this.sql = sql;
        this.parameters = parameters;
        this.generatedKeys = generatedKeys;
    }

    @Override
    public Expression<?>[] getResultFields()
    {
        return resultFields;
    }

    @Override
    public String getSQL()
    {
        return sql;
    }

    @Override
    public Object[] getParameters()
    {
        return parameters;
    }

    @Override
    public boolean isWithGeneratedKeys()
    {
        return generatedKeys;
    }
}
