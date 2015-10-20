<#include "orm-common.ftl">

package ${dm.package}.impl;

import com.touwolf.bridje.ioc.Ioc;
import com.touwolf.bridje.orm.*;
import com.touwolf.bridje.orm.impl.*;
import com.touwolf.bridje.orm.exceptions.*;
import ${dm.package}.*;
import static com.touwolf.bridje.orm.DataUtils.*;
import java.util.*;
import java.util.logging.Logger;
<@renderExtraImports entity = entity />

/**
 * ${entity.description}
 */

final class ${entity.name}Impl implements ${getEntityImplementationInterfacesDeclaration(entity)}
{
    /**
     * Logger de la clase
     */
    private static final Logger LOG = Logger.getLogger(${entity.name}Impl.class.getName());

    /**
     * El contexto de datos para esta entidad
     */
    private DataContextImpl context;

    /**
     * Lo valores de los campos de esta entidad en base de datos.
     */
    private Object[] record;

    <#if entity.isMultiKeys()>
    /**
     * La llave para esta entidad que encapsula a multiples llaves primarias
     */
    private ${entity.name}Key key;
    <#else>
        <#list entity.keyFields as f><#-- Debe ser uno solo -->
    /**
    * El campo llave ${f.name} de columna ${f.column}
    */
    private ${f.javaType} ${f.name};

        </#list>
    </#if>
    <#list entity.nonKeyFields as f>
    /**
     * El campo ${f.name} (no llave) de la columna ${f.column} de esta entidad
     */
    private ${f.javaType} ${f.name};

    </#list>
    <#list entity.relations as relation>
    <#if relation.returnsOne>
    /**
     * Propiedad de la relacion  de ${relation.name} a ${relation.relatedName}
     */
    private ${relation.relatedEntity.name} ${relation.relatedName?uncap_first};

    </#if>
    </#list>
    /**
     * Constructor usado para crear una nueva entidad en la base de datos.
     */
    ${entity.name}Impl(DataContextImpl context)
    {
        this.context = context;
        this.record = new Object[${entity.fields?size}];
        <#list entity.requiredFields as f>
        <#if f.defaultValue??>
        set${f.name?cap_first}(${setValue(f,f.defaultValue)});
        </#if>
        </#list>
    }

    /**
     * Constructor usado para obtener una entidad de base de datos.
     */
    ${entity.name}Impl(DataContextImpl context, Object[] record)
    {
        this(context);
        this.record = record;
        this.rollback();
    }

    <#if entity.isMultiKeys()>
    /**
     * Obtiene la llave de esta entidad.
     */
    @Override
    public ${entityKey} getKey()
    {
        if(this.key == null)
        {
            this.key = new ${entityKey}();
        }
        return this.key;
    }

    /**
     * Establece la llave de esta entidad
     */
    @Override
    public void setKey(${entityKey} key)
    {
        this.key = key;
    }

    </#if>
    <#list entity.fields as f>
        <#if f.visible>
    @Override
        </#if>
    <#if f.visible>public </#if>${f.javaType} get${f.name?cap_first}()
    {
        <#if f.key &&  entity.isMultiKeys()>
        return getKey().get${f.name?cap_first}();
        <#else>
        <#if f.javaType == "String" && f.trim?? && f.trim>
        if(this.${f.name} != null)
        {
            return this.${f.name}.trim();
        }
        <#else>
        return this.${f.name};
        </#if>
        </#if>
    }

        <#if !f.readonly>
    @Override
        </#if>
    <#if !f.readonly>public </#if>void set${f.name?cap_first}(${f.javaType} value)
    {
        <#if f.key &&  entity.isMultiKeys()>
        getKey().set${f.name?cap_first}(value);
        <#else>
        <#if f.javaType == "Date" && f.sqlType == "DATE" >
        this.${f.name} = truncate(value);
        <#else>
        <#if f.javaType == "String" && f.trim?? && f.trim>
        if(value != null)
        {
            this.${f.name} = value.trim();
        }
        <#else>
        this.${f.name} = value;
        </#if>
        </#if>
        </#if>
    }

    /**
     * Devuelve el valor que esta en base de datos del campo <b>${f.name}</b>
     * @return devuelve un valor de tipo {@link ${f.javaType}}
     */
    ${f.javaType} getOriginal${f.name?cap_first}()
    {
        <#if f.defaultValue??>
        return context.cast(${f.javaType}.class, record[${f_index}], ${f.getJavaCodeValue(f.defaultValue)});
        <#else>
        return context.cast(${f.javaType}.class, record[${f_index}]);
        </#if>
    }

    /**
     * Establece el valor en base de datos del campo <b>${f.name}</b>
     * @param original${f.name?cap_first} Nuevo valor de la llave <b>${f.name}</b> establecido en base de datos
     */
    void setOriginal${f.name?cap_first}(${f.javaType} original${f.name?cap_first})
    {
        record[${f_index}] = original${f.name?cap_first};
    }

