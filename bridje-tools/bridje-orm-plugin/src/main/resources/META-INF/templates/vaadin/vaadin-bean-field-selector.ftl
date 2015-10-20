<#import "vaadin-common.ftl" as common>
<#assign tableName = mojo.getTableName(entity) />
<#assign containerName = mojo.getContainerName(entity) />
<#assign beanName = mojo.getBeanName(entity) />
<#assign FieldSelectorName = mojo.getFieldSelectorName(entity) />
<#assign entityName = entity.name />
<#assign editableFields = common.getControlEditableFields(entity) />
<#assign entityRelations = common.getReturnsOneRelations(entity) />

package ${dm.package}.vaadin;

import com.touwolf.bridje.vaadin.NestedFieldSelector;

/**
 * Generador de Querys para {@link ${beanName}}
 */
public final class ${FieldSelectorName}
{
    private final NestedFieldSelector NestedFieldSelector;
    
    private final String parentQueryString;

    /**
     * Crea una Query para campos que referencian elementos de tipo {@link ${entityName}}
     * @param parentVariableName Nombre del campo
     * @param NestedFieldSelector {@link com.touwolf.bridje.vaadin.NestedFieldSelector} encargado de agregar la query al componente
     */
    protected ${FieldSelectorName}(String parentVariableName, NestedFieldSelector NestedFieldSelector)
    {
        this.parentQueryString = parentVariableName;
        this.NestedFieldSelector = NestedFieldSelector;

    }

    /**
     * Devuelve el {@link NestedFieldSelector} creado por {@link RechargesTable#getNestedFieldSelector()}
     * @return instancia de {@link NestedFieldSelector}, no <code>null</code>
     */
    public NestedFieldSelector getNestedFieldSelector()
    {
        return NestedFieldSelector;
    }

<#list editableFields as field>
    <#assign fieldTitle = common.getFieldTitle(field) />
    <#assign addFieldFuncName = common.getAddMethodforField(field) />
    /**
     * Mostrara el campo "${field.name}" del {@link ${entityName}} correpondiente, identificado con un caption por defecto. Ej.: "${fieldTitle}"
     */
    public void ${addFieldFuncName}()
    {
        ${addFieldFuncName}("${fieldTitle}");
    }
    
    /**
     * Mostrara el campo "${field.name}" del {@link ${entityName}} correpondiente, identificado por el <code>caption</code> especificado
     * @param caption Texto con que el usuario identificara al campo
     */
    public void ${addFieldFuncName}(String caption)
    {
        String finalQueryString = NestedFieldSelector.joinFieldTokens(parentQueryString, "${field.name}");
        NestedFieldSelector.addComponentSelector(finalQueryString, caption);
    }

</#list>
<#list entityRelations as entityRelation>
    <#assign relatedEntityTitle = common.getEntityRelatedName(entityRelation) />
    <#assign relatedEntityName = entityRelation.relatedEntity.name />
    <#assign relatedFieldName = entityRelation.relatedName?lower_case />
    <#assign getterMethodName = common.getNestedFileSelectorGetterMethodName(relatedEntityName) />
    <#assign FieldSelectorName = mojo.getFieldSelectorName(entityRelation.relatedEntity) />
    /**
     * Realiza una consulta al {@link ${beanName}#${relatedFieldName}}(${relatedEntityTitle}) correspondiente para agregar alguno de sus campos
     * @return Instancia de {@link ${FieldSelectorName}}, no <code>null</code>
     */
    public ${FieldSelectorName} ${getterMethodName}()
    {
        String ${relatedEntityName}QueryString = NestedFieldSelector.joinFieldTokens(parentQueryString, "${relatedFieldName}");
        return new ${FieldSelectorName}(${relatedEntityName}QueryString, NestedFieldSelector);
    } 

</#list>
}