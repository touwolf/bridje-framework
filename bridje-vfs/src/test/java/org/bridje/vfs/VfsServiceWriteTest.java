/*
 * Copyright 2015 Bridje Framework.
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

package org.bridje.vfs;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import org.bridje.ioc.Ioc;
import static org.junit.Assert.*;
import org.junit.Test;

public class VfsServiceWriteTest
{
    private static final Logger LOG = Logger.getLogger(VfsServiceWriteTest.class.getName());

    @Test
    public void testWrite() throws IOException
    {
        File f = new File("./target/tmptests/someprop1.properties");
        if(f.exists())
        {
           f.delete();
        }
        f.getParentFile().mkdirs();
        Properties prop = new Properties();
        prop.put("prop1", "val1");

        VfsService vfsServ = Ioc.context().find(VfsService.class);
        vfsServ.mountFile("/tests", f.getParentFile());
        vfsServ.findFolder("/tests").createNewFile(new Path("someprop1.properties"));
        vfsServ.writeFile("tests/someprop1.properties", prop);
        Properties readedProp = vfsServ.readFile("tests/someprop1.properties", Properties.class);
        assertNotNull(readedProp);
        assertEquals(prop.get("prop1"), readedProp.getProperty("prop1"));
    }

    @Test
    public void testMountDefault() throws IOException
    {
        VfsService vfsServ = Ioc.context().find(VfsService.class);
        assertNotNull(vfsServ.findFile("/other/testfile"));
    }

    @Test
    public void testCreateAndWrite() throws IOException
    {
        VfsService vfsServ = Ioc.context().find(VfsService.class);
        vfsServ.mountFile("/etc", "./target");

        deleteDirectory(new File("./target/someTestFolder")); //delete the main dir
        assertFalse(vfsServ.canMkDir("/etc")); //etc is main mounted folder.
        assertTrue(vfsServ.canMkDir("/etc/someTestFolder")); // someTestFolder does not exists so it can be created
        assertTrue(vfsServ.canMkDir("/etc/someTestFolder/xmlwrtext")); // someTestFolder/xmlwrtext does not exists so it can be created
        SomeData someData = new SomeData();// sample data
        someData.setName("Data File");// sample data
        assertTrue(vfsServ.canCreateNewFile("/etc/someTestFolder/xmlwrtext/someData.xml")); //the file someTestFolder/xmlwrtext/someData.xml does not exists.
        assertNotNull(vfsServ.createAndWriteNewFile("/etc/someTestFolder/xmlwrtext/someData.xml", someData)); //create and serialize with the xml
        SomeData someData1 = vfsServ.readFile("/etc/someTestFolder/xmlwrtext/someData.xml", SomeData.class); //reed the serialized file.
        assertNotNull(someData1);//the data must not be null
        assertEquals(someData.getName(), someData1.getName());//must have the same value.

        //testing if the file can be create, someTestFolder exists but xmlwrtext1 does not.
        assertTrue(vfsServ.canCreateNewFile("/etc/someTestFolder/xmlwrtext1/someData2.xml"));

        deleteDirectory(new File("./target/someTestFolder1")); // deleting testing dir
        assertTrue(vfsServ.canCreateNewFile("/etc/someTestFolder1/xmlwrtext1/someData2.xml"));
        VFolder fold = vfsServ.mkDir("/etc/someTestFolder1");
        assertTrue(fold.canCreateNewFile("/xmlwrtext1/someData2.xml"));
        assertNotNull(fold.createAndWriteNewFile("/xmlwrtext1/someData2.xml", someData));
        SomeData someData2 = fold.readFile("/xmlwrtext1/someData2.xml", SomeData.class);
        assertNotNull(someData2);
        assertEquals(someData.getName(), someData2.getName());
        assertFalse(fold.canCreateNewFile("/xmlwrtext1/someData2.xml"));
    }

    static public boolean deleteDirectory(File path)
    {
        if (path.exists())
        {
            File[] files = path.listFiles();
            for (File file : files)
            {
                if (file.isDirectory())
                {
                    deleteDirectory(file);
                }
                else
                {
                    file.delete();
                }
            }
        }
        return (path.delete());
    }
}
