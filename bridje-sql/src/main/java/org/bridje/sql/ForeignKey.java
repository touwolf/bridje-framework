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

package org.bridje.sql;

class ForeignKey
{
    private final String name;
    
    private final Table sourceTable;
    
    private Table targetTable;
    
    private final Column<?>[] sourceColumns;
    
    private Column<?>[] targetColumns;

    public ForeignKey(String name, Table sourceTable, Column<?>... sourceColumns)
    {
        this.name = name;
        this.sourceTable = sourceTable;
        this.sourceColumns = sourceColumns;
    }

    public void references(Table targetTable, Column<?>... targetColumns)
    {
        this.targetTable = targetTable;
        this.targetColumns = targetColumns;
    }
    
    public String getName()
    {
        return name;
    }

    public Table getSourceTable()
    {
        return sourceTable;
    }

    public Table getTargetTable()
    {
        return targetTable;
    }

    public Column<?>[] getSourceColumns()
    {
        return sourceColumns;
    }

    public Column<?>[] getTargetColumns()
    {
        return targetColumns;
    }
}
