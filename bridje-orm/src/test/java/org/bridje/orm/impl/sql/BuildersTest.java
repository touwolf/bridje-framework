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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BuildersTest
{
    @Test
    public void testCreateTable()
    {
        DDLBuilder b = new DDLBuilder();
        String query = b.createTable("my_table")
                .column(b.buildColumnStmt("id", "BIGINT", 0, 0, true, true, false, null))
                .column(b.buildColumnStmt("name", "VARCHAR", 100, 0, false, false, false, "NULL"))
                .primaryKey("id")
                .toString();

        String expected = "CREATE TABLE my_table(\n" +
                          "    id BIGINT NOT NULL AUTO_INCREMENT, \n" +
                          "    name VARCHAR(100) NULL DEFAULT NULL, \n" +
                          "    PRIMARY KEY (id)\n" +
                          ")";
        assertEquals(expected, query);
    }

    @Test
    public void testAlterTableColumn()
    {
        DDLBuilder b = new DDLBuilder();
        String query = b.alterTable("my_table")
                .addColumn(b.buildColumnStmt("other_col", "DECIMAL", 8, 2, false, false, false, "NULL"))
                .toString();

        String expected = "ALTER TABLE my_table\n" +
                         "    ADD other_col DECIMAL(8, 2) NULL DEFAULT NULL\n";
        assertEquals(expected, query);
    }

    @Test
    public void testDeleteWithConditionTuples()
    {
        DeleteBuilder d = new DeleteBuilder();
        String query = d.delete("my_table")
                .where("my_table.id=1").toString();

        String expected = "DELETE FROM my_table WHERE my_table.id=1";
        assertEquals(expected, query);
    }

    @Test
    public void testDeleteWithoutConditionTuples()
    {
        DeleteBuilder d = new DeleteBuilder();
        String query = d.delete("my_table").toString();

        String expected = "DELETE FROM my_table";
        assertEquals(expected, query);
    }

    @Test
    public void testInsertTuples()
    {
        InsertBuilder i = new InsertBuilder();
        String query = i.insertInto("my_table")
                .fields("my_field, my_field_str")
                .values("2, 'value'")
                .toString();

        String expected = "INSERT INTO my_table (my_field, my_field_str)  VALUES (2, 'value')";
        assertEquals(expected, query);
    }

    @Test
    public void testUpdateTuples()
    {
        UpdateBuilder u = new UpdateBuilder();
        String query = u.update("my_table")
                .set("my_field='new value'")
                .where("my_field <> 'new value'")
                .toString();

        String expected = "UPDATE my_table SET my_field='new value' = ? WHERE my_field <> 'new value'";
        assertEquals(expected, query);
    }

    @Test
    public void testSelectTuples()
    {
        SelectBuilder s = new SelectBuilder(null);
        String query = s.select("id")
                .from("my_table")
                .where("my_field <> 'new value'")
                .toString();

        String expected = "SELECT id FROM my_table WHERE my_field <> 'new value'";
        assertEquals(expected, query);
    }
}
