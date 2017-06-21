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
import org.bridje.ioc.Ioc;

/**
 * This class represents a custom field, a field whos type is defined in a
 * separate jar, bridje will read the custom data type and generate an entity
 * field with the given class name and properties.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomFieldInf extends FieldInfBase
{
    @XmlAttribute
    private String type;

    @XmlAttribute
    private Boolean autoIncrement;

    /**
     * The name of the custom field type.
     *
     * @return The name of the custom field type.
     */
    public String getType()
    {
        return type;
    }

    /**
     * The name of the custom field type.
     *
     * @param type The name of the custom field type.
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * Determines when ever this field must be mark as an auto increment column
     * on the RDBMS.
     *
     * @return If the field is auto increment.
     */
    public boolean getAutoIncrement()
    {
        if (autoIncrement == null)
        {
            return false;
        }
        return autoIncrement;
    }

    /**
     * Sets when ever this field must be mark as an auto increment column
     * on the RDBMS.
     *
     * @param autoIncrement If the field is auto increment.
     */
    public void setAutoIncrement(boolean autoIncrement)
    {
        this.autoIncrement = autoIncrement;
    }

    @Override
    public String getJavaType()
    {
        CustomTypesProvider prov = Ioc.context().find(CustomTypesProvider.class);
        String result = prov.getJavaType(getType());
        if(result == null) return "InvalidType";
        return result;
    }

    @Override
    public String getTableColumn()
    {
        CustomTypesProvider prov = Ioc.context().find(CustomTypesProvider.class);
        String result = prov.getColumnClass(getType());
        if(result == null) return "InvalidColumn";
        return result;
    }

    @Override
    public FieldInfBase clone(EntityInfBase entity)
    {
        CustomFieldInf result = new CustomFieldInf();
        clone(result, entity);
        result.type = type;
        result.autoIncrement = autoIncrement;
        return result;
    }

}
