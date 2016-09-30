
package ${model.package};

import java.sql.SQLException;
import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;
import org.bridje.ioc.Ioc;
import org.bridje.ioc.thls.Thls;
import org.bridje.ioc.thls.ThlsAction;
import org.bridje.orm.*;

public class ${model.name}
{
    private static Set<Class<?>> TABLE_SET;

    private final EntityContext context;

    public ${model.name}(EntityContext context)
    {
        this.context = context;
    }

    public ${model.name}(DataSource dataSource)
    {
        this(Ioc.context().find(OrmService.class).createContext(dataSource));
    }

    public ${model.name}(String dataSourceName)
    {
        this(Ioc.context().find(OrmService.class).createContext(dataSourceName));
    }

    public Table<?>[] tables()
    {
        Table<?>[] tables = new Table<?>[]
        {
            <#list model.entitys as entity >
            ${entity.name}.TABLE<#if entity_has_next>,</#if>
            </#list>
        };
        return tables;
    }

    public static <T> T doWithModel(ThlsAction<T> action, DataSource dataSource) throws Exception
    {
        return doWithModel(action, new ${model.name}(dataSource));
    }

    public static <T> T doWithModel(ThlsAction<T> action, String dataSourceName) throws Exception
    {
        return doWithModel(action, new ${model.name}(dataSourceName));
    }

    public static <T> T doWithModel(ThlsAction<T> action, EntityContext context) throws Exception
    {
        return doWithModel(action, new ${model.name}(context));
    }

    public static <T> T doWithModel(ThlsAction<T> action, ${model.name} model) throws Exception
    {
        return Thls.doAs(action, ${model.name}.class, model);
    }

    public static ${model.name} get()
    {
        return Thls.get(${model.name}.class);
    }

    public void fixAllTables() throws SQLException
    {
        context.fixTable(tables());
    }

    public <T> Query<T> query(Table<T> table)
    {
        return context.query(table);
    }

    public void clearCache()
    {
        context.clearCache();
    }

    public <T> Table<T> findTable(Class<T> entity)
    {
        return context.findTable(entity);
    }

    public <T> boolean haveEntity(Class<T> entity)
    {
        if(TABLE_SET == null)
        {
            Set<Class<?>> tbSet = new HashSet<>();
            <#list model.entitys as entity >
            tbSet.add(${entity.name}.class);
            </#list>
            TABLE_SET = tbSet;
        }
        return TABLE_SET.contains(entity);
    }

    public <T> T find(Table<T> table, Object id) throws SQLException
    {
        return context.find(table, id);
    }

    <#list model.entitys as entity >
    <#if entity.operations.insert == "yes" >
    public void insert(${entity.name} entity) throws SQLException
    {
        context.insert(entity);
    }

    </#if>
    <#if entity.operations.update == "yes" >
    public void update(${entity.name} entity) throws SQLException
    {
        context.update(entity);
    }

    </#if>
    <#if entity.operations.delete == "yes" >
    public void delete(${entity.name} entity) throws SQLException
    {
        context.delete(entity);
    }

    </#if>
    <#if entity.operations.save == "yes" && entity.keyField.isAutoIncrement>
    public void save(${entity.name} entity) throws SQLException
    {
        if(entity.get${entity.keyField.name?cap_first}() == null)
        {
            context.insert(entity);
        }
        else
        {
            context.update(entity);
        }
    }

    </#if>
    <#if entity.operations.find == "yes" >
    public ${entity.name} find${entity.name}(${entity.keyField.javaType} id) throws SQLException
    {
        return context.find(${entity.name}.TABLE, id);
    }

    </#if>
    <#if entity.operations.refresh == "yes" >
    public void refresh(${entity.name} entity) throws SQLException
    {
        context.refresh(entity);
    }

    </#if>
    <#if entity.operations.query == "yes" >
    public Query<${entity.name}> query${entity.name}() throws SQLException
    {
        return context.query(${entity.name}.TABLE);
    }

    public Query<${entity.name}> query${entity.name}(Condition where) throws SQLException
    {
        return context.query(${entity.name}.TABLE).where(where);
    }

    </#if>
    </#list>
}