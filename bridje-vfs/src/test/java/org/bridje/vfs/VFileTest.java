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

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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

    @Test
    public void test3MountMerged() throws IOException
    {
        VFile pathToMount = new VFile("/merged");
        pathToMount.mount(new FileSource(new File("./target/test-classes/merged/folder1")));
        Assert.assertTrue(pathToMount.exists());
        Assert.assertTrue(pathToMount.isDirectory());
        String[] list = pathToMount.list();
        Assert.assertNotNull(list);
        Assert.assertEquals(2, list.length);
        List<String> files = Arrays.asList(list);
        Assert.assertTrue(files.contains("common.txt"));
        Assert.assertTrue(files.contains("document1.txt"));
        VFile[] search = pathToMount.search(new GlobExpr("/common.txt"));
        Assert.assertNotNull(search);
        Assert.assertEquals(1, search.length);
        Assert.assertEquals("folder1", readFirstLine(search[0]));
        //now merge...
        pathToMount.mount(new FileSource(new File("./target/test-classes/merged/folder2")));
        Assert.assertTrue(pathToMount.exists());
        Assert.assertTrue(pathToMount.isDirectory());
        list = pathToMount.list();
        Assert.assertNotNull(list);
        Assert.assertEquals(3, list.length);
        files = Arrays.asList(list);
        Assert.assertTrue(files.contains("common.txt"));
        Assert.assertTrue(files.contains("document1.txt"));
        Assert.assertTrue(files.contains("document2.txt"));
        search = pathToMount.search(new GlobExpr("/common.txt"));
        Assert.assertNotNull(search);
        Assert.assertEquals(1, search.length);
        Assert.assertEquals("folder2", readFirstLine(search[0]));
    }

    @Test
    public void test4MountMerged() throws IOException
    {
        new VFile("/merged1").mount(new FileSource(new File("./target/test-classes/merged1")));
        new VFile("/merged1/vfs/other/folder1").mount(new FileSource(new File("./target/test-classes/merged/folder1")));
        new VFile("/merged1/vfs/other/folder2").mount(new FileSource(new File("./target/test-classes/merged/folder2")));

        Assert.assertTrue(Arrays.asList(new VFile("/merged1").list()).contains("vfs"));
        Assert.assertTrue(Arrays.asList(new VFile("/merged1").list()).contains("merged.txt"));

        Assert.assertArrayEquals(new String[]{"other"}, new VFile("/merged1/vfs").list());

        Assert.assertTrue(Arrays.asList(new VFile("/merged1/vfs/other").list()).contains("other-common.txt"));
        Assert.assertTrue(Arrays.asList(new VFile("/merged1/vfs/other").list()).contains("folder1"));
        Assert.assertTrue(Arrays.asList(new VFile("/merged1/vfs/other").list()).contains("folder2"));

        Assert.assertTrue(Arrays.asList(new VFile("/merged1/vfs/other/folder1").list()).contains("document1.txt"));
        Assert.assertTrue(Arrays.asList(new VFile("/merged1/vfs/other/folder1").list()).contains("common.txt"));
    }

    @Test
    public void test5MountMerged() throws IOException
    {
        new VFile("/merged2").mount(new FileSource(new File("./target/test-classes/merged1")));
        new VFile("/merged2/somefolder/other/folder1").mount(new FileSource(new File("./target/test-classes/merged/folder1")));
        new VFile("/merged2/somefolder/other/folder2").mount(new FileSource(new File("./target/test-classes/merged/folder2")));
        new VFile("/merged2/somefolder/other/merged1").mount(new FileSource(new File("./target/test-classes/merged1")));

        String[] list = Arrays.stream(new VFile("/merged2").list())
            .sorted(Comparator.naturalOrder())
            .toArray(String[]::new);
        Assert.assertArrayEquals(new String[] { "merged.txt", "somefolder", "vfs" }, list);

        Assert.assertArrayEquals(new String[] { "other" }, new VFile("/merged2/vfs").list());

        list = Arrays.stream(new VFile("/merged2/somefolder/other").list())
            .sorted(Comparator.naturalOrder())
            .toArray(String[]::new);
        Assert.assertArrayEquals(new String[] { "folder1", "folder2", "merged1" }, list);

        list = Arrays.stream(new VFile("/merged2/somefolder/other/folder1").list())
            .sorted(Comparator.naturalOrder())
            .toArray(String[]::new);
        Assert.assertArrayEquals(new String[] { "common.txt", "document1.txt" }, list);

        Assert.assertArrayEquals(new String[] { "other-common.txt" }, new VFile("/merged2/somefolder/other/merged1/vfs/other").list());
    }

    private String readFirstLine(VFile file) throws IOException
    {
        try (InputStream is = file.openForRead())
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            return reader.readLine();
        }
    }
}
