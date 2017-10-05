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

import java.sql.SQLException;
import org.bridje.sql.SQL;
import org.bridje.sql.SQLEnvironment;
import org.bridje.sql.SQLResultSet;
import org.bridje.sql.Schema;

public class TestModel
{
    public static final Schema SCHEMA;

    static {
        SCHEMA = SQL.buildSchema("testdb")
                    .table(User.TABLE)
                    .table(Group.TABLE)
                    .table(UserGroup.TABLE)
                    .build();
    }

    private final EntityContext ctx;

    private final SQLEnvironment env;

    public TestModel(EntityContext orm, SQLEnvironment sql)
    {
        this.ctx = orm;
        this.env = sql;
    }

    public void findSchema() throws SQLException
    {
        env.fixSchema(SCHEMA);
    }

    public User findUser(Long id) throws SQLException
    {
        if(ctx.contains(User.class, id)) return ctx.get(User.class, id);
        return env.fetchOne(User.SELECT, this::parseUser, id);
    }

    public void saveUser(User user) throws SQLException
    {
        if(user.getId() == null)
        {
            Long id = env.fetchOne(User.INSERT, 
                        (rs) -> rs.get(User.ID), 
                        user.getEmail(), 
                        user.getPassword(), 
                        user.getActive());
            user.setId(id);
        }
        else
        {
            env.update(User.UPDATE, 
                        user.getEmail(),
                        user.getPassword(),
                        user.getActive(),
                        user.getId());
        }
        ctx.put(user.getId(), user);
    }

    public void delteUser(User user) throws SQLException
    {
        env.update(User.DELETE, user.getId());
        ctx.remove(User.class, user.getId());
    }

    public void deleteAllUsers() throws SQLException
    {
        env.update(User.DELETE_ALL);
        ctx.clear(User.class);
    }

    public User parseUser(SQLResultSet rs) throws SQLException
    {
        User user = new User();
        user.setId(rs.get(User.ID));
        user.setEmail(rs.get(User.EMAIL));
        user.setPassword(rs.get(User.PASSWORD));
        user.setActive(rs.get(User.ACTIVE));
        ctx.put(user.getId(), user);
        return user;
    }

    public Group findGroup(Long id) throws SQLException
    {
        if(ctx.contains(Group.class, id)) return ctx.get(Group.class, id);
        return env.fetchOne(Group.SELECT, this::parseGroup, id);
    }

    public Group parseGroup(SQLResultSet rs) throws SQLException
    {
        Group group = new Group();
        group.setId(rs.get(Group.ID));
        group.setTitle(rs.get(Group.TITLE));
        ctx.put(group.getId(), group);
        return group;
    }

    public void saveGroup(Group group) throws SQLException
    {
        if(group.getId() == null)
        {
            Long id = env.fetchOne(Group.INSERT, 
                        (rs) -> rs.get(Group.ID), 
                        group.getTitle());
            group.setId(id);
        }
        else
        {
            env.update(User.UPDATE, 
                        group.getTitle(),
                        group.getId());
        }
    }

    public UserGroup findUserGroup(Long id) throws SQLException
    {
        if(ctx.contains(UserGroup.class, id)) return ctx.get(UserGroup.class, id);
        return env.fetchOne(UserGroup.SELECT, this::parseUserGroup, id);
    }

    public UserGroup parseUserGroup(SQLResultSet rs) throws SQLException
    {
        UserGroup entity = new UserGroup();
        entity.setId(rs.get(UserGroup.ID));
        entity.setGroup(rs.get(UserGroup.GROUP, this::findGroup));
        entity.setUser(rs.get(UserGroup.USER, this::findUser));
        ctx.put(entity.getId(), entity);
        return entity;
    }

    public void saveUserGroup(UserGroup userGroup) throws SQLException
    {
        if(userGroup.getId() == null)
        {
            Long id = env.fetchOne(UserGroup.INSERT, 
                        (rs) -> rs.get(UserGroup.ID), 
                        userGroup.getGroup(),
                        userGroup.getUser());
            userGroup.setId(id);
        }
        else
        {
            env.update(User.UPDATE, 
                        userGroup.getGroup(),
                        userGroup.getUser(),
                        userGroup.getId());
        }
    }
}
