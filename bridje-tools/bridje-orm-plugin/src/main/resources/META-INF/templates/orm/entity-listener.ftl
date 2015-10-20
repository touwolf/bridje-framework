<#include "orm-common.ftl">

package ${dm.package};

import com.touwolf.bridje.orm.*;
import com.touwolf.bridje.orm.exceptions.*;
import java.util.List;

/**
 * Listener de los eventos de la entidad ${entity.name}.
 */
public interface ${entity.name}Listener 
{
    /**
     * Este metodo sera llamado antes de ser salvada la entidad entity.
     */
    void beforeSave(${entity.name} entity) throws QueryException, FieldException;

    /**
     * Este metodo sera llamado despues de ser salvada la entidad entity.
     */
    void afterSave(${entity.name} entity) throws QueryException, FieldException;

    /**
     * Este metodo sera llamado antes de eliminar la entidad entity.
     */
    void beforeDelete(${entity.name} entity) throws QueryException;

    /**
     * Este metodo sera llamado despues de eliminar la entidad entity.
     */
    void afterDelete(${entity.name} entity) throws QueryException;

    <#list entity.lifeCycle.transitions as transition>
    /**
     * Este metodo sera llamada antes de realizar la trancision ${transition.name} sobre la entidad entity.
     */
    void before${transition.name?cap_first}(${entity.name} entity) throws QueryException, StateException, FieldException;

    /**
     * Este metodo sera llamada despues de realizar la trancision ${transition.name} sobre la entidad entity.
     */
    void after${transition.name?cap_first}(${entity.name} entity) throws QueryException, StateException, FieldException;

    </#list>
}