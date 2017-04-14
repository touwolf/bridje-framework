package org.bridje.web.srcgen.adapter;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link CData}
 */
public class CDataTest
{
    /**
     * Test of {@link CData#marshal(String)} && {@link CData#unmarshal(String)}
     */
    @Test
    public void testMarshallUnmarshall() throws JAXBException, XMLStreamException
    {
        String content = "<h1>Testing</h1>";

        ObjectCData obj = new ObjectCData();
        obj.setContent(content);

        JAXBContext ctx = JAXBContext.newInstance(ObjectCData.class);
        Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        Assert.assertTrue(writer.toString().contains("CDATA"));

        StringReader reader = new StringReader(writer.toString());
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        obj = (ObjectCData) unmarshaller.unmarshal(reader);
        Assert.assertEquals(content, obj.getContent());
    }
}
