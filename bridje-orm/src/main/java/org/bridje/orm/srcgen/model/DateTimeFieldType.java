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

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * This enumerator represents the java types that can be use for a datetime
 * field.
 */
@XmlType
@XmlEnum
public enum DateTimeFieldType
{
    /**
     * java.util.Date
     */
    @XmlEnumValue("Date")
    DATE,
    /**
     * java.sql.Time
     */
    @XmlEnumValue("SQLTime")
    SQLTIME,
    /**
     * java.sql.Date
     */
    @XmlEnumValue("SQLDate")
    SQLDATE,
    /**
     * java.sql.Timestamp
     */
    @XmlEnumValue("Timestamp")
    TIMESTAMP,
    /**
     * java.time.LocalTime
     */
    @XmlEnumValue("LocalTime")
    LOCALTIME,
    /**
     * java.time.LocalDate
     */
    @XmlEnumValue("LocalDate")
    LOCALDATE,
    /**
     * java.time.LocalDateTime
     */
    @XmlEnumValue("LocalDateTime")
    LOCALDATETIME;

}
