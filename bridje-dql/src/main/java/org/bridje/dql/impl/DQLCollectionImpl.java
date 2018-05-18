/*
 * Copyright 2018 Bridje Framework.
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

package org.bridje.dql.impl;

import org.bridje.dql.DQLCollection;
import org.bridje.dql.DQLDocument;
import org.bridje.dql.DQLFilter;
import org.bridje.dql.DQLQuery;

class DQLCollectionImpl implements DQLCollection
{
    private final String name;

    public DQLCollectionImpl(String name)
    {
        this.name = name;
    }

    @Override
    public DQLQuery delete(DQLFilter filter)
    {
        return new DQLDeleteQuery(this, filter);
    }

    @Override
    public DQLQuery distinct(String field, DQLFilter filter)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DQLQuery find(DQLFilter query, DQLDocument fields)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DQLQuery findOne(DQLFilter query, DQLDocument fields)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DQLQuery insertOne(DQLDocument document)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DQLQuery insertMany(DQLDocument[] documents)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DQLQuery updateOne(DQLFilter filter, DQLDocument update)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DQLQuery updateMany(DQLFilter filter, DQLDocument update)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DQLQuery createIndex(DQLDocument keys)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DQLQuery drop()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DQLQuery dropIndex(String index)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DQLQuery dropIndexes()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
