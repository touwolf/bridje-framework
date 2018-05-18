/*
 * Copyright 2017 Bridje Framework.
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

package org.bridje.sql;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import org.bridje.dql.DQL;
import org.bridje.dql.DQLCollection;
import org.bridje.dql.DQLField;
import org.bridje.dql.DQLQuery;
import org.bridje.dql.dialects.MongoDBDialect;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DQLTest
{
    public DQLTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp() throws IOException, URISyntaxException
    {
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void test1DQL() throws SQLException
    {
        DQLCollection products = DQL.collection("products");
        DQLField name = DQL.field("name");
        DQLQuery query = products.delete(DQL.in(name, "gilbert", "others"));
        System.out.println(query.toStatement(new MongoDBDialect()));
    }
}
