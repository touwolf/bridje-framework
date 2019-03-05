package org.bridje.el;

import de.odysseus.el.misc.TypeConverter;
import org.bridje.ioc.Ioc;
import org.junit.Assert;
import org.junit.Test;

public class TypeConverterTest
{
    @Test
    public void testConvert()
    {
        TypeConverter converter = Ioc.context().find(TypeConverter.class);
        Assert.assertNotNull(converter);

        Double doubleValue = 2.0123456789;
        String doubleStr = String.valueOf(doubleValue);
        Double converted = converter.convert(doubleStr, Double.class);
        Assert.assertNotNull(converted);
        Assert.assertEquals(doubleValue, converted);

        doubleStr = String.valueOf(doubleValue).replaceAll("\\.", ",");
        converted = converter.convert(doubleStr, Double.class);
        Assert.assertNotNull(converted);
        Assert.assertEquals(doubleValue, converted);

        doubleValue = 2D;
        doubleStr = String.valueOf(doubleValue);
        converted = converter.convert(doubleStr, Double.class);
        Assert.assertNotNull(converted);
        Assert.assertEquals(doubleValue, converted);

        doubleValue = 2_000.0123456789;
        doubleStr = String.valueOf(doubleValue);
        converted = converter.convert(doubleStr, Double.class);
        Assert.assertNotNull(converted);
        Assert.assertEquals(doubleValue, converted);

        doubleStr = String.valueOf(doubleValue).replaceAll("\\.", ",");
        converted = converter.convert(doubleStr, Double.class);
        Assert.assertNotNull(converted);
        Assert.assertEquals(doubleValue, converted);

        doubleStr = "2,000,0123456789";
        converted = converter.convert(doubleStr, Double.class);
        Assert.assertNotNull(converted);
        Assert.assertEquals(doubleValue, converted);
    }
}
