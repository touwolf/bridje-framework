<#include "orm-common.ftl">

package ${dm.package}; 

import javax.sql.DataSource;
import com.touwolf.bridje.orm.*;
import com.touwolf.bridje.orm.exceptions.*;
import java.util.List;
import java.util.Date;
<@renderExtraImports entity = dm />

public interface ${dm.name} ${getModelInterfacesDeclaration(dm)}
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
        <#if e.isQueryable!false>
    /**
    *
    * @return
    */
    public ${e.name}Query query${e.name?cap_first}();

        </#if>
    </#list>
    <#list dm.entitys as e>       
    <#list e.constructors as construct>
    /**
     <#list construct.params as p>
     * @param ${p.name?uncap_first}
     </#list>
     * @return
     * @throws com.touwolf.bridje.orm.exceptions.QueryException
     * @throws com.touwolf.bridje.orm.exceptions.FieldException
     */
    public ${e.name} add${construct.name?cap_first}(${construct.paramsDeclaration}) throws QueryException, FieldException;

    </#list>
    <#list e.filters as efilter> 
    <#assign funcName = e.name?cap_first + efilter.name!?cap_first />
    <#assign filterParams = showFilterParamsDefinition(efilter.params!) /> 
    <#if efilter.count?? && efilter.count> 
    /**
     <#list efilter.params as p>
     <#list p.paramsPrefix as prefix>
     * @param ${prefix}<#if prefix != "">${p.paramName?cap_first}<#else>${p.paramName}</#if>
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
     * @param ${prefix}<#if prefix != "">${p.paramName?cap_first}<#else>${p.paramName}</#if>
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
     * @param ${prefix}<#if prefix != "">${p.paramName?cap_first}<#else>${p.paramName}</#if>
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
     * @param ${prefix}<#if prefix != "">${p.paramName?cap_first}<#else>${p.paramName}</#if>
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