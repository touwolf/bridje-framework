/*
 * Copyright 2016 Bridje Framework.
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

import javax.sql.DataSource;
import org.bridje.ioc.Ioc;
import org.bridje.jdbc.JdbcService;
import org.bridje.jdbc.config.DataSourceConfig;
import org.bridje.orm.impl.EntityContextImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gilbe
 */
public class EntityContextTest
{
    
    public EntityContextTest()
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

    /**
     * Test of find method, of class EntityContext.
     */
    @Test
    public void testFind()
    {
        JdbcService jdbcServ = Ioc.context().find(JdbcService.class);
        DataSourceConfig config = new DataSourceConfig();
        config.setDriver("com.mysql.jdbc.Driver");
        config.setUrl("jdbc:mysql://localhost:3306/testdb");
        config.setUser("root");
        config.setName("TestDB");
        config.setPassword("asd");
        DataSource ds = jdbcServ.createDataSource(config);
        EntityContext instance = new EntityContextImpl(ds);
        //instance.fixTable(User.class);
        
        /*
        User user = instance.find(User.class, 1l);
        instance.delete(user);
        user = instance.find(User.class, 1l);
        assertNull(user);

        instance.insert(new User(1l, "Admin"));
        User user = instance.find(User.class, 1l);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getName());
        assertEquals(user.getId().longValue(), 1l);
        assertEquals(user.getName(), "Admin");

        instance.insert(new User(2l, "Admin 1"));
        user = instance.find(User.class, 2l);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getName());
        assertEquals(user.getId().longValue(), 2l);
        assertEquals(user.getName(), "Admin 1");
        */
    }
}
