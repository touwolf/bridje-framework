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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Ioc;
import org.bridje.vfs.CpSource;
import org.bridje.vfs.VFile;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ORMTest
{
    private static final Logger LOG = Logger.getLogger(ORMTest.class.getName());

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
        ORMService ormServ = Ioc.context().find(ORMService.class);
        ORMEnvironment ormEnv = ormServ.createEnvironment()
                                    .model(UsersModel.class, "Derby-DB")
                                    .build();
        UsersModel model = ormEnv.getModel(UsersModel.class);
        model.fixSchema();
        List<User> lst = model.findUsers();
        lst.forEach(u -> {
            try
            {
                model.doDeleteUser(u);
            }
            catch (Exception e)
            {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
        });

        User user = new User();
        user.setEmail("someemail@hotmail.com");
        user.setPassword("pass");
        user.setActive(true);
        user.setCoordinates(new Coordinates(45.1f, 67.2f));
        model.saveUser(user);
        
        lst = model.findUsers();
        Assert.assertNotNull(lst);
        Assert.assertEquals(1, lst.size());
        Assert.assertEquals("someemail@hotmail.com", user.getEmail());
        Assert.assertEquals(45.1f, lst.get(0).getCoordinates().getLatitude(), 0.1f);
        Assert.assertEquals(67.2f, lst.get(0).getCoordinates().getLongitude(), 0.10f);

        Group group = new Group();
        group.setCode("GPU");
        group.setTitle("Group");
        model.insertGroup(group);

        UserGroup userGroup = new UserGroup();
        userGroup.setGroup(group);
        userGroup.setUser(user);
        model.saveUserGroup(userGroup);
    }
}
