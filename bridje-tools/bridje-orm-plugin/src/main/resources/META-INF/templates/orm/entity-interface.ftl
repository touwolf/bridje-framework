<#include "orm-common.ftl">

package ${dm.package};

import com.touwolf.bridje.orm.*;
import com.touwolf.bridje.orm.exceptions.*;
import java.util.List;
import java.util.Date;
<@renderExtraImports entity = entity />

/**
*  ${entity.description}
*/
public interface ${entity.name} extends Entity<#if entity.save>, SaveableEntity</#if><#if entity.refresh>, RefreshableEntity</#if><#if entity.rollback>, RollbackableEntity</#if><#if entity.delete>, DeletableEntity</#if>${getEntityInterfacesDeclaration(entity)}
{
<@renderEntityKeyFieldsFunctionsDefinitions entity = entity />
<@renderFieldsFunctionsDefinitions fields = entity.nonKeyFields />
<#-- Filtros relacionados -->
<#list entity.relations as relation>
<#list relation.relatedFilters as relatedFilter>
    <#assign funcName = relatedFilter.entity.name + relatedFilter.name!?cap_first />
    <#assign relatedParams = getEntityRelatedFilterParams(relatedFilter, entity) />
    <#assign filterParams = showFilterParamsDefinition(relatedParams) />
    <#assign relatedEntity = relatedFilter.entity />
    <#if relatedFilter.count?? && relatedFilter.count>
    /**
     * Devuelve la cantidad de elementos correspondientes de tipo {@link ${relatedEntity.name}}
        <#list relatedParams as param>
            <#list param.paramsPrefix as prefix>
     * @param ${prefix}<#if prefix != "">${param.paramName?cap_first}<#else>${param.paramName}</#if> ${param.description!("parametro requerido")}
            </#list>
        </#list>
     * @return cantidad de elementos encontrados >= 0
     * @throws com.touwolf.bridje.orm.exceptions.QueryException Errores de consulta a la base de datos
     */
    public int count${funcName}(${filterParams}) throws QueryException;

    </#if>
    <#if relatedFilter.delete?? && relatedFilter.delete>
    /**
     * Elimina elementos de tipo {@link ${relatedEntity.name}}
        <#list relatedParams as param>
            <#list param.paramsPrefix as prefix>
     * @param ${prefix}<#if prefix != "">${param.paramName?cap_first}<#else>${param.paramName}</#if> ${param.description!("parametro requerido")}
            </#list>
        </#list>
     * @throws com.touwolf.bridje.orm.exceptions.QueryException Errores de consulta a la base de datos
     */
    public void delete${funcName}(${filterParams}) throws QueryException;

    </#if>
    <#if relatedFilter.exist?? && relatedFilter.exist>
    /**
     * Confirma la existencia de elementos de tipo {@link ${relatedEntity.name}}
        <#list relatedParams as param>
            <#list param.paramsPrefix as prefix>
     * @param ${prefix}<#if prefix != "">${param.paramName?cap_first}<#else>${param.paramName}</#if> ${param.description!("parametro requerido")}
            </#list>
        </#list>
     * @return true | false si se encontro el elemento cuyos parametros fueron pasados
     * @throws com.touwolf.bridje.orm.exceptions.QueryException Errores de consulta a la base de datos
     */
    public boolean exist${funcName}(${filterParams}) throws QueryException;

    </#if>
    <#if relatedFilter.find?? && relatedFilter.find>
    /**
     * Busca elementos de tipo {@link ${relatedEntity.name}}
        <#list relatedParams as param>
            <#list param.paramsPrefix as prefix>
     * @param ${prefix}<#if prefix != "">${param.paramName?cap_first}<#else>${param.paramName}</#if> ${param.description!("parametro requerido")}
            </#list>
        </#list>
     * @return Listado de {@link ${relatedEntity.name?cap_first}} encontrados
     * @throws com.touwolf.bridje.orm.exceptions.QueryException Errores de consulta a la base de datos
     */
    public <#if relation.returnsMany>List<${relatedEntity.name?cap_first}><#else>${relatedEntity.name?cap_first}</#if> find${funcName}(${filterParams}) throws QueryException;

    </#if>
</#list>

</#list>
<#list entity.relations as relation>
    <#if !relation.returnsMany>
    /**
     * Devuelve la entidad relacionada "${relation.relatedName}"
     * @return Instancia de {@link ${relation.relatedEntity.name}}
     * @throws com.touwolf.bridje.orm.exceptions.QueryException
     */
    public ${relation.relatedEntity.name} get${relation.relatedName}() throws QueryException;

    /**
    * Establece el valor del {@link ${relation.relatedEntity.name}} correspondiente<#if relation.relatedName?has_content>, en la relacion <b>${relation.relatedName}</b></#if>
    * @param ${relation.relatedEntity.name?lower_case} Nuevo valor del {@link ${relation.relatedEntity.name}} correspondiente
    */
    public void set${relation.relatedName}(${relation.relatedEntity.name} ${relation.relatedEntity.name?lower_case});

    </#if>
    <#list relation.relatedConstructors as construct>
    <#if relation.returnsMany>
    /**
     *
    <#list construct.params! as param>
    <#if !(param.relatedEntity?? && param.relatedEntity.name == relation.entity.name)>
     * @param ${param.name?uncap_first} ${param.description!("parametro requerido")}
    </#if>
    </#list>
     * @return
     * @throws com.touwolf.bridje.orm.exceptions.QueryException
     * @throws com.touwolf.bridje.orm.exceptions.FieldException
     */
    public ${relation.relatedEntity.name} add${relation.relatedName}(${construct.findRelationParamsDeclaration(relation)}) throws QueryException, FieldException;

    </#if>
    </#list>
</#list>
<#list entity.relations as relation>
    <#if relation.returnsMany && relation.relatedEntity.isQueryable>

    /**
     * Permite hacer consultas a la entidad relacionada ${relation.relatedEntity.name}
     * @return Instancia de {@link  ${relation.relatedEntity.name}Query}, no <code>null</code>
     */
    public ${relation.relatedEntity.name}Query query${relation.relatedName?cap_first}();

    </#if>
</#list>
<#list entity.lifeCycle.states as state>
    /**
     *
     * @return
     */
    public boolean is${state.name?cap_first}();

</#list>
<#list entity.lifeCycle.transitions as transition>
    /**
     *
     * @throws com.touwolf.bridje.orm.exceptions.QueryException
     * @throws com.touwolf.bridje.orm.exceptions.StateException
     * @throws com.touwolf.bridje.orm.exceptions.FieldException
     */
    public void ${transition.name}() throws QueryException, StateException, FieldException;

</#list>
}