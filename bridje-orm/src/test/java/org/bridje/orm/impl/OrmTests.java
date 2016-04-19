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

package org.bridje.orm.impl;

import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.bridje.ioc.Ioc;
import org.bridje.jdbc.JdbcService;
import org.bridje.jdbc.config.DataSourceConfig;
import org.bridje.orm.EntityContext;
import org.bridje.orm.OrmService;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
/**
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrmTests
{
    private static final Logger LOG = Logger.getLogger(OrmTests.class.getName());

    private EntityContext ctx;

    @Before
    public void before()
    {
        deleteDataBase("./target/h2testdb.mv.db");
        JdbcService jdbc = Ioc.context().find(JdbcService.class);
        DataSourceConfig config = new DataSourceConfig();
        config.setDriver("org.h2.Driver");
        config.setUrl("jdbc:h2:./target/h2testdb");
        config.setUser("sa");
        config.setName("H2TestDB");
        config.setPassword("");
        DataSource ds = jdbc.createDataSource(config);
        ctx = Ioc.context().find(OrmService.class).createContext(ds);
    }

    @Test
    public void test1FixDataBase() throws SQLException
    {
        try
        {
            ctx.fixTable(User.TABLE);
            assertEquals(0, ctx.query(User.TABLE).count());
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    @Test
    public void test2Insert() throws SQLException
    {
        User user = new User(1l, "Admin");
        ctx.insert(user);
        
        assertEquals(1, ctx.query(User.TABLE).count());
    }

    private void deleteDataBase(String tageth2testdb)
    {
        File f = new File(tageth2testdb);
        if(f.exists())
        {
            f.delete();
        }
    }
}
