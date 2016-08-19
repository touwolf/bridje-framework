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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Ioc;
import static org.junit.Assert.*;
import org.junit.Test;

public class VfsServiceTest
{
    private static final Logger LOG = Logger.getLogger(VfsServiceTest.class.getName());
    
    @Test
    public void testFindFiles()
    {
        String path = "/src/main";
        VfsService instance = Ioc.context().find(VfsService.class);
        instance.mount(new Path("/src"), new FileSource(new File("./src")));
        String expResult = "main";
        VFolder result = instance.findFolder(path);
        assertNotNull(result);
        assertEquals(expResult, result.getName());
    }

    @Test
    public void testTravel()
    {
        VfsService instance = Ioc.context().find(VfsService.class);
        List<VFile> res = new ArrayList<>();
        instance.travel((VFile f) ->
        {
            LOG.log(Level.INFO, f.getPath().toString());
            assertTrue(f.getName().matches("^P.+\\.java$"));
            assertTrue(f.getParentPath().toString().matches(".+impl"));
            res.add(f);
        }, "**/impl/P*.java");
        assertFalse(res.isEmpty());
    }
    
    @Test
    public void testRead() throws IOException
    {
        File f = new File("./target/tmptests/someprop.properties");
        if(f.exists())
        {
           f.delete();
        }
        f.getParentFile().mkdirs();
        f.createNewFile();
        Properties prop = new Properties();
        prop.put("prop1", "val1");
        prop.store(new FileWriter(f), "A properties file");
        
        VfsService vfsServ = Ioc.context().find(VfsService.class);
        vfsServ.mountFile("/tests", f.getParentFile());
        Properties readedProp = vfsServ.readFile("tests/someprop.properties", Properties.class);
        assertNotNull(readedProp);
        assertEquals(prop.get("prop1"), readedProp.getProperty("prop1"));
    }

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
}
