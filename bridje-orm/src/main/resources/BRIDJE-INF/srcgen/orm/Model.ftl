
package ${model.package};

import java.sql.SQLException;
import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import org.bridje.ioc.Ioc;
import org.bridje.ioc.thls.Thls;
import org.bridje.ioc.thls.ThlsAction;
import org.bridje.orm.*;

/**
 * This class represents the ${model.name} data model.
 * it must be use to read and write ${model.name} entitys.
 * The full list of ${model.name} entitys is the following.
 * <ul>
<#list model.entitys as entity >
 * <li>${entity.name}<br></li>
</#list>
 * </ul>
 */
public class ${model.name}
{
    private static Set<Class<?>> TABLE_SET;

    private final EntityContext context;

    /**
     * Creates a new model from an EntityContext object.
     * @param context The context to be use.
     */
    public ${model.name}(EntityContext context)
    {
        this.context = context;
    }

    /**
     * Creates a new model from an DataSource object.
     * @param dataSource The datasource to be use.
     */
    public ${model.name}(DataSource dataSource)
    {
        this(Ioc.context().find(OrmService.class).createContext(dataSource));
    }

    /**
     * Creates a new model from the name of the  DataSource to be use.
     * @param dataSourceName The name of the dataSource to be use.
     */
    public ${model.name}(String dataSourceName)
    {
        this(Ioc.context().find(OrmService.class).createContext(dataSourceName));
    }

    /**
     * Retrieve all the tables for the entitys that are handled by this model.
     * @return all the tables for the entitys that are handled by this model.
     */
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

    /**
     * Performs the given action with a new model in the current thread local storage.
     * @param <T> The result type for the action.
     * @param action The action to be executed.
     * @param dataSource The DataSource
     * @return The result of the action execution.
     * @throws java.lang.Exception If any exception is throws.
     */
    public static <T> T doWithModel(ThlsAction<T> action, DataSource dataSource) throws Exception
    {
        return doWithModel(action, new ${model.name}(dataSource));
    }

    /**
     * Performs the given action with a new model in the current thread local storage.
     * @param <T> The result type for the action.
     * @param action The action to be executed.
     * @param dataSourceName The DataSource name.
     * @return The result of the action execution.
     * @throws java.lang.Exception If any exception is throws.
     */
    public static <T> T doWithModel(ThlsAction<T> action, String dataSourceName) throws Exception
    {
        return doWithModel(action, new ${model.name}(dataSourceName));
    }

    /**
     * Performs the given action with a new model in the current thread local storage.
     * @param <T> The result type for the action.
     * @param action The action to be executed.
     * @param context The EntityContext.
     * @return The result of the action execution.
     * @throws java.lang.Exception If any exception is throws.
     */
    public static <T> T doWithModel(ThlsAction<T> action, EntityContext context) throws Exception
    {
        return doWithModel(action, new ${model.name}(context));
    }

    /**
     * Performs the given action with a new model in the current thread local storage.
     * @param <T> The result type for the action.
     * @param action The action to be executed.
     * @param model The ${model.name}.
     * @return The result of the action execution.
     * @throws java.lang.Exception If any exception is throws.
     */
    public static <T> T doWithModel(ThlsAction<T> action, ${model.name} model) throws Exception
    {
        return Thls.doAs(action, ${model.name}.class, model);
    }

    /**
     * Gets the model available in the current thread local storage.
     * @return The current ${model.name} model.
     */
    public static ${model.name} get()
    {
        return Thls.get(${model.name}.class);
    }

    /**
     * Fix all the tables of the model in the database.
     * @throws java.sql.SQLException If any SQL exception occurs.
     */
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

    public void refresh(Object entity) throws SQLException
    {
        context.refresh(entity);
    }

    <#list model.entitys as entity >
    <#list entity.operations.crud as crudOp >
    <#if crudOp.type == "create">
    public ${entity.name} ${crudOp.name}(<#list crudOp.params as param>${param.javaType} ${param.name}<#if param_has_next>, </#if></#list>) throws SQLException
    {
        ${entity.name} entity = new ${entity.name}();
        <#list crudOp.setFields as setField>
        entity.set${setField.name?cap_first}(${setField.value});
        </#list>
        <#list crudOp.params as param>
        entity.set${param.name?cap_first}(${param.name});
        </#list>
        context.insert(entity);
        return entity;
    }

    <#elseif crudOp.type == "readAll">
    public List<<#if crudOp.resultField??>${crudOp.resultField.javaType}<#else>${entity.name}</#if>> ${crudOp.name}(<#list crudOp.params as param>${param.javaType} ${param.name}<#if param_has_next>, </#if></#list>) throws SQLException
    {
        return context.query(${entity.name}.TABLE)
                        .where(
                            <#assign first = true />
                            <#list crudOp.params as param>
                            <#if first>
                            ${entity.name}.${param.column?upper_case}.eq(${param.name})
                            <#else>
                            .and(${entity.name}.${param.column?upper_case}.eq(${param.name}))
                            </#if>
                            <#assign first = false />
                            </#list>
                        )
                        .fetchAll(<#if crudOp.resultField??>${entity.name}.${crudOp.resultField.column?upper_case}</#if>);
    }
    
    <#elseif crudOp.type == "readOne">
    public <#if crudOp.resultField??>${crudOp.resultField.javaType}<#else>${entity.name}</#if> ${crudOp.name}(<#list crudOp.params as param>${param.javaType} ${param.name}<#if param_has_next>, </#if></#list>) throws SQLException
    {
        return context.query(${entity.name}.TABLE)
                        .where(
                            <#assign first = true />
                            <#list crudOp.params as param>
                            <#if first>
                            ${entity.name}.${param.column?upper_case}.eq(${param.name})
                            <#else>
                            .and(${entity.name}.${param.column?upper_case}.eq(${param.name}))
                            </#if>
                            <#assign first = false />
                            </#list>
                        )
                        .fetchOne(<#if crudOp.resultField??>${entity.name}.${crudOp.resultField.column?upper_case}</#if>);
    }

    <#elseif crudOp.type == "update">
    public ${entity.name} ${crudOp.name}(${entity.name} entity) throws SQLException
    {
        context.update(entity);
    }
    
    <#elseif crudOp.type == "delete">
    public void ${crudOp.name}(${entity.name} entity) throws SQLException
    {
        context.delete(entity);
    }
    
    <#elseif crudOp.type == "save">
    public void save(${entity.name} entity) throws SQLException
    {
        <#list crudOp.setFields as setField>
        if(entity.get${setField.name?cap_first}() == null)
        {
            entity.set${setField.name?cap_first}(${setField.value});
        }
        </#list>
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
    </#list>
    </#list>
}