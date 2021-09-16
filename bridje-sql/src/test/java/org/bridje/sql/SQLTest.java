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
import org.junit.*;

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

    @Test
    public void test1CreateTables() throws SQLException
    {
        SQLService sqlServ = Ioc.context().find(SQLService.class);
        SQLEnvironment sqlEnv = sqlServ.createEnvironment("H2-DB");
        sqlEnv.fixSchema(TestDB.SCHEMA);

        sqlEnv.update(SQL.delete().from(User.TABLE).toQuery());
        sqlEnv.update(SQL.delete().from(Group.TABLE).toQuery());
        sqlEnv.update(SQL.delete().from(UserGroup.TABLE).toQuery());

        Query insertUsers = SQL.insertInto(User.TABLE)
                .columns(User.EMAIL, User.PASSWORD, User.ACTIVE)
                .values(User.EMAIL.asParam(), User.PASSWORD.asParam(), User.ACTIVE.asParam())
                .toQuery();
        sqlEnv.update(insertUsers, "email1@domain.com", "pass1", true);
        sqlEnv.update(insertUsers, "email2@domain.com", "pass2", true);
        sqlEnv.update(insertUsers, "email3@domain.com", "pass3", true);
        sqlEnv.update(insertUsers, "email4@domain.com", "pass4", false);
        sqlEnv.update(insertUsers, "email5@domain.com", "pass5", false);

        Query countUsers = SQL.select(SQL.count())
                                    .from(User.TABLE)
                                    .toQuery();

        Integer ctn = sqlEnv.fetchOne(countUsers, (rs) -> rs.get(SQL.count()));
        Assert.assertNotNull(ctn);
        Assert.assertEquals(5l, ctn.longValue());

        Query insertGroups = SQL.insertInto(Group.TABLE)
                .columns(Group.TITLE)
                .values(Group.TITLE.asParam())
                .toQuery();
        sqlEnv.update(insertGroups, "Group 1");
        sqlEnv.update(insertGroups, "Group 2");
        sqlEnv.update(insertGroups, "Group 3");

        Query countGroups = SQL.select(SQL.count())
                                    .from(Group.TABLE)
                                    .toQuery();

        ctn = sqlEnv.fetchOne(countGroups, (rs) -> rs.get(SQL.count()));
        Assert.assertNotNull(ctn);
        Assert.assertEquals(3l, ctn.longValue());
    }
}
