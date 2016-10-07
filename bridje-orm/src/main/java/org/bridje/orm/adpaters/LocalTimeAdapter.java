/*
 * Copyright 2016 Bridje Framework.
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

package org.bridje.orm.adpaters;

import java.sql.Time;
import java.time.LocalTime;
import org.bridje.ioc.Component;
import org.bridje.orm.Column;
import org.bridje.orm.SQLAdapter;

/**
 * An SQLAdapter for LocalTime class.
 */
@Component
public class LocalTimeAdapter implements SQLAdapter
{
    @Override
    public Object serialize(Object value, Column column)
    {
        if(value instanceof LocalTime)
        {
            LocalTime lt = ((LocalTime)value);
            return Time.valueOf(lt);
        }
        return null;
    }

    @Override
    public Object unserialize(Object value, Column column)
    {
        if(value instanceof Time)
        {
            Time d = ((Time)value);
            return d.toLocalTime();
        }
        return null;
    }
}
