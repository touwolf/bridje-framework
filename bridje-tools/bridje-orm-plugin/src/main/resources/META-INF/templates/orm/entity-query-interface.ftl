<#include "orm-common.ftl">

package ${dm.package};

import com.touwolf.bridje.orm.EntityQuery;
import java.util.Date;

/**
 * 
 */
public interface ${entity.name}Query extends EntityQuery<${entity.name}>
{
    <#list entity.fields as f>
    <#if f.visible>
    /**
     * @param values
     * @return 
     */
    ${entity.name}Query by${f.name?cap_first}(${f.objectJavaType}... values);

    /**
     * @param values
     * @return 
     */
    ${entity.name}Query by${f.name?cap_first}Not(${f.objectJavaType}... values);

    /**
     * @param values
     * @return 
     */
    ${entity.name}Query or${f.name?cap_first}(${f.objectJavaType}... values);

    /**
     * @param values
     * @return 
     */
    ${entity.name}Query or${f.name?cap_first}Not(${f.objectJavaType}... values);

    <#if !(f.autoIncrement?? && f.autoIncrement)>
    /**
     * @param value
     * @return
     */
    ${entity.name}Query set${f.name?cap_first}(${f.javaType} value);

    </#if>
    <#if f.isNumeric || f.isDate>
    /**
     * @param values
     * @return
     */
    ${entity.name}Query by${f.name?cap_first}Ge(${f.objectJavaType}... values);

    /**
     * @param values
     * @return
     */
    ${entity.name}Query by${f.name?cap_first}NotGe(${f.objectJavaType}... values);

    /**
     * @param values
     * @return
     */
    ${entity.name}Query or${f.name?cap_first}Ge(${f.objectJavaType}... values);

    /**
     * @param values
     * @return
     */
    ${entity.name}Query or${f.name?cap_first}NotGe(${f.objectJavaType}... values);

    /**
     * @param values
     * @return
     */
    ${entity.name}Query by${f.name?cap_first}Gt(${f.objectJavaType}... values);

    /**
     * @param values
     * @return
     */
    ${entity.name}Query by${f.name?cap_first}NotGt(${f.objectJavaType}... values);

    /**
     * @param values
     * @return
     */
    ${entity.name}Query or${f.name?cap_first}Gt(${f.objectJavaType}... values);

    /**
     * @param values
     * @return
     */
    ${entity.name}Query or${f.name?cap_first}NotGt(${f.objectJavaType}... values);

    /**
     * @param values
     * @return
     */
    ${entity.name}Query by${f.name?cap_first}Le(${f.objectJavaType}... values);

    /**
     * @param values
     * @return
     */
    ${entity.name}Query by${f.name?cap_first}NotLe(${f.objectJavaType}... values);

    /**
     * @param values
     * @return
     */
    ${entity.name}Query or${f.name?cap_first}Le(${f.objectJavaType}... values);

    /**
     * @param values
     * @return
     */
    ${entity.name}Query or${f.name?cap_first}NotLe(${f.objectJavaType}... values);

    /**
     * @param values
     * @return
     */
    ${entity.name}Query by${f.name?cap_first}Lt(${f.objectJavaType}... values);

    /**
     * @param values
     * @return
     */
    ${entity.name}Query by${f.name?cap_first}NotLt(${f.objectJavaType}... values);

    /**
     * @param values
     * @return
     */
    ${entity.name}Query or${f.name?cap_first}Lt(${f.objectJavaType}... values);

    /**
     * @param values
     * @return
     */
    ${entity.name}Query or${f.name?cap_first}NotLt(${f.objectJavaType}... values);

    </#if>
    <#if f.javaType == "String">
    /**
     * @param values
     * @return
     */
    ${entity.name}Query by${f.name?cap_first}Like(${f.objectJavaType}... values);

    /**
     * @param values
     * @return
     */
    ${entity.name}Query by${f.name?cap_first}NotLike(${f.objectJavaType}... values);

    /**
     * @param values
     * @return
     */
    ${entity.name}Query or${f.name?cap_first}Like(${f.objectJavaType}... values);

    /**
     * @param values
     * @return
     */
    ${entity.name}Query or${f.name?cap_first}NotLike(${f.objectJavaType}... values);

    </#if>
    </#if>
    </#list>
    <#list entity.relations as relation>
    /**
     * 
     * @return
     */
    ${relation.relatedEntity.name}Query join${relation.relatedName?cap_first}();

    /**
     * 
     * @return
     */
    ${entity.name}Query by${relation.relatedName?cap_first}(${relation.relatedEntity.name} ${relation.relatedName?uncap_first});

    /**
    *
    * @return
    */
    ${entity.name}Query set${relation.relatedName?cap_first}(${relation.relatedEntity.name} ${relation.relatedName?uncap_first});
    </#list>
}