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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Ioc;
import org.junit.Test;
import static org.junit.Assert.*;

public class VfsServiceTest
{
    private static final Logger LOG = Logger.getLogger(VfsServiceTest.class.getName());

    @Test
    public void testFindFiles()
    {
        String path = "/src/main";
        VfsService instance = Ioc.context().find(VfsService.class);
        instance.mount(new Path("/src"), new FileVfsSource(new File("./src")));
        String expResult = "main";
        VirtualFolder result = instance.findFolder(path);
        assertNotNull(result);
        assertEquals(expResult, result.getName());
    }

    @Test
    public void testTravel()
    {
        VfsService instance = Ioc.context().find(VfsService.class);
        instance.travel((VirtualFile f) ->
        {
            LOG.log(Level.INFO, f.getPath().toString());
            assertTrue(f.getName().matches("^P.+\\.java$"));
            assertTrue(f.getParentPath().toString().matches(".+impl"));
        }, ".+impl\\/P.+\\.java");
    }
}