    </#list>
    <#if entity.rollback>
    @Override
    </#if>
    <#if entity.rollback>public </#if>void rollback()
    {
        if(record != null)
        {
            <#list entity.fields as f>
            ${getFieldSetterStm(f, getFieldGetterStm(f, "original"))};
            </#list>
        }
    }

    <#if entity.refresh>
    @Override
    </#if>
    <#if entity.refresh>public </#if>void refresh() throws QueryException
    {
        <#list entity.keyFields as f>
        <#if f.autoIncrement?? && f.autoIncrement>
        if(${getFieldGetterStm(f)} <= 0)
        {
            rollback();
            return;
        }
        </#if>
        </#list>
        record = new ${entity.name}QueryImpl(context)
                    <#list entity.keyFields as f >
                    <#if !f.autoIncrement?? || !f.autoIncrement>
                    .by${f.name?cap_first}(${getFieldGetterStm(f,"original")})
                    <#else>
                    .by${f.name?cap_first}(${getFieldGetterStm(f)})
                    </#if>
                    </#list>
                    .fetchOne();
        rollback();
    }


    @Override
    public boolean exists() throws QueryException
    {
        if (record == null)
        {
            return false;
        }
        <#list entity.keyFields as f>
        <#if f.autoIncrement?? && f.autoIncrement>
        if(get${f.name?cap_first}() <= 0)
        {
            return false;
        }
        </#if>
        </#list>
        return new ${entity.name}QueryImpl(context)
                            <#list entity.keyFields as f >
                            .by${f.name?cap_first}(get${f.name?cap_first}())
                            </#list>
                            .count() > 0;
    }

    <#if entity.save>
    @Override
    </#if>
    <#if entity.save>public </#if>void save() throws QueryException, FieldException
    {
        List<${entity.name}Listener> listeners = this.context.getIocContext().findAll(${entity.name}Listener.class);
        for(${entity.name}Listener listener : listeners)
        {
            listener.beforeSave(this);
        }
        <#list entity.fields as f>
        <#list f.rules as r>
        if( !(${r.findRuleCondition(f)}) )
        {
            <#if r.message??>
            throw new FieldException("${f.name}", "${r.message}");
            <#else>
            throw new FieldException("${f.name}", "El campo ${f.name} de ${entity.name} es invalido.");
            </#if>
        }
        </#list>
        </#list>
        if(exists())
        {
            new ${entity.name}QueryImpl(context)
                        <#list entity.updatableFields as f >
                        .set${f.name?cap_first}(get${f.name?cap_first}())
                        </#list>
                        <#list entity.keyFields as f >
                        <#if !f.autoIncrement?? || !f.autoIncrement>
                        .by${f.name?cap_first}(getOriginal${f.name?cap_first}())
                        <#else>
                        .by${f.name?cap_first}(get${f.name?cap_first}())
                        </#if>
                        </#list>
                        .updateAll();
        }
        else
        {
            Object[] resultKeys = new ${entity.name}QueryImpl(context)
                        <#list entity.updatableFields as f >
                        .set${f.name?cap_first}(get${f.name?cap_first}())
                        </#list>
                        .insert();
            <#list entity.incrementFields as f>
            ${f.name} = context.cast(${f.javaType}.class, resultKeys[${f_index}]);
            </#list>
        }
        <#list entity.updatableFields as f >
        setOriginal${f.name?cap_first}(get${f.name?cap_first}());
        </#list>
        for(${entity.name}Listener listener : listeners)
        {
            listener.afterSave(this);
        }
    }

    <#if entity.delete>
    @Override
    </#if>
    <#if entity.delete>public </#if>void delete() throws QueryException
    {
        if(exists())
        {
            List<${entity.name}Listener> listeners = this.context.getIocContext().findAll(${entity.name}Listener.class);
            for(${entity.name}Listener listener : listeners)
            {
                listener.beforeDelete(this);
            }
            new ${entity.name}QueryImpl(context)
                        <#list entity.keyFields as f >
                        <#if !f.autoIncrement?? || !f.autoIncrement>
                        .by${f.name?cap_first}(getOriginal${f.name?cap_first}())
                        <#else>
                        .by${f.name?cap_first}(get${f.name?cap_first}())
                        </#if>
                        </#list>
                        .deleteAll();
            for(${entity.name}Listener listener : listeners)
            {
                listener.afterDelete(this);
            }
        }
    }

    <#-- Filtros relacionados -->
    <#list entity.relations as relation>
    <#list relation.relatedFilters as relatedFilter>
    <#if relation.returnsMany>
    <#assign relatedParams = getEntityRelatedFilterParams(relatedFilter, entity) />
    <#assign filterParams = showFilterParamsDefinition(relatedParams) />
    <#assign filterParamsVars = showFilterParamsDefinitionVars(relatedParams) />
    public ${relatedFilter.entity.name}QueryImpl filter${relatedFilter.entity.name}${relatedFilter.name!?cap_first}(${filterParams}) throws QueryException
    {
        return new ${relatedFilter.entity.name}QueryImpl(context)
                    <#list relatedFilter.params as param>
                    <#if param.isRelationFilterParam()>
                    <#if param.isRelatedWith(entity.name)>
                    .by${relation.name?cap_first}(this)
                    <#else>
                    .by${param.name?cap_first}(${param.paramName})
                    </#if>
                    <#else>
                    ${param.filterCondition}
                    </#if>
                    </#list>
        ;
    }

