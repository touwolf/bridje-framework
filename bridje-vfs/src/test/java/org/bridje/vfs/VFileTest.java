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
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VFileTest
{
    @Test
    public void test1MountFiles() throws IOException
    {
        VFile pathToMount = new VFile("/project/sources");
        pathToMount.mount(new FileSource(new File("./src")));
        pathToMount = new VFile("/project/target");
        pathToMount.mount(new FileSource(new File("./target")));
    }

    @Test
    public void test2ListFiles() throws IOException
    {
        VFile srcMainJava = new VFile("/project/sources/main/java");
        Assert.assertTrue(srcMainJava.exists());
        Assert.assertTrue(srcMainJava.isDirectory());
        Assert.assertEquals(1, srcMainJava.list().length);
    }
}
