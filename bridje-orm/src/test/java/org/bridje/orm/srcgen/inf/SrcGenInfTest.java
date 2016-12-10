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

package org.bridje.orm.srcgen.inf;

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
        Assert.assertEquals(1, model.getEntities().size());
        
        EntityInf entity1 = model.getEntities().get(0);
        IntegerFieldInf intField1 = (IntegerFieldInf)entity1.getFields().get(0);
        Assert.assertEquals(IntegerFieldType.INTEGER, intField1.getType());
        Assert.assertEquals("integerField", intField1.getName());
        Assert.assertEquals(IntegerFieldSQLType.INTEGER, intField1.getSqlType());
        Assert.assertEquals(10, intField1.getLength().intValue());
        Assert.assertFalse(intField1.isRequired());
        Assert.assertTrue(intField1.isIndexed());
        Assert.assertFalse(intField1.isKey());
        Assert.assertEquals("Some int field", intField1.getDescription());
        
        StringFieldInf stringField1 = (StringFieldInf)entity1.getFields().get(1);
        Assert.assertEquals("someString", stringField1.getName());
        Assert.assertEquals(StringFieldSQLType.VARCHAR, stringField1.getSqlType());
        Assert.assertEquals(100, stringField1.getLength().intValue());
        Assert.assertTrue(stringField1.isRequired());
        Assert.assertTrue(stringField1.isIndexed());
        Assert.assertFalse(stringField1.isKey());
        Assert.assertEquals("Some string field", stringField1.getDescription());
        
        DecimalFieldInf doubleField1 = (DecimalFieldInf)entity1.getFields().get(2);
        Assert.assertEquals(DecimalFieldType.DOUBLE, doubleField1.getType());
        Assert.assertEquals("doubleField", doubleField1.getName());
        Assert.assertEquals(DecimalFieldSQLType.DOUBLE, doubleField1.getSqlType());
        Assert.assertEquals(10, doubleField1.getLength().intValue());
        Assert.assertEquals(5, doubleField1.getPrecision().intValue());
        Assert.assertFalse(doubleField1.isRequired());
        Assert.assertTrue(doubleField1.isIndexed());
        Assert.assertFalse(doubleField1.isKey());
        Assert.assertEquals("Some double field", doubleField1.getDescription());
        
        DateTimeFieldInf dateTimeField1 = (DateTimeFieldInf)entity1.getFields().get(3);
        Assert.assertEquals(DateTimeFieldType.LOCALDATETIME, dateTimeField1.getType());
        Assert.assertEquals("datetimeField", dateTimeField1.getName());
        Assert.assertEquals(DateTimeFieldSQLType.DATETIME, dateTimeField1.getSqlType());
        Assert.assertFalse(dateTimeField1.isRequired());
        Assert.assertTrue(dateTimeField1.isIndexed());
        Assert.assertFalse(dateTimeField1.isKey());
        Assert.assertEquals("Some datetime field", dateTimeField1.getDescription());
        
    }
}
