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

package org.bridje.el.impl.convert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.bridje.el.ElSimleConvertProvider;
import org.bridje.el.ElSimpleConvertMap;
import org.bridje.ioc.Component;
import org.bridje.ioc.Priority;

@Component
@Priority(5030)
public class DateConverter implements ElSimleConvertProvider
{
    @Override
    public ElSimpleConvertMap createConvertMap()
    {
        ElSimpleConvertMap map = new ElSimpleConvertMap();
        
        map.add(LocalDate.class, Date.class, this::ldToDate);
        map.add(Date.class, LocalDate.class, this::dateToLd);
        map.add(LocalDateTime.class, Date.class, this::ldtToDate);
        map.add(Date.class, LocalDateTime.class, this::dateToLdt);
        
        return map;
    }

    public Date ldToDate(LocalDate data)
    {
        return Date.from(data.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public LocalDate dateToLd(Date data)
    {
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public Date ldtToDate(LocalDateTime data)
    {
        return Date.from(data.atZone(ZoneId.systemDefault()).toInstant());
    }

    public LocalDateTime dateToLdt(Date data)
    {
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
