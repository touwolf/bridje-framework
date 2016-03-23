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

/**
 * 
 */
public interface EntityContext
{
    <T> void fixTable(Class<T> entityClass);
    
    <T> T find(Class<T> entityClass, Object id);

    <T> T insert(T entity);
    
    <T> T update(T entity);
    
    <T> T refresh(T entity);

    <T> T delete(T entity);

    <T> Query<T> query(Table<T> entityTable);
    
    void clearCache();
}
