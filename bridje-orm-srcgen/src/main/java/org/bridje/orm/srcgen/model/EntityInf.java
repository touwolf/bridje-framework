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

package org.bridje.orm.srcgen.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public class EntityInf
{
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String table;

    @XmlTransient
    private ModelInf model;

    @XmlAttribute(name = "description")
    private String description;

    private EntityInfKey key;

    @XmlElementWrapper(name = "fields")
    @XmlElements(
    {
        @XmlElement(name = "relation", type = RelationField.class),
        @XmlElement(name = "boolean", type = BooleanField.class),
        @XmlElement(name = "number", type = NumberField.class),
        @XmlElement(name = "string", type = StringField.class)
    })
    private List<FieldInf> fields;

    @XmlElementWrapper(name = "indexes")
    @XmlElements(
    {
        @XmlElement(name = "index", type = EntityIndexInf.class),
        @XmlElement(name = "unique", type = EntityUniqueIndexInf.class)
    })
    private List<EntityIndexInf> indexes;

    @XmlElementWrapper(name = "queries")
    @XmlElements(
    {
        @XmlElement(name = "query", type = QueryInf.class)
    })
    private List<QueryInf> queries;

    @XmlTransient
    private List<FieldInf> allFields;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTable()
    {
        if(table == null)
        {
            table = Utils.toSQLName(name);
        }
        return table;
    }

    public void setTable(String table)
    {
        this.table = table;
    }

    public String getFullName()
    {
        return model.getPackage() + "." + this.getName();
    }

    public ModelInf getModel()
    {
        return model;
    }

    public void setModel(ModelInf model)
    {
        this.model = model;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public FieldInf getKey()
    {
        return key.getField();
    }

    public void setKey(FieldInf key)
    {
        if(this.key == null) this.key = new EntityInfKey();
        this.key.setField(key);
    }

    public List<FieldInf> getFields()
    {
        return fields;
    }

    public void setFields(List<FieldInf> fields)
    {
        this.fields = fields;
    }

    public List<FieldInf> getAllFields()
    {
        if(allFields == null)
        {
            allFields = new ArrayList<>();
            allFields.add(key.getField());
            allFields.addAll(fields);
        }
        return allFields;
    }

    public List<FieldInf> getNonAiFields()
    {
        return getAllFields()
                    .stream()
                    .filter(f -> !f.isAutoIncrement())
                    .collect(Collectors.toList());
    }

    public List<QueryInf> getQueries()
    {
        return queries;
    }

    public void setQueries(List<QueryInf> queries)
    {
        this.queries = queries;
    }

    public List<EntityIndexInf> getIndexes()
    {
        return indexes;
    }

    public void setIndexes(List<EntityIndexInf> indexes)
    {
        this.indexes = indexes;
    }

    public List<RelationField> getForeignKeys()
    {
        return fields.stream()
                    .filter(f -> f instanceof RelationField)
                    .map(f -> (RelationField)f)
                    .collect(Collectors.toList());
    }

    void afterUnmarshal(Unmarshaller u, Object parent)
    {
        setModel((ModelInf)parent);
    }

    public FieldInf[] findFields(String[] names)
    {
        FieldInf[] fieldsArr = new FieldInf[names.length];
        int i = 0;
        for (String fieldName : names)
        {
            fieldsArr[i] = findField(fieldName);
            i++;
        }
        return fieldsArr;
    }

    public FieldInf findField(String fieldName)
    {
        return getAllFields().stream()
                .filter(f -> f.getName().equalsIgnoreCase(fieldName))
                .findFirst()
                .orElse(null);
    }
}
