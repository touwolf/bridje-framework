
package ${model.package};

import java.sql.SQLException;
import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import org.bridje.ioc.Ioc;
import org.bridje.ioc.thls.Thls;
import org.bridje.ioc.thls.ThlsAction;
import org.bridje.ioc.thls.ThlsActionException;
import org.bridje.ioc.thls.ThlsActionException2;
import org.bridje.orm.*;

/**
 * This class represents the ${model.name} data model.
 * it must be use to read and write ${model.name} entities.
 * The full list of ${model.name} entities is the following.
 * <ul>
<#list model.entities as entity >
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
     * Retrieve all the tables for the entities that are handled by this model.
     * @return all the tables for the entities that are handled by this model.
     */
    public Table<?>[] tables()
    {
        Table<?>[] tables = new Table<?>[]
        {
            <#list model.entities as entity >
            ${entity.name}.TABLE<#if entity_has_next>,</#if>
            </#list>
        };
        return tables;
    }

    /**
     * Performs the given action with a new model in the current thread local storage.
     * @param <T> The result type for the action.
     * @param action The action to be executed.
     * @param model The ${model.name}.
     * @return The result of the action execution.
     */
    public static <T> T doWithModel(ThlsAction<T> action, ${model.name} model)
    {
        return Thls.doAs(action, ${model.name}.class, model);
    }

    /**
     * Performs the given action with a new model in the current thread local storage.
     * @param <T> The result type for the action.
     * @param <E> The type of the first exception.
     * @param action The action to be executed.
     * @param model The ${model.name}.
     * @return The result of the action execution.
     * @throws E If any exception is throws.
     */
    public static <T, E extends Throwable> T doWithModelEx(ThlsActionException<T, E> action, ${model.name} model) throws E
    {
        return Thls.doAsEx(action, ${model.name}.class, model);
    }

    /**
     * Performs the given action with a new model in the current thread local storage.
     * @param <T> The result type for the action.
     * @param <E> The type of the first exception.
     * @param <E2> The type of the second exception.
     * @param action The action to be executed.
     * @param model The ${model.name}.
     * @return The result of the action execution.
     * @throws E If any exception is throws.
     * @throws E2 If any exception is throws.
     */
    public static <T, E extends Throwable, E2 extends Throwable> T doWithModelEx2(ThlsActionException2<T, E, E2> action, ${model.name} model) throws E, E2
    {
        return Thls.doAsEx2(action, ${model.name}.class, model);
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

    /**
     * Creates a new query with the given entity as the base entity for the
     * query, the object returned by this method can be customized to build must
     * common queries you'l whant to execute on the database.
     *
     * @param <T> The type of the entity.
     * @param table The entity table to be query.
     * @return A new Query object.
     */
    public <T> Query<T> query(Table<T> table)
    {
        return context.query(table);
    }

    /**
     * Obtains the entity context.
     *
     * @return The entity context object.
     */
    public EntityContext getContext()
    {
        return context;
    }

    /**
     * Clears the internal cache of the entity context, so new queries retrieve
     * fresh data from the database, note that entities returned from this
     * context will be cached, so if you what to reset the context and release
     * memory this method must be call.
     */
    public void clearCache()
    {
        context.clearCache();
    }

    /**
     * Finds the table for the given entity.
     *
     * @param <T> The type of the entity.
     * @param entity The class of the entity.
     * @return The table object for the given entity.
     */
    public <T> Table<T> findTable(Class<T> entity)
    {
        return context.findTable(entity);
    }

    /**
     * Determines when ever this model has the given entity.
     *
     * @param <T> The type of the entity.
     * @param entity The class of the entity.
     * @return true the given class is an entity of this model, false otherwise.
     */
    public <T> boolean haveEntity(Class<T> entity)
    {
        if(TABLE_SET == null)
        {
            Set<Class<?>> tbSet = new HashSet<>();
            <#list model.entities as entity >
            tbSet.add(${entity.name}.class);
            </#list>
            TABLE_SET = tbSet;
        }
        return TABLE_SET.contains(entity);
    }

    /**
     * This method will find an entity given his class and id.
     *
     * @param <T> The type of the entity.
     * @param table The entity table to be find.
     * @param id The id of the entity to be find.
     * @return The finded entity, or null if no entity can be found by that id.
     * @throws SQLException If any SQLException occurs.
     */
    public <T> T find(Table<T> table, Object id) throws SQLException
    {
        return context.find(table, id);
    }

    /**
     * This method will update all the fields of the entity from the actual
     * values in the database.
     *
     * @param <T> The type of the entity.
     * @param entity The entity to be refreshed.
     * @return The same entity passed to this method but with the fields
     * refreshed.
     * @throws SQLException If any SQLException occurs.
     */
    public <T> T refresh(T entity) throws SQLException
    {
        return context.refresh(entity);
    }

    <#list model.entities as entity >
    <#list entity.operations as crudOp >
    <#if crudOp.operationType == "CREATE" >
    /**
     * This method creates a new ${entity.name} entity. and insert it into the database.
    <#list crudOp.params as param>
     * @param ${param.name} ${param.description!}
    </#list>
     * @return The created ${entity.name} object.
     * @throws SQLException If any SQLException occurs.
     */
    public ${entity.name} ${crudOp.name}(<#list crudOp.params as param>${param.javaType} ${param.name}<#if param_has_next>, </#if></#list>) throws SQLException
    {
        ${entity.name} entity = new ${entity.name}();
        <#list crudOp.setFields as setField>
        entity.set${setField.field.name?cap_first}(${setField.value});
        </#list>
        <#list crudOp.params as param>
        entity.set${param.name?cap_first}(${param.name});
        </#list>
        context.insert(entity);
        return entity;
    }

    <#elseif crudOp.operationType == "READ" && crudOp.resultType == "ALL" >
    /**
     * This method finds a list of <#if crudOp.resultField??>${crudOp.resultField.javaType}<#else>${entity.name}</#if> object from the database.
    <#list crudOp.params as param>
     * @param ${param.name} ${param.description!}
    </#list>
     * @return A list of <#if crudOp.resultField??>${crudOp.resultField.javaType}<#else>${entity.name}</#if> objects.
     * @throws SQLException If any SQLException occurs.
     */
    public List<<#if crudOp.resultField??>${crudOp.resultField.javaType}<#else>${entity.name}</#if>> ${crudOp.name}(<#list crudOp.params as param>${param.javaType} ${param.name}<#if param_has_next>, </#if></#list>) throws SQLException
    {
        return context.query(${entity.name}.TABLE)
                        .where(
                            <#assign first = true />
                            <#list crudOp.params as param>
                            <#if first>
                            ${param.entity.name}.${param.column?upper_case}.eq(${param.name})
                            <#else>
                            .and(${param.entity.name}.${param.column?upper_case}.eq(${param.name}))
                            </#if>
                            <#assign first = false />
                            </#list>
                            <#list crudOp.conditions as cond>
                            <#if first>
                            ${cond.field.entity.name}.${cond.field.column?upper_case}.${cond.operator}(${cond.value})
                            <#else>
                            .and(${cond.field.entity.name}.${cond.field.column?upper_case}.${cond.operator}(${cond.value}))
                            </#if>
                            <#assign first = false />
                            </#list>
                        )
                        .fetchAll(<#if crudOp.resultField??>${entity.name}.${crudOp.resultField.column?upper_case}</#if>);
    }

    <#elseif crudOp.operationType == "READ" && crudOp.resultType == "ONE" >
    /**
     * This method finds a <#if crudOp.resultField??>${crudOp.resultField.javaType}<#else>${entity.name}</#if> object from the database.
    <#list crudOp.params as param>
     * @param ${param.name} ${param.description!}
    </#list>
     * @return A <#if crudOp.resultField??>${crudOp.resultField.javaType}<#else>${entity.name}</#if> object.
     * @throws SQLException If any SQLException occurs.
     */
    public <#if crudOp.resultField??>${crudOp.resultField.javaType}<#else>${entity.name}</#if> ${crudOp.name}(<#list crudOp.params as param>${param.javaType} ${param.name}<#if param_has_next>, </#if></#list>) throws SQLException
    {
        return context.query(${entity.name}.TABLE)
                        .where(
                            <#assign first = true />
                            <#list crudOp.params as param>
                            <#if first>
                            ${param.entity.name}.${param.column?upper_case}.eq(${param.name})
                            <#else>
                            .and(${param.entity.name}.${param.column?upper_case}.eq(${param.name}))
                            </#if>
                            <#assign first = false />
                            </#list>
                            <#list crudOp.conditions as cond>
                            <#if first>
                            ${cond.field.entity.name}.${cond.field.column?upper_case}.${cond.operator}(${cond.value})
                            <#else>
                            .and(${cond.field.entity.name}.${cond.field.column?upper_case}.${cond.operator}(${cond.value}))
                            </#if>
                            <#assign first = false />
                            </#list>
                        )
                        .fetchOne(<#if crudOp.resultField??>${entity.name}.${crudOp.resultField.column?upper_case}</#if>);
    }

    <#elseif crudOp.operationType == "UPDATE" >
    /**
     * This method updates an ${entity.name} object into the database.
     * @param entity The entity to be updated.
    <#list crudOp.params as param>
     * @param ${param.name} ${param.description!}
    </#list>
     * @throws SQLException If any SQLException occurs.
     */
    public void ${crudOp.name}(${entity.name} entity<#list crudOp.params as param>, ${param.javaType} ${param.name}</#list>) throws SQLException
    {
        <#list crudOp.setFields as setField>
        entity.set${setField.field.name?cap_first}(${setField.value});
        </#list>
        <#list crudOp.params as param>
        entity.set${param.name?cap_first}(${param.name});
        </#list>
        context.update(entity);
    }

    <#elseif crudOp.operationType == "DELETE_ENTITY" >
    /**
     * This method deletes the given ${entity.name} object into the database.
     * @param entity The entity to be deleted.
     * @throws SQLException If any SQLException occurs.
     */
    public void ${crudOp.name}(${entity.name} entity) throws SQLException
    {
        context.delete(entity);
    }

    <#elseif crudOp.operationType == "DELETE">
    /**
     * This method deletes all ${entity.name} objects in the database that match the given parameters.
     * @return An integer representing the number of record deleted in the database.
    <#list crudOp.params as param>
     * @param ${param.name} ${param.description!}
    </#list>
     * @throws SQLException If any SQLException occurs.
     */
    public int ${crudOp.name}(<#list crudOp.params as param>${param.javaType} ${param.name}<#if param_has_next>, </#if></#list>) throws SQLException
    {
        return context.query(${entity.name}.TABLE)
                        .where(
                            <#assign first = true />
                            <#list crudOp.params as param>
                            <#if first>
                            ${param.entity.name}.${param.column?upper_case}.eq(${param.name})
                            <#else>
                            .and(${param.entity.name}.${param.column?upper_case}.eq(${param.name}))
                            </#if>
                            <#assign first = false />
                            </#list>
                            <#list crudOp.conditions as cond>
                            <#if first>
                            ${cond.field.entity.name}.${cond.field.column?upper_case}.${cond.operator}(${cond.value})
                            <#else>
                            .and(${cond.field.entity.name}.${cond.field.column?upper_case}.${cond.operator}(${cond.value}))
                            </#if>
                            <#assign first = false />
                            </#list>
                        )
                        .delete();
    }

    <#elseif crudOp.operationType == "SAVE">
    /**
     * This method save a new ${entity.name} object in the database.
     * @param entity The entity to be saved.
     * @throws SQLException If any SQLException occurs.
     */
    public void save(${entity.name} entity) throws SQLException
    {
        <#list crudOp.setFields as setField>
        if(entity.get${setField.field.name?cap_first}() == null)
        {
            entity.set${setField.field.name?cap_first}(${setField.value});
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
