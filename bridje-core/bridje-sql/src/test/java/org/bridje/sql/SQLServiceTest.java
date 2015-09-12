/*
 * Copyright 2015 Bridje Framework.
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

import org.bridje.ioc.Ioc;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SQLServiceTest
{
    public SQLServiceTest()
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

    @Test
    public void testSelect()
    {
        SQLService sqlServ = Ioc.context().find(SQLService.class);
        Table users = new Table("users");
        Table groups = new Table("groups");
        Column name = new Column(users, "name");
        Column id = new Column(users, "id");
        Column userId = new Column(groups, "user_id");
        Literal numb1 = new Literal(1);
        Literal numb0 = new Literal(0);
        System.out.println(sqlServ.select(name, numb1)
                .from(users
                        .innerJoin(groups).on(id.eq(userId)))
                .where(id.gt(numb0).or(userId.isNull()))
                .groupBy(id.asc())
                .having(id.isNull())
                .orderBy(userId.desc())
                .getSQL());

        System.out.println("--------------------------------------");
        System.out.println(sqlServ.insertInto(users)
                                    .set(name, numb0)
                                    .set(userId, numb1)
                                    .getSQL());

        System.out.println("--------------------------------------");
        System.out.println(sqlServ.update(groups)
                .set(name, numb0)
                .set(userId, numb1)
                .where(name.eq(name))
                .getSQL());
        
        System.out.println("--------------------------------------");
        System.out.println(sqlServ.delete()
                .from(groups)
                .where(name.eq(name))
                .getSQL());
        
        System.out.println("--------------------------------------");
        System.out.println(sqlServ.delete(groups)
                .from(groups.innerJoin(users).on(id.eq(userId)))
                .where(name.eq(name))
                .getSQL());
    }
}
