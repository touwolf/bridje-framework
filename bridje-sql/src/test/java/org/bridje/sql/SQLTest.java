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
import org.bridje.ioc.Ioc;
import org.bridje.vfs.CpSource;
import org.bridje.vfs.VFile;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SQLTest
{
    public SQLTest()
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
        VFile etc = new VFile("/etc");
        etc.mount(new CpSource("BRIDJE-INF/etc"));
    }

    @After
    public void tearDown()
    {
    }

    //@Test
    public void test1CreateTables() throws SQLException
    {
        SQLQuery selectQuery = SQL.select(User.ID)
                                    .from(User.TABLE)
                                    .where(User.ACTIVE.eq(true))
                                    .toQuery();

        SQLQuery insertQuery = SQL.insertInto(User.TABLE)
                                    .columns(User.EMAIL, User.PASSWORD, User.ACTIVE)
                                    .values("email@somedomain.com", "mypass", true)
                                    .toQuery();

        SQLService sqlServ = Ioc.context().find(SQLService.class);
        SQLEnvironment sqlEnv = sqlServ.createEnvironment("TestDB");
        sqlEnv.fixTable(User.TABLE, Group.TABLE);

        Long id = sqlEnv.fetchOne(insertQuery, (rs) -> rs.get(User.ID));
        System.out.println(id);

        id = sqlEnv.fetchOne(selectQuery, (rs) -> rs.get(User.ID));
        System.out.println(id);
    }
    
}
