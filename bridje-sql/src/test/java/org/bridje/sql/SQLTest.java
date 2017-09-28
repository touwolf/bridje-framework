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

import java.sql.SQLException;
import org.bridje.ioc.Ioc;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

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
    public void setUp()
    {
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
        SQLEnvironment sqlEnv = sqlServ.createEnvironment("TestDS");
        sqlEnv.fixTable(User.TABLE, Group.TABLE);
        SQLResultSet insRs = sqlEnv.execute(insertQuery);
        while (insRs.next())
        {
            Long id = insRs.get(User.ID);
            System.out.println(id);
        }
        SQLResultSet selRs = sqlEnv.execute(selectQuery);
        while (selRs.next())
        {
            Long id = selRs.get(User.ID);
            System.out.println(id);
        }
    }
    
}