    <#if relatedFilter.count?? && relatedFilter.count>
    @Override
    public int count${relatedFilter.entity.name}${relatedFilter.name!?cap_first}(${filterParams}) throws QueryException
    {
        return filter${relatedFilter.entity.name}${relatedFilter.name!?cap_first}(${filterParamsVars}).count();
    }

    </#if>
    <#if relatedFilter.delete?? && relatedFilter.delete>
    @Override
    public void delete${relatedFilter.entity.name}${relatedFilter.name!?cap_first}(${filterParams}) throws QueryException
    {
        filter${relatedFilter.entity.name}${relatedFilter.name!?cap_first}(${filterParamsVars}).deleteAll();
    }

    </#if>
    <#if relatedFilter.exist?? && relatedFilter.exist>
    @Override
    public boolean exist${relatedFilter.entity.name}${relatedFilter.name!?cap_first}(${filterParams}) throws QueryException
    {
        return filter${relatedFilter.entity.name}${relatedFilter.name!?cap_first}(${filterParamsVars}).exists();
    }

    </#if>
    <#if relatedFilter.find?? && relatedFilter.find>
    @Override
    public List<${relatedFilter.entity.name?cap_first}> find${relatedFilter.entity.name}${relatedFilter.name!?cap_first}(${filterParams}) throws QueryException
    {
        return filter${relatedFilter.entity.name}${relatedFilter.name!?cap_first}(${filterParamsVars}).find();
    }

    </#if>
    </#if>
    </#list>
    </#list>
    <#list entity.relations as relation>
    <#if relation.returnsOne>
    @Override
    public ${relation.relatedEntity.name} get${relation.relatedName}() throws QueryException
    {
        if(this.${relation.relatedName?uncap_first} == null)
        {
            this.${relation.relatedName?uncap_first} = new ${relation.relatedEntity.name}QueryImpl(context)
                    .by${relation.name?cap_first}(this)
                    .findOne();
        }
        return this.${relation.relatedName?uncap_first};
    }

    @Override
    public void set${relation.relatedName}(${relation.relatedEntity.name} ${relation.relatedEntity.name?uncap_first})
    {
        this.${relation.relatedEntity.name?uncap_first} = ${relation.relatedEntity.name?uncap_first};
        <#list relation.fields as relationField>
        <#if (relationField.field.autoIncrement)!"false" == "false">
        set${relationField.field.name?cap_first}(${relation.relatedEntity.name?uncap_first}.get${relationField.relatedField.name?cap_first}());
        </#if>
        </#list>
    }

    </#if>
    <#list relation.relatedConstructors as construct>
    <#if relation.returnsMany>
    @Override
    public ${relation.relatedEntity.name} add${relation.relatedName}(${construct.findRelationParamsDeclaration(relation)}) throws QueryException, FieldException
    {
        ${relation.relatedEntity.name}Impl result = new ${relation.relatedEntity.name}Impl(context);
        result.set${relation.name}(this);
        <#list construct.findRelationParams(relation) as param>
        <#if param.relatedEntity??>
        result.set${param.relatedEntity.name?cap_first}(${param.relatedEntity.name?uncap_first});
        <#else>
        result.set${param.fieldObject.name?cap_first}(${param.fieldObject.name});
        </#if>
        </#list>
        result.save();
        return result;
    }

    </#if>
    </#list>
    <#if (relation.returnsMany)!"false">
        <#if relation.relatedEntity.isQueryable>
    @Override
        </#if>
    public ${relation.relatedEntity.name}Query<#if !relation.relatedEntity.isQueryable>Impl</#if> query${relation.relatedName?cap_first}()
    {
        return new ${relation.relatedEntity.name}QueryImpl(context);
    }

    </#if>
    </#list>
    <#list entity.lifeCycle.states as state>
    @Override
    public boolean is${state.name?cap_first}()
    {
        <#list state.fields as field>
        if(get${field.name!?cap_first}() != ${field.value})
        {
            return false;
        }

        </#list>
        return true;
    }

    </#list>
    <#list entity.lifeCycle.transitions as transition>
    @Override
    public void ${transition.name}() throws QueryException, StateException, FieldException
    {
        if(!is${transition.from?cap_first}())
        {
            throw new StateException("${transition.from}", "La entidad esta en un status invalido.");
        }
        <#list transition.toState.fields as field>
        set${field.name!?cap_first}(${field.value});
        </#list>
        save();
    }

    </#list>
    <#list entity.interfaces as interfaceName>
    <#assign interface = mojo.getJavaInterfaceData(interfaceName) />
    <@renderInterfaceImplMethods entity=entity interfaceMetaData=interface />
    </#list>
}