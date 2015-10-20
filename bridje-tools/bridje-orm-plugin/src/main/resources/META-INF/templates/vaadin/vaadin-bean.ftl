<#import "vaadin-common.ftl" as common>
<#import "../orm/orm-common.ftl" as orm>
<#assign tableName = mojo.getTableName(entity) />
<#assign containerName = mojo.getContainerName(entity) />
<#assign beanName = mojo.getBeanName(entity) />
<#assign entityName = entity.name />
<#assign entityVariableName = entityName?lower_case + "Entity" />
<#assign entityRelations = common.getReturnsOneRelations(entity) />

package ${dm.package}.vaadin;

import com.touwolf.bridje.ioc.IocContext;
import com.touwolf.bridje.orm.exceptions.QueryException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.Serializable;
<@common.renderOrmImports entity = entity />

/**
 * <code>Bean</code> de {@link ${entityName}}
 */
public final class ${beanName} implements Serializable
{
    private static final Logger LOG = Logger.getLogger(${beanName}.class.getName());

    private final IocContext context;

<#list entity.fields as field>
    <#assign fieldName = common.getVariableName(field) />
    <#assign fieldType = field.javaType />
    private ${fieldType} ${fieldName};

</#list>
<#list entityRelations as relation>
    private ${mojo.getBeanName(relation.relatedEntity)} ${common.getRelatedEntityVariableName(relation)};

</#list>
    /**
    * Crea el <b>${beanName}</b> a partir de la entidad correspondiente
    * @param ${entityVariableName} Instancia de la entidad {@link ${entityName}}
    * @param context Contexto actual
    */
    public ${beanName}(${entityName} ${entityVariableName}, IocContext context)
    {
        this.context = context; 
<#list entity.fields as field>
        <#assign getterMethod = orm.getFieldGetterStm(field) />
        <#assign setterMethod = common.getSetterMethodName(field) />
        ${setterMethod}(${entityVariableName}.${getterMethod});
</#list>
    <#if entityRelations?has_content>
        try
        {
<#list entityRelations as relation>
            ${common.getRelationSetterMethodName(relation)}(${entityVariableName}.${common.getRelationGetterMethodName(relation)}());
</#list>
        }
        catch (QueryException ex)
        {
            LOG.log(Level.WARNING, "Error agregando los beans de los campos relativos", ex);
        }
    </#if>
    }

    <#list entity.fields as field>
        <#assign fieldType = field.javaType />
        <#assign fieldName = common.getVariableName(field) />
        <#assign getterMethod = orm.getRegularFieldGetterStm(field) />
        <#assign setterMethod = orm.getRegularFieldSetterStm(field, fieldType + " " + fieldName) />

    public ${fieldType} ${getterMethod}
    {
        return ${fieldName};
    }

    public void ${setterMethod}
    {
        this.${fieldName} = ${fieldName};
    }

    </#list>
    <#list entityRelations as relation>
        <#assign fieldName = common.getRelatedEntityVariableName(relation) />
        <#assign getterMethod = common.getRelationGetterMethodName(relation) />
        <#assign setterMethod = common.getRelationSetterMethodName(relation) />
        <#assign fieldBean = mojo.getBeanName(relation.relatedEntity) />
        <#assign fieldType = relation.relatedEntity.name?cap_first />
    public ${fieldBean} ${getterMethod}()
    {
        return ${fieldName};
    }

    public void ${setterMethod}(${fieldType} ${fieldName})
    {
        ${setterMethod}(new ${fieldBean}(${fieldName}, context));
    }

    public void ${setterMethod}(${fieldBean} ${fieldName})
    {
        this.${fieldName} = ${fieldName};
    }

    </#list>
}
