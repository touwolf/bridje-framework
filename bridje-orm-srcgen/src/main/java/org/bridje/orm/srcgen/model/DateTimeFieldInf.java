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

package org.bridje.orm.srcgen.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * This class represents a kind of fild that will hold date and time values.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DateTimeFieldInf extends FieldInfBase
{
    @XmlAttribute
    private DateTimeFieldType type;

    @XmlAttribute
    private DateTimeFieldSQLType sqlType;

    /**
     * The type for this field, it must be one of the date/times types.
     *
     * @return The date/time type of the field.
     */
    public DateTimeFieldType getType()
    {
        if (type == null)
        {
            return DateTimeFieldType.LOCALDATETIME;
        }
        return type;
    }

    /**
     * The type for this field, it must be one of the date/times types.
     *
     * @param type The date/time type of the field.
     */
    public void setType(DateTimeFieldType type)
    {
        this.type = type;
    }

    /**
     * Gets the SQL type (JDBCType) that must be use for this field.
     * 
     * @return The SQL type that must be use for this field.
     */
    public DateTimeFieldSQLType getSqlType()
    {
        if (sqlType == null)
        {
            switch (getType())
            {
                case DATE:
                    return DateTimeFieldSQLType.DATE;
                case LOCALDATE:
                    return DateTimeFieldSQLType.DATE;
                case LOCALTIME:
                    return DateTimeFieldSQLType.TIME;
                case LOCALDATETIME:
                    return DateTimeFieldSQLType.DATE;
                case SQLDATE:
                    return DateTimeFieldSQLType.DATE;
                case SQLTIME:
                    return DateTimeFieldSQLType.TIME;
                case TIMESTAMP:
                    return DateTimeFieldSQLType.TIMESTAMP;
            }
        }
        return sqlType;
    }

    /**
     * Gets the SQL type (JDBCType) that must be use for this field.
     * 
     * @param sqlType The SQL type that must be use for this field.
     */
    public void setSqlType(DateTimeFieldSQLType sqlType)
    {
        this.sqlType = sqlType;
    }

    @Override
    public String getJavaType()
    {
        switch (getType())
        {
            case DATE:
                return "Date";
            case LOCALDATE:
                return "java.time.LocalDate";
            case LOCALTIME:
                return "java.time.LocalTime";
            case LOCALDATETIME:
                return "java.time.LocalDateTime";
            case SQLDATE:
                return "java.sql.Date";
            case SQLTIME:
                return "java.sql.Time";
            case TIMESTAMP:
                return "java.sql.Timestamp";
        }
        return "LocalDateTime";
    }

    @Override
    public FieldInfBase clone(EntityInfBase entity)
    {
        DateTimeFieldInf result = new DateTimeFieldInf();
        clone(result, entity);
        result.type = type;
        result.sqlType = sqlType;
        return result;
    }

}
