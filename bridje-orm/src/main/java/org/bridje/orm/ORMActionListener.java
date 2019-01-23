package org.bridje.orm;

import org.bridje.sql.Table;

public interface ORMActionListener
{
    void entityAdded(Table table, Class entityClass, Object entityObject);

    void entityUpdated(Table table, Class entityClass, Object entityObject);

    void entityRemoved(Table table, Class entityClass, Object entityKey);
}
