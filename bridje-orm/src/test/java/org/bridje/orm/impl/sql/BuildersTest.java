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

package org.bridje.orm.impl.sql;

import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BuildersTest
{
    @Test
    public void test1CreateTable()
    {
        DDLBuilder b = new DDLBuilder("`");
        String query = b.createTable("my_table")
                .column(b.buildColumnStmt("id", "BIGINT", 0, 0, true, true, null))
                .column(b.buildColumnStmt("name", "VARCHAR", 100, 0, false, false, "NULL"))
                .primaryKey("id")
                .toString();
        String expected = "CREATE TABLE `my_table`(\n" +
                          "    `id` BIGINT NOT NULL AUTO_INCREMENT, \n" +
                          "    `name` VARCHAR(100) NULL DEFAULT NULL, \n" +
                          "    PRIMARY KEY (`id`)\n" +
                          ");";
        assertEquals(expected, query);
    }

    @Test
    public void test2CreateColumn()
    {
        DDLBuilder b = new DDLBuilder("`");
        String query = b.alterTable("my_table")
                .addColumn(b.buildColumnStmt("other_col", "DECIMAL", 8, 2, false, false, "NULL"))
                .toString();
        System.out.println(query);
        String expected = "ALTER TABLE `my_table`\n" +
                         "    ADD `other_col` DECIMAL(8, 2) NULL DEFAULT NULL\n" +
                         ";";
        assertEquals(expected, query);
    }

    @Test
    public void testDeleteWithConditionTuples()
    {
        DeleteBuilder d = new DeleteBuilder();
        String query = d.delete("`my_table`")
                .where("`my_table`.`id`=1").toString();

        System.out.println(query);
        String expected = "DELETE FROM `my_table` WHERE `my_table`.`id`=1;";
        assertEquals(expected, query);
    }

    @Test
    public void testDeleteWithoutConditionTuples()
    {
        DeleteBuilder d = new DeleteBuilder();
        String query = d.delete("`my_table`").toString();

        System.out.println(query);
        String expected = "DELETE FROM `my_table`;";
        assertEquals(expected, query);
    }
}
