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

package org.bridje.orm;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import org.bridje.vfs.CpSource;
import org.bridje.vfs.VFile;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ORMTest
{
    public ORMTest()
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
    public void test1() throws SQLException
    {
        /*
        ORMService ormServ = Ioc.context().find(ORMService.class);
        ORMEnvironment ormEnv = ormServ
                                    .model(TestModel.class, "Derby-DB")
                                    .build();
        TestModel model = ormEnv.getModel(TestModel.class);
        model.findSchema();

        model.deleteAllUsers();

        User user = new User();
        user.setEmail("someemail@hotmail.com");
        user.setPassword("pass");
        user.setActive(true);
        model.saveUser(user);

        Group group = new Group();
        group.setTitle("Group");
        model.saveGroup(group);

        UserGroup userGroup = new UserGroup();
        userGroup.setGroup(group);
        userGroup.setUser(user);
        model.saveUserGroup(userGroup);
        */
    }
}
