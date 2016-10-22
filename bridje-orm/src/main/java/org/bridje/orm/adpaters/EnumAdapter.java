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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Component;
import org.bridje.orm.Column;
import org.bridje.orm.SQLAdapter;
import org.bridje.orm.TableColumn;

/**
 * An SQLAdapter for enum classes. This adapter will serialize any anum object
 * taking the ordinal or the name depending on the result JDBCType.
 */
@Component
public class EnumAdapter implements SQLAdapter
{
    private static final Logger LOG = Logger.getLogger(EnumAdapter.class.getName());

    @Override
    public Object serialize(Object value, Column column)
    {
        if (value instanceof Enum)
        {
            if (column instanceof TableColumn)
            {
                switch (((TableColumn) column).getSqlType())
                {
                    case BIGINT:
                    case INTEGER:
                    case SMALLINT:
                    case TINYINT:
                        return serializeOrdinal((Enum) value);
                    case VARCHAR:
                    case CHAR:
                    case NVARCHAR:
                    case NCHAR:
                        return serializeName((Enum) value);
                }
            }
        }
        return null;
    }

    @Override
    public Object unserialize(Object value, Column column)
    {
        if (isEnum(column.getType()))
        {
            if (value instanceof Number)
            {
                return unserializeOrdinal((Number) value, (Class<? extends Enum>) column.getType());
            }
            if (value instanceof String)
            {
                return unserializeString((String) value, (Class<? extends Enum>) column.getType());
            }
        }
        return null;
    }

    private int serializeOrdinal(Enum aEnum)
    {
        return aEnum.ordinal();
    }

    private String serializeName(Enum aEnum)
    {
        return aEnum.name();
    }

    private Object unserializeOrdinal(Number number, Class<? extends Enum> cls)
    {
        Enum[] vals = cls.getEnumConstants();
        int index = number.intValue();
        if (index < 0 || index >= vals.length)
        {
            return null;
        }
        return vals[index];
    }

    private Object unserializeString(String string, Class<? extends Enum> cls)
    {
        return Enum.valueOf(cls, string);
    }

    private boolean isEnum(Class type)
    {
        return Enum.class.isAssignableFrom(type);
    }
}
