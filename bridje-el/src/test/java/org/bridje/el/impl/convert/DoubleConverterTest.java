package org.bridje.el.impl.convert;

import org.junit.Assert;
import org.junit.Test;

public class DoubleConverterTest {
    @Test
    public void testStringToDouble() {
        Double expected = 1820.00;
        String string = "$ :" + expected;
        Double value = DoubleConverter.toDouble(string);
        Assert.assertNotNull(value);
        Assert.assertEquals(expected, value);

        string = "$ " + expected;
        value = DoubleConverter.toDouble(string);
        Assert.assertNotNull(value);
        Assert.assertEquals(expected, value);

        expected *= -1;
        string = "$ :" + expected;
        value = DoubleConverter.toDouble(string);
        Assert.assertNotNull(value);
        Assert.assertEquals(expected, value);
    }
}
