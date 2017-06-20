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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SrcGenInfTest
{
    @Test
    public void test1ReadModel() throws JAXBException
    {
        JAXBContext jaxbCtx = JAXBContext.newInstance(ModelInf.class);
        Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
        ModelInf model = (ModelInf)unmarshaller.unmarshal(getClass().getResourceAsStream("SomeModel.xml"));
        Assert.assertEquals("SomeModel", model.getName());
        Assert.assertEquals(1, model.getTemplates().size());
        Assert.assertEquals(2, model.getEntities().size());
        Assert.assertEquals(1, model.getEnums().size());

        EntityInf entity1 = model.getEntities().get(0);
        IntegerFieldInf intField1 = (IntegerFieldInf)entity1.getFields().get(1);
        Assert.assertEquals(IntegerFieldType.INTEGER, intField1.getType());
        Assert.assertEquals("integerField", intField1.getName());
        Assert.assertEquals(IntegerFieldSQLType.INTEGER, intField1.getSqlType());
        Assert.assertEquals(10, intField1.getLength().intValue());
        Assert.assertFalse(intField1.getRequired());
        Assert.assertTrue(intField1.getIndexed());
        Assert.assertFalse(intField1.getKey());
        Assert.assertEquals("Some int field", intField1.getDescription());

        StringFieldInf stringField1 = (StringFieldInf)entity1.getFields().get(2);
        Assert.assertEquals("someString", stringField1.getName());
        Assert.assertEquals(StringFieldSQLType.VARCHAR, stringField1.getSqlType());
        Assert.assertEquals(100, stringField1.getLength().intValue());
        Assert.assertTrue(stringField1.getRequired());
        Assert.assertTrue(stringField1.getIndexed());
        Assert.assertFalse(stringField1.getKey());
        Assert.assertEquals("Some string field", stringField1.getDescription());

        DecimalFieldInf doubleField1 = (DecimalFieldInf)entity1.getFields().get(3);
        Assert.assertEquals(DecimalFieldType.DOUBLE, doubleField1.getType());
        Assert.assertEquals("doubleField", doubleField1.getName());
        Assert.assertEquals(DecimalFieldSQLType.DOUBLE, doubleField1.getSqlType());
        Assert.assertEquals(10, doubleField1.getLength().intValue());
        Assert.assertEquals(5, doubleField1.getPrecision().intValue());
        Assert.assertFalse(doubleField1.getRequired());
        Assert.assertTrue(doubleField1.getIndexed());
        Assert.assertFalse(doubleField1.getKey());
        Assert.assertEquals("Some double field", doubleField1.getDescription());

        DateTimeFieldInf dateTimeField1 = (DateTimeFieldInf)entity1.getFields().get(4);
        Assert.assertEquals(DateTimeFieldType.LOCALDATETIME, dateTimeField1.getType());
        Assert.assertEquals("datetimeField", dateTimeField1.getName());
        Assert.assertEquals(DateTimeFieldSQLType.DATE, dateTimeField1.getSqlType());
        Assert.assertFalse(dateTimeField1.getRequired());
        Assert.assertTrue(dateTimeField1.getIndexed());
        Assert.assertFalse(dateTimeField1.getKey());
        Assert.assertEquals("Some datetime field", dateTimeField1.getDescription());

        RelationFieldInf relationField1 = (RelationFieldInf)entity1.getFields().get(5);
        Assert.assertEquals("relationField", relationField1.getName());
        Assert.assertEquals("SomeEntity1", relationField1.getType().getName());
        Assert.assertFalse(relationField1.getRequired());
        Assert.assertTrue(relationField1.getIndexed());
        Assert.assertFalse(relationField1.getKey());
        Assert.assertEquals("Some relation", relationField1.getDescription());

        EnumFieldInf enumField1 = (EnumFieldInf)entity1.getFields().get(6);
        Assert.assertEquals("someEnum", enumField1.getName());
        Assert.assertEquals("SomeEnum", enumField1.getType().getName());
        Assert.assertEquals(EnumFieldSQLType.TINYINT, enumField1.getSqlType());
        Assert.assertFalse(enumField1.getRequired());
        Assert.assertTrue(enumField1.getIndexed());
        Assert.assertFalse(enumField1.getKey());
        Assert.assertEquals("Some enums", enumField1.getDescription());

        CustomFieldInf customField1 = (CustomFieldInf)entity1.getFields().get(7);
        Assert.assertEquals("someCustom", customField1.getName());
        Assert.assertEquals("SomeType", customField1.getType());
        Assert.assertFalse(customField1.getAutoIncrement());
        Assert.assertFalse(customField1.getRequired());
        Assert.assertTrue(customField1.getIndexed());
        Assert.assertFalse(customField1.getKey());
        Assert.assertEquals("Some custom field", customField1.getDescription());

        EnumInf enum1 = (EnumInf)model.getEnums().get(0);
        Assert.assertEquals("SomeEnum", enum1.getName());
        Assert.assertEquals(2, enum1.getConstants().size());
        EnumConstantInf enumConst1 = enum1.getConstants().get(0);
        Assert.assertEquals("CONST1", enumConst1.getName());
        EnumConstantInf enumConst2 = enum1.getConstants().get(1);
        Assert.assertEquals("CONST2", enumConst2.getName());
    }
}
