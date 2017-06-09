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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * This class represents the read operation for an Entity, The read operation
 * will be added to the ORM model class in the source code generation.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ReadOperationInf extends ParametizedOperationInf
{
    @XmlAttribute
    private ReadOperationResultType resultType;

    @XmlAttribute
    private String result;

    @XmlAttribute
    private String orderBys;

    @XmlTransient
    private List<OrderByInf> orderBysFields;

    /**
     * The result type for the read operation.
     * 
     * @return The result type for the read operation.
     */
    public ReadOperationResultType getResultType()
    {
        return resultType;
    }

    /**
     * The result type for the read operation.
     * 
     * @param resultType The result type for the read operation.
     */
    public void setResultType(ReadOperationResultType resultType)
    {
        this.resultType = resultType;
    }

    /**
     * The java type for the result of this operation.
     * 
     * @return The java type for the result of this operation.
     */
    public String getResult()
    {
        return result;
    }

    /**
     * The java type for the result of this operation.
     * 
     * @param result The java type for the result of this operation.
     */
    public void setResult(String result)
    {
        this.result = result;
    }

    /**
     * The field that will be returned by this read operation.
     * 
     * @return The field that will be returned by this read operation..
     */
    public FieldInfBase getResultField()
    {
        return getEntity().getFields()
                            .stream()
                            .filter(f -> f.getName().equalsIgnoreCase(result))
                            .findAny().orElse(null);
    }

    @Override
    public OperationType getOperationType()
    {
        return OperationType.READ;
    }

    @Override
    public OperationInfBase clone(EntityInfBase entity)
    {
        ReadOperationInf res = new ReadOperationInf();
        clone(res, entity);
        res.orderBys = this.orderBys;
        res.orderBysFields = this.orderBysFields;
        res.resultType = this.resultType;
        res.result = this.result;
        return res;
    }

    /**
     * The list of order by fields that this operation have.
     * 
     * @return A list of field information objects that represents the order by statement.
     */
    public List<OrderByInf> getOrderBys()
    {
        if(orderBysFields == null)
        {
            orderBysFields = parseOrderBys(orderBys);
        }
        return orderBysFields;
    }

    /**
     * Utility method to parse all the params, out of the params attribute.
     * 
     * @param fields The comma separated fields String.
     * @return The list of fields for the params.
     */
    protected List<OrderByInf> parseOrderBys(String fields)
    {
        List<OrderByInf> result = new ArrayList<>();
        if(fields == null || fields.isEmpty()) return result;
        String[] split = fields.split(",");
        for (String orderByField : split)
        {
            String[] orderByArr = orderByField.split(" ");
            String fieldName = orderByArr[0];
            String type = "asc";
            if(orderByArr.length > 1) type = orderByArr[1];
            FieldInfBase field = getEntity().findField(fieldName);
            if(field == null) continue;
            result.add(new OrderByInf(field, type.equals("desc") ? OrderByType.DESC : OrderByType.ASC));
        }
        return result;
    }
}
