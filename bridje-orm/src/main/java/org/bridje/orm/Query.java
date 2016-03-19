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

import java.util.List;

/**
 *
 * @param <T>
 */
public interface Query<T>
{
    void paging(int page, int size);

    List<T> fetchAll();

    <C> List<C> fetchAll(Column<T, C> column);
    
    T fetchOne();
    
    <C> C fetchOne(Column<T, C> column);

    long count();

    boolean exists();

    Query<T> by(Condition condition);
}
