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

package org.bridje.dm;

import java.util.List;
/**
 *
 * @author gilberto
 */
public interface Entity
{
    String getName();
    
    Table getTable();

    List<Field> getFields();
    
    List<BasicField> getBasicFields();

    List<EnumField> getEnumFields();

    List<RelationField> getRelationFields();

    Field findField(String name);

    DomainModel getDomainModel();
}
