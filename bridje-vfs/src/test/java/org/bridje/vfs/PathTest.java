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

import static org.junit.Assert.*;
import org.junit.Test;

public class PathTest
{
    /**
     * Test of getFirstElement method, of class Path.
     */
    @Test
    public void testGetFirstElement()
    {
        Path instance = new Path("/usr/local");
        String expResult = "usr";
        String result = instance.getFirstElement();
        assertEquals(expResult, result);
    }

    /**
     * Test of getName method, of class Path.
     */
    @Test
    public void testGetName()
    {
        Path instance = new Path("/usr/local/asd.txt");
        String expResult = "asd.txt";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getParent method, of class Path.
     */
    @Test
    public void testGetParent()
    {
        Path instance = new Path("/var/etc/log/");
        Path expResult = new Path("/var/etc");
        Path result = instance.getParent();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNext method, of class Path.
     */
    @Test
    public void testGetNext()
    {
        Path instance = new Path("/var/www/html");
        Path expResult = new Path("/www/html");
        Path result = instance.getNext();
        assertEquals(expResult, result);
    }

    /**
     * Test of hasNext method, of class Path.
     */
    @Test
    public void testHasNext()
    {
        Path instance = new Path("/www");
        boolean expResult = false;
        boolean result = instance.hasNext();
        assertEquals(expResult, result);
    }

    /**
     * Test of isSelf method, of class Path.
     */
    @Test
    public void testIsSelf()
    {
        Path instance = new Path("/./asd");
        boolean expResult = true;
        boolean result = instance.isSelf();
        assertEquals(expResult, result);
    }

    /**
     * Test of isParent method, of class Path.
     */
    @Test
    public void testIsParent()
    {
        Path instance = new Path("/../");
        boolean expResult = true;
        boolean result = instance.isParent();
        assertEquals(expResult, result);
    }

    /**
     * Test of isLast method, of class Path.
     */
    @Test
    public void testIsLast()
    {
        Path instance = new Path("/www");
        boolean expResult = true;
        boolean result = instance.isLast();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCanonicalPath method, of class Path.
     */
    @Test
    public void testGetCanonicalPath()
    {
        Path instance = new Path("/usr/./../usr/local/../g.txt");
        Path expResult = new Path("/usr/g.txt");
        Path result = instance.getCanonicalPath();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Path.
     */
    @Test
    public void testToString_0args()
    {
        Path instance = new Path("/usr/local");
        String expResult = "usr/local";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Path.
     */
    @Test
    public void testToStringString()
    {
        String pathSep = "\\";
        Path instance = new Path("/usr/local/bin");
        String expResult = "usr\\local\\bin";
        String result = instance.toString(pathSep);
        assertEquals(expResult, result);
    }

    /**
     * Test of join method, of class Path.
     */
    @Test
    public void testJoin_Path()
    {
        Path path = new Path("/html/public");
        Path instance = new Path("/var/www");
        Path expResult = new Path("/var/www/html/public");
        Path result = instance.join(path);
        assertEquals(expResult, result);
    }

    /**
     * Test of join method, of class Path.
     */
    @Test
    public void testJoin_String()
    {
        String path = "/html/public";
        Path instance = new Path("/var/www");
        Path expResult = new Path("/var/www/html/public");
        Path result = instance.join(path);
        assertEquals(expResult, result);
    }
}
