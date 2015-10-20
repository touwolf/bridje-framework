<#include "common.ftl">

package ${dm.package}; 

import javax.sql.DataSource;
import com.touwolf.bridje.orm.*;
import com.touwolf.bridje.orm.exceptions.*;
import java.util.List;
import java.util.Date;

public interface ${dm.name}
{
    /**
     * Crea una nueva instancia ${dm.name}
     * 
     * @param ds El origen de datos al cual se va a conectar este modelo.
     * @return Una instancia de ${dm.name} con un DataSource nuevo
     */
    public ${dm.name} createNew${dm.name}(DataSource ds);

    /**
     * Instala el modelo ${dm.name} en la base de datos 
     */
    public void fix();

    <#list dm.entitys as e>
    /**
     *
     * @return
     */
    public ${e.name}Query query${e.name?cap_first}();

    </#list>
    <#list dm.entitys as e>       
    <#list e.constructors as construct>
    <#assign paramFields=construct.fields />
    /**
     <#list paramFields as p>
     * @param ${p.name}
     </#list>
     * @return
     * @throws com.touwolf.bridje.orm.exceptions.QueryException
     * @throws com.touwolf.bridje.orm.exceptions.FieldException
     */
    <#assign existParam = false />
    <#list paramFields as pf>
    <#assign existParam = true />
    </#list>
    <#assign concatParams = "" />
    <#assign relEntitysParam = [] />
    <#assign existRelation = false />
    <#list construct.relatedEntitys as relEntity> 
    <#assign relEntitysParam = relEntitysParam + [relEntity.name + " " + relEntity.name?uncap_first] />
    <#assign existRelation = true />
    </#list>
    <#if existParam && existRelation>
    <#assign concatParams = ", " />
    </#if>
    public ${e.name} add${e.name?cap_first}(${showParamsDefinition(paramFields!)?join(", ")}${concatParams}${relEntitysParam?join(", ")}) throws QueryException, FieldException;

    </#list>
    <#list e.filters as efilter> 
    <#assign funcName = e.name?cap_first + efilter.name!?cap_first />
    <#assign filterParams = showFilterParamsDefinition(efilter.params!) /> 
    <#if efilter.count?? && efilter.count> 
    /**
     <#list efilter.params as p>
     <#list p.paramsPrefix as prefix>
     * @param ${prefix}<#if prefix != "">${p.field?cap_first}<#else>${p.field}</#if>
     </#list>
     </#list>
     * @return
     * @throws com.touwolf.bridje.orm.exceptions.QueryException
     */
    public int count${funcName}(${filterParams}) throws QueryException;

    </#if>
    <#if efilter.delete?? && efilter.delete> 
    /**
     <#list efilter.params as p>
     <#list p.paramsPrefix as prefix>
     * @param ${prefix}<#if prefix != "">${p.field?cap_first}<#else>${p.field}</#if>
     </#list>
     </#list>
     * @throws com.touwolf.bridje.orm.exceptions.QueryException
     */
    public void delete${funcName}(${filterParams}) throws QueryException;

    </#if> 
    <#if efilter.exist?? && efilter.exist>
    /**
     <#list efilter.params as p>
     <#list p.paramsPrefix as prefix>
     * @param ${prefix}<#if prefix != "">${p.field?cap_first}<#else>${p.field}</#if>
     </#list>
     </#list>
     * @return
     * @throws com.touwolf.bridje.orm.exceptions.QueryException
     */
    public boolean exist${funcName}(${filterParams}) throws QueryException;

    </#if> 
    <#if efilter.find?? && efilter.find>
    /**
     <#list efilter.params as p>
     <#list p.paramsPrefix as prefix>
     * @param ${prefix}<#if prefix != "">${p.field?cap_first}<#else>${p.field}</#if>
     </#list>
     </#list>
     * @return
     * @throws com.touwolf.bridje.orm.exceptions.QueryException
     */
    public List<${e.name?cap_first}> find${funcName}(${filterParams}) throws QueryException;

    </#if>
    </#list>  
    </#list>
}