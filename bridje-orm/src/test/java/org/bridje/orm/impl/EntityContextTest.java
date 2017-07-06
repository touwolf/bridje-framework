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
        ctx = Ioc.context().find(OrmService.class).createModel(ds, TestOrmModel.class).getContext();
    }

    @Test
    public void test1FixDataBase() throws SQLException
    {
        ctx.fixTable(User.TABLE);
        ctx.fixTable(Group.TABLE);
        assertEquals(0, ctx.query(Group.TABLE).count());
        assertEquals(0, ctx.query(User.TABLE).count());
    }

    @Test
    public void test2Insert() throws SQLException
    {
        Group group = new Group(1L, "Admins");
        ctx.insert(group);

        User user = new User(1L, "Admin");
        user.setAge((short)30);
        user.setBrithday(new Date());
        user.setClasif('A');
        user.setCounts((byte)1);
        user.setCreated(new Timestamp(System.currentTimeMillis()));
        user.setCredit(50.5F);
        user.setEnable(true);
        user.setHour(new Time(System.currentTimeMillis()));
        user.setMins(10);
        user.setMoney(100.40D);
        user.setUpdated(new java.sql.Date(System.currentTimeMillis()));
        user.setYear(2016L);
        user.setGroup(group);
        ctx.insert(user);

        assertEquals(1, ctx.query(Group.TABLE).count());
        assertEquals(1, ctx.query(User.TABLE).count());
    }

    @Test
    public void test2QueryInsert() throws SQLException
    {
        Group group = new Group(11L, "Admins 11");
        ctx.query(Group.TABLE)
            .set(Group.ID, group.getId())
            .set(Group.NAME, group.getName())
            .insert();

        User user = new User(11L, "Admin 11");
        user.setAge((short)31);
        user.setBrithday(new Date());
        user.setClasif('D');
        user.setCounts((byte)1);
        user.setCreated(new Timestamp(System.currentTimeMillis()));
        user.setCredit(0.5F);
        user.setEnable(true);
        user.setHour(new Time(System.currentTimeMillis()));
        user.setMins(11);
        user.setMoney(0.40D);
        user.setUpdated(new java.sql.Date(System.currentTimeMillis()));
        user.setYear(2011L);
        user.setGroup(group);
        ctx.insert(user);

        assertEquals(2, ctx.query(Group.TABLE).count());
        assertEquals(2, ctx.query(User.TABLE).count());
    }

    @Test
    public void test3Select() throws SQLException
    {
        ctx.clearCache();
        User user = ctx.find(User.TABLE, 1L);
        assertNotNull(user);

        assertEquals((short)30, user.getAge().shortValue());
        assertNotNull(user.getBrithday());
        assertEquals('A', user.getClasif().charValue());
        assertEquals((byte)1, user.getCounts().byteValue());
        assertNotNull(user.getCreated());
        assertEquals(50.5F, user.getCredit(), 0F);
        assertEquals(true, user.getEnable());
        assertNotNull(user.getHour());
        assertEquals(10, user.getMins().intValue());
        assertEquals(100.40d, user.getMoney(), 0D);
        assertNotNull(user.getUpdated());
        assertEquals(2016L, user.getYear().longValue());
        assertNotNull(user.getGroup());
        assertEquals(1L, user.getGroup().getId().longValue());
        assertEquals("Admins", user.getGroup().getName());

        assertEquals(2, ctx.query(Group.TABLE).count());
        assertEquals(2, ctx.query(User.TABLE).count());
    }

    @Test
    public void test3Update() throws SQLException
    {
        User user = ctx.find(User.TABLE, 1L);
        user.setAge((short)40);
        user.setBrithday(new Date());
        user.setClasif('B');
        user.setCounts((byte)2);
        user.setCreated(new Timestamp(System.currentTimeMillis()));
        user.setCredit(30.5F);
        user.setEnable(false);
        user.setHour(new Time(System.currentTimeMillis()));
        user.setMins(20);
        user.setMoney(500.40D);
        user.setUpdated(new java.sql.Date(System.currentTimeMillis()));
        user.setYear(2017L);
        user.setGroup(ctx.insert(new Group(2L, "Admins 2")));
        assertNotNull(user);

        ctx.update(user);
        ctx.clearCache();
        user = ctx.find(User.TABLE, 1L);

        assertEquals((short)40, user.getAge().shortValue());
        assertNotNull(user.getBrithday());
        assertEquals('B', user.getClasif().charValue());
        assertEquals((byte)2, user.getCounts().byteValue());
        assertNotNull(user.getCreated());
        assertEquals(30.5F, user.getCredit(), 0F);
        assertEquals(false, user.getEnable());
        assertNotNull(user.getHour());
        assertEquals(20, user.getMins().intValue());
        assertEquals(500.40d, user.getMoney(), 0D);
        assertNotNull(user.getUpdated());
        assertEquals(2017L, user.getYear().longValue());
        assertNotNull(user.getGroup());
        assertEquals(2L, user.getGroup().getId().longValue());
        assertEquals("Admins 2", user.getGroup().getName());

        assertEquals(3, ctx.query(Group.TABLE).count());
        assertEquals(2, ctx.query(User.TABLE).count());
        List<Group> groups = ctx.query(Group.TABLE).orderBy(Group.ID.asc()).fetchAll();
        assertEquals(1L, groups.get(0).getId().longValue());
        assertEquals(2L, groups.get(1).getId().longValue());
    }

    @Test
    public void test4Functions() throws SQLException
    {
        User user = new User(2L, "Admin 3");
        user.setAge((short)20);
        user.setBrithday(new Date());
        user.setClasif('C');
        user.setCounts((byte)3);
        user.setCreated(new Timestamp(System.currentTimeMillis()));
        user.setCredit(6.5F);
        user.setEnable(true);
        user.setHour(new Time(System.currentTimeMillis()));
        user.setMins(9);
        user.setMoney(150.40D);
        user.setUpdated(new java.sql.Date(System.currentTimeMillis()));
        user.setYear(2018L);
        user.setGroup(ctx.find(Group.TABLE, 1L));
        assertNotNull(ctx.insert(user));

        assertEquals(91, ctx.query(User.TABLE).fetchOne(User.AGE.sum()).shortValue());
        assertEquals(6, ctx.query(User.TABLE).fetchOne(User.COUNTS.sum()).byteValue());
        assertEquals(37.5F, ctx.query(User.TABLE).fetchOne(User.CREDIT.sum()), 0.01F);
        assertEquals(7, ctx.query(User.TABLE).where(User.NAME.eq("Admin 3")).fetchOne(User.NAME.length()).intValue());
        assertEquals(9, ctx.query(User.TABLE).where(User.NAME.eq("Admin 3")).fetchOne(User.NAME.length().plus(2)).intValue());
        assertEquals("Admin 3", ctx.query(User.TABLE).where(User.NAME.length().eq(7)).fetchOne(User.NAME));
        assertEquals("Admin 3", ctx.query(User.TABLE).where(User.NAME.length().plus(1).eq(8)).fetchOne(User.NAME));
        assertEquals("Admin 3", ctx.query(User.TABLE).where(User.NAME.in("Admin 3", "Admin 49")).fetchOne(User.NAME));
        assertEquals("Admin 3", ctx.query(User.TABLE).where(User.MONEY.in(100D, 150.40D)).fetchOne(User.NAME));
    }

    @Test
    public void test5FetchRelations() throws SQLException
    {
        List<Group> groups = ctx.query(User.TABLE).orderBy(User.GROUP.asc()).fetchAll(User.GROUP);
        assertEquals(3, groups.size());
        assertNotNull(groups.get(0));
        assertNotNull(groups.get(1));
        assertEquals(1L, groups.get(0).getId().longValue());
        assertEquals(2L, groups.get(1).getId().longValue());

        User user = ctx.query(User.TABLE).where(User.GROUP.eq(groups.get(0))).fetchOne();
        user.setGroup(groups.get(1));
        ctx.update(user);

        groups = ctx.query(User.TABLE).fetchAll(User.GROUP);
        assertEquals(3, groups.size());
        assertNotNull(groups.get(0));
        assertNotNull(groups.get(1));
        assertEquals(2L, groups.get(0).getId().longValue());
        assertEquals(2L, groups.get(1).getId().longValue());
    }

    @Test
    public void test6Joins() throws SQLException
    {
        assertNotNull(ctx.query(Group.TABLE)
                .where(Group.NAME.eq("Admins 2"))
                .fetchOne());
        assertNotNull(ctx.query(User.TABLE)
                .orderBy(User.AGE.asc())
                .join(User.GROUP)
                .where(Group.NAME.eq("Admins 2").or(User.NAME.like("%Admin%")))
                .orderBy(Group.NAME.asc())
                .fetchOne());
    }

    @Test
    public void test7Delete() throws SQLException
    {
        assertNotNull(ctx.find(Group.TABLE, 1L));
        assertNotNull(ctx.find(Group.TABLE, 2L));
        assertNotNull(ctx.find(User.TABLE, 1L));
        assertNotNull(ctx.find(User.TABLE, 2L));

        ctx.delete(ctx.find(Group.TABLE, 1L));
        ctx.delete(ctx.find(User.TABLE, 1L));
        ctx.delete(ctx.find(Group.TABLE, 2L));
        ctx.delete(ctx.find(User.TABLE, 2L));

        assertNull(ctx.find(Group.TABLE, 1L));
        assertNull(ctx.find(Group.TABLE, 2L));
        assertNull(ctx.find(User.TABLE, 1L));
        assertNull(ctx.find(User.TABLE, 2L));
    }

    @Test
    public void test8AutoIncrement() throws SQLException
    {
        ctx.fixTable(Rol.TABLE);
        Rol rol = ctx.insert(new Rol("Admin Rol"));
        assertNotNull(rol.getId());
        assertEquals(1, rol.getId().intValue());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void deleteDataBase(String tageth2testdb)
    {
        File f = new File(tageth2testdb);
        if(f.exists())
        {
            f.delete();
        }
    }
}
