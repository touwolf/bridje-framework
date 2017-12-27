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

import org.bridje.sql.SQLBuilder;
import org.bridje.sql.Limit;

class LimitImpl implements Limit
{
    private final int offset;

    private final int count;

    public LimitImpl(int count)
    {
        this.offset = -1;
        this.count = count;
    }

    public LimitImpl(int offset, int count)
    {
        this.offset = offset;
        this.count = count;
    }

    @Override
    public int getOffset()
    {
        return offset;
    }

    @Override
    public int getCount()
    {
        return count;
    }

    @Override
    public void writeSQL(SQLBuilder builder)
    {
        if (offset < 0)
        {
            builder.appendLimit(count);
        }
        else
        {
            builder.appendLimit(offset, count);
        }
    }
}
