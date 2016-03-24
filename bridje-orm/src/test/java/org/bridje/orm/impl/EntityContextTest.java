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
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.bridje.ioc.Ioc;
import org.bridje.jdbc.JdbcService;
import org.bridje.jdbc.config.DataSourceConfig;
import org.bridje.orm.EntityContext;
import org.bridje.orm.OrmService;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EntityContextTest
{
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
    public void test1FixDataBase()
    {
        ctx.fixTable(User.class);
        ctx.fixTable(Group.class);
        assertEquals(0, ctx.query(Group_.table).count());
        assertEquals(0, ctx.query(User_.table).count());
    }

    @Test
    public void test2Insert()
    {
        Group group = new Group(1l, "Admins");
        ctx.insert(group);
        
        User user = new User(1l, "Admin");
        user.setAge((short)30);
        user.setBrithday(new Date());
        user.setClasif('A');
        user.setCounts((byte)1);
        user.setCreated(new Timestamp(System.currentTimeMillis()));
        user.setCredit(50.5f);
        user.setEnable(true);
        user.setHour(new Time(System.currentTimeMillis()));
        user.setMins(10);
        user.setMoney(100.40d);
        user.setUpdated(new java.sql.Date(System.currentTimeMillis()));
        user.setYear(2016l);
        user.setGroup(group);
        ctx.insert(user);
        
        assertEquals(1, ctx.query(Group_.table).count());
        assertEquals(1, ctx.query(User_.table).count());
    }
    
    @Test
    public void test3Select()
    {
        ctx.clearCache();
        User user = ctx.find(User.class, 1l);
        assertNotNull(user);

        assertEquals((short)30, user.getAge().shortValue());
        assertNotNull(user.getBrithday());
        assertEquals('A', user.getClasif().charValue());
        assertEquals((byte)1, user.getCounts().byteValue());
        assertNotNull(user.getCreated());
        assertEquals(50.5f, user.getCredit(), 0f);
        assertEquals(true, user.getEnable());
        assertNotNull(user.getHour());
        assertEquals(10, user.getMins().intValue());
        assertEquals(100.40d, user.getMoney(), 0d);
        assertNotNull(user.getUpdated());
        assertEquals(2016l, user.getYear().longValue());
        assertNotNull(user.getGroup());
        assertEquals(1l, user.getGroup().getId().longValue());
        assertEquals("Admins", user.getGroup().getName());

        assertEquals(1, ctx.query(Group_.table).count());
        assertEquals(1, ctx.query(User_.table).count());
    }

    @Test
    public void test3Update()
    {
        User user = ctx.find(User.class, 1l);
        user.setAge((short)40);
        user.setBrithday(new Date());
        user.setClasif('B');
        user.setCounts((byte)2);
        user.setCreated(new Timestamp(System.currentTimeMillis()));
        user.setCredit(30.5f);
        user.setEnable(false);
        user.setHour(new Time(System.currentTimeMillis()));
        user.setMins(20);
        user.setMoney(500.40d);
        user.setUpdated(new java.sql.Date(System.currentTimeMillis()));
        user.setYear(2017l);
        user.setGroup(ctx.insert(new Group(2l, "Admins 2")));
        assertNotNull(user);

        ctx.update(user);
        ctx.clearCache();
        user = ctx.find(User.class, 1l);

        assertEquals((short)40, user.getAge().shortValue());
        assertNotNull(user.getBrithday());
        assertEquals('B', user.getClasif().charValue());
        assertEquals((byte)2, user.getCounts().byteValue());
        assertNotNull(user.getCreated());
        assertEquals(30.5f, user.getCredit(), 0f);
        assertEquals(false, user.getEnable());
        assertNotNull(user.getHour());
        assertEquals(20, user.getMins().intValue());
        assertEquals(500.40d, user.getMoney(), 0d);
        assertNotNull(user.getUpdated());
        assertEquals(2017l, user.getYear().longValue());
        assertNotNull(user.getGroup());
        assertEquals(2l, user.getGroup().getId().longValue());
        assertEquals("Admins 2", user.getGroup().getName());

        assertEquals(2, ctx.query(Group_.table).count());
        assertEquals(1, ctx.query(User_.table).count());
        List<Group> groups = ctx.query(Group_.table).orderBy(Group_.id.asc()).fetchAll();
        assertEquals(1l, groups.get(0).getId().longValue());
        assertEquals(2l, groups.get(1).getId().longValue());
    }

    @Test
    public void test4Functions()
    {
        User user = new User(2l, "Admin 3");
        user.setAge((short)20);
        user.setBrithday(new Date());
        user.setClasif('C');
        user.setCounts((byte)3);
        user.setCreated(new Timestamp(System.currentTimeMillis()));
        user.setCredit(6.5f);
        user.setEnable(true);
        user.setHour(new Time(System.currentTimeMillis()));
        user.setMins(9);
        user.setMoney(150.40d);
        user.setUpdated(new java.sql.Date(System.currentTimeMillis()));
        user.setYear(2018l);
        user.setGroup(ctx.find(Group.class, 1l));
        assertNotNull(ctx.insert(user));

        assertEquals(60, ctx.query(User_.table).fetchOne(User_.age.sum()).shortValue());
        assertEquals(5, ctx.query(User_.table).fetchOne(User_.counts.sum()).byteValue());
        assertEquals(37.0f, ctx.query(User_.table).fetchOne(User_.credit.sum()), 0.01f);
        assertEquals(7, ctx.query(User_.table).where(User_.name.eq("Admin 3")).fetchOne(User_.name.length()).intValue());
        assertEquals(9, ctx.query(User_.table).where(User_.name.eq("Admin 3")).fetchOne(User_.name.length().puls(2)).intValue());
        assertEquals("Admin 3", ctx.query(User_.table).where(User_.name.length().eq(7)).fetchOne(User_.name));
        assertEquals("Admin 3", ctx.query(User_.table).where(User_.name.length().puls(1).eq(8)).fetchOne(User_.name));
    }

    @Test
    public void test5Delete()
    {
        assertNotNull(ctx.find(Group.class, 1l));
        assertNotNull(ctx.find(Group.class, 2l));
        assertNotNull(ctx.find(User.class, 1l));
        assertNotNull(ctx.find(User.class, 2l));
        
        ctx.delete(ctx.find(User.class, 1l).getGroup());
        ctx.delete(ctx.find(User.class, 1l));
        ctx.delete(ctx.find(User.class, 2l).getGroup());
        ctx.delete(ctx.find(User.class, 2l));
        
        assertNull(ctx.find(Group.class, 1l));
        assertNull(ctx.find(Group.class, 2l));
        assertNull(ctx.find(User.class, 1l));
        assertNull(ctx.find(User.class, 2l));
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
