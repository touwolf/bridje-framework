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
 * This class represents the types that the properties of the enumerators may
 * have.
 */
@XmlType
@XmlEnum
public enum EnumPropertyType
{
    /**
     * Integer
     */
    @XmlEnumValue("INTEGER")
    INTEGER,
    /**
     * String
     */
    @XmlEnumValue("STRING")
    STRING,
    /**
     * Double
     */
    @XmlEnumValue("DOUBLE")
    DOUBLE,
    /**
     * Long
     */
    @XmlEnumValue("LONG")
    LONG

}
