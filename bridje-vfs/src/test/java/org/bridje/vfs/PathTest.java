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

/**
 * Tests for {@link Path}
 */
public class PathTest
{
    /**
     * Test of {@link Path#getFirstElement()}
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
     * Test of {@link Path#getName()}
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
     * Test of {@link Path#getParent()}
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
     * Test of {@link Path#getNext()}
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
     * Test of {@link Path#hasNext()}
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
     * Test of {@link Path#isSelf()}
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
     * Test of {@link Path#isParent()}
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
     * Test of {@link Path#isLast()}
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
     * Test of {@link Path#getCanonicalPath()}
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
     * Test of {@link Path#toString()]}
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
     * Test of {@link Path#toString(java.lang.String)}
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
     * Test of {@link Path#join(org.bridje.vfs.Path)}
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
     * Test of {@link Path#join(java.lang.String)}
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

    /**
     * Test of {@link Path#globMatches(java.lang.String)}
     */
    @Test
    public void testGlob_Matches()
    {
        Path folder = new Path("/usr/dev/projects/superProject/src/main/java/org/bridje");
        // ok
        assertTrue(new GlobExpr("/usr/dev/**").globMatches(folder));
        assertTrue(new GlobExpr("/usr/dev/**/src/**/bridje").globMatches(folder));
        assertTrue(new GlobExpr("/usr/**/main/java/**").globMatches(folder));
        assertTrue(new GlobExpr("/usr/**/main/{java,js,php}/**").globMatches(folder));
        assertTrue(new GlobExpr("/usr/**/main/{j?va,js,php}/**").globMatches(folder));
        assertTrue(new GlobExpr("/usr/**/main/{j*}/**").globMatches(folder));
        assertTrue(new GlobExpr("/usr/**/main/{*ava}/**").globMatches(folder));
        assertTrue(new GlobExpr("/usr/**/main/{?ava}/**").globMatches(folder));
        assertTrue(new GlobExpr("/usr/**/src/**/[maven,java]/**").globMatches(folder));
        assertTrue(new GlobExpr("/usr/**/src/**/[a-z]ava/**").globMatches(folder));
        // not ok
        assertFalse(new GlobExpr("/usr/**/main/js/**").globMatches(folder));
        assertFalse(new GlobExpr("/usr/**/main/{css,js,php}/**").globMatches(folder));
        assertFalse(new GlobExpr("/usr/dev/?").globMatches(folder));
        assertFalse(new GlobExpr("/usr/**/main/{j?ava,js,php}/**").globMatches(folder));
        assertFalse(new GlobExpr("/usr/**/main/java/*").globMatches(folder));
        assertFalse(new GlobExpr("/usr/**/main/{p*}/**").globMatches(folder));
        assertFalse(new GlobExpr("/usr/**/main/*/bridje").globMatches(folder));
        assertFalse(new GlobExpr("/usr/**/src/**/[?ava]/**").globMatches(folder));

        Path file = new Path("/usr/dev/projects/superProject/src/main/java/org/bridje/main.java");
        // ok
        assertTrue(new GlobExpr("/usr/dev/**/*.*").globMatches(file));
        assertTrue(new GlobExpr("/usr/dev/**/*.java").globMatches(file));
        assertTrue(new GlobExpr("/usr/dev/**/main.*").globMatches(file));
        assertTrue(new GlobExpr("/usr/dev/**/ma?n.*").globMatches(file));
        assertTrue(new GlobExpr("/usr/dev/**/[a-z]???.*").globMatches(file));
        assertTrue(new GlobExpr("/usr/dev/**/java/**/bridje/{main,Class,Interface}.java").globMatches(file));
        assertTrue(new GlobExpr("/usr/dev/**/java/**/{m*}.java").globMatches(file));
        assertTrue(new GlobExpr("/usr/dev/**/java/**/*.*").globMatches(file));
        assertTrue(new GlobExpr("/usr/dev/**/java/**/*").globMatches(file));
        // not ok
        assertFalse(new GlobExpr("/usr/dev/**/[0-9]???.*").globMatches(file));
        assertFalse(new GlobExpr("/usr/dev/**/[a-z]??.*").globMatches(file));
        assertFalse(new GlobExpr("/usr/dev/**/java/**/{l*}.java").globMatches(file));
        assertFalse(new GlobExpr("/usr/dev/**/php/**/*.*").globMatches(file));
        assertFalse(new GlobExpr("/usr/dev/**/java/**/*.").globMatches(file));
    }

    /**
     * Test of {@link Path#globRemaining(java.lang.String)}
     */
    @Test
    public void testGlob_Remaining()
    {
        Path folder = new Path("/usr/dev/projects/superProject/src/main/java/org/bridje");
        assertEquals("src/main/java/org/bridje", new GlobExpr("/**/superProject").globRemaining(folder).toString());
        assertEquals("src/main/java/org/bridje", new GlobExpr("/**/dev/projects/{s*}t").globRemaining(folder).toString());
        assertNotEquals("src/main/java/org/bridje", new GlobExpr("/**/projects/**").globRemaining(folder).toString());
    }
}
