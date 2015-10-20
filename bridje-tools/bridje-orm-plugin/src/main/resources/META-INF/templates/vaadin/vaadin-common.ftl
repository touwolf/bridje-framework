
<#assign defaultTitleFields = ["description", "name"] />

<#-- Nombre de variable que contendra contenedor de variables multiples -->
<#assign EntityKeyVariableName = "key" />

<#function getBeanCaptionFunction beanName>
    <#return "get" + beanName + "Caption" />
</#function>

<#-- Devuelve titulo de una columna para una entidad -->
<#function getColumnTitleByColumn entity column>
    <#assign title = column.title!"" />
    <#if title = "">
        <#assign existenField = entity.findFieldByName(column.field)!"" />
        <#if existenField!="">
            <#assign title = existenField.name />
        </#if>
    </#if>
    <#return title />
</#function>

<#-- Devuelve titulo de una columna para una entidad -->
<#function getColumnTitleByField table entity field>
    <#assign title = field.title />
    <#assign column = table.findColumnByFieldName(field.name)!"" />
    <#if column != "">
        <#assign title = getColumnTitleByColumn(entity, column) />
    </#if>
    <#return title />
</#function>

<#-- Devuelve un metodo de agregar un campo a un componente del UI -->
<#function getAddMethodforField field>
    <#return "addColumn" + field.name?cap_first />
</#function>

<#-- Devuelve un metodo de quitar un campo a un componente del UI -->
<#function getRemoveMethodforField field>
    <#return "removeColumn" + field.name?cap_first />
</#function>

<#-- Devuelve un metodo de agregar un campo relacionado dentro de los selectores de campos externos -->
<#function getNestedFileSelectorGetterMethodName name>
    <#return "get" + name?cap_first />
</#function>

<#-- Devuelve el texto con que identificara el usuario un campo -->
<#function getFieldTitle field returnNull=false>
    <#if field?has_content && field.description?has_content >
        <#return field.description />
    </#if>
    <#if !returnNull>
        <#return field.name />
    </#if>
    <#return "" />
</#function>

<#-- Devuelve un listado de notaciones a ponerle un campo de un Bean, para que vaadin pueda validar el formulario correspondiente-->
<#function findBeanFieldNotations field>
    <#assign notations = [] />
    <#if field.required>
        <#assign notations = notations + ["@NotNull"] />
    </#if>
    <#if field.getObjectJavaType() = "String">
    <#--assign strSizeRule = findStringSizeNotation(field) /-->
        <#if strSizeRule?has_content>
            <#assign notations = notations + [strSizeRule] />
        </#if>
    <#elseif field.isNumeric>
        <#list field.rules as fieldRule>
        ${LOG.info("Generando la regla " + fieldRule.ruleName)}
            <#switch fieldRule.ruleName>
                <#case "max">
                <#case "min">
                    <#assign notations = notations + [getNumericNotation(fieldRule.ruleName,fieldRule.value,fieldRule.message)] />
                    <#break>
                <#case "between">
                    <#assign notations = notations + [getNumericNotation("min",fieldRule.from,fieldRule.message), getNumericNotation("max",fieldRule.to,fieldRule.message)] />
                    <#break>
                <#default>
            </#switch>
        </#list>
    </#if>
    <#return notations />
</#function>

<#-- Notacion para dimension de cadenas -->
<#function findStringSizeNotation field>
    <#if field.findRuleByName??>
        <#assign attributes = [] />
        <#assign ruleBetween = field.findRuleByName("between") />
        <#if ruleBetween?has_content>
            <#assign attributes = attributes + ['min = "'+ field.from + '"'] />
            <#assign attributes = attributes + ['max = "'+ field.to + '"'] />
            <#assign attributes = attributes + ['message = "'+ ruleBetween.message + '"'] />
        <#else>
            <#assign minRule = field.findRuleByName("min") />
            <#if minRule?has_content>
                <#assign attributes = attributes + ['min = "'+ field.value + '"'] />
            </#if>
            <#assign maxRule = field.findRuleByName("max")/>
            <#if maxRule?has_content>
                <#assign attributes = attributes + ['max = "'+ field.value + '"'] />
            </#if>
        </#if>
        <#if attributes?size > 0 >
            <#return "@Size("+ attributes?join(",") +")" />
        </#if>
    </#if>
    <#return ""/>
</#function>

<#-- Notacion para valores numericos, contiene un valor y un mensaje -->
<#function getNumericNotation type value message="">
    <#assign attributes = [] />
    <#assign valuePrefix = "" />
    <#if message?has_content>
        <#assign valuePrefix = "value = " />
    </#if>
    <#assign attributes = attributes + [valuePrefix + value] />
    <#assign attributes = attributes + ['message = "'+ message + '"'] />
    <#return "@" + type?cap_first + "(" + attributes?join(",") + ")" />
</#function>

<#-- Devuelve relaciones que devuelven un elemento: necesario para variables del Bean -->
<#function getReturnsOneRelations entity>
    <#assign result = [] />
    <#list entity.relations as relation>
        <#if relation.returnsOne>
            <#assign result = result + [relation] />
        </#if>
    </#list>
    <#return result />
</#function>

<#-- De una relacion relativa a una entidad, devuelve la variable de dicha entidad que interviene -->
<#function getEntityRelatedName entityRelation>
    <#list entityRelation.fields as relatedField>
        <#assign fieldTitle = getFieldTitle((relatedField.field)!"", true) />
        <#if fieldTitle?has_content>
            <#return fieldTitle/>
        </#if>
    </#list>
    <#return entityRelation.relatedName />
</#function>

<#-- Devuelve los campos que son propios del control y son editables en controles de Vaadin -->
<#function getControlEditableFields entity>
    <#assign resultFields = [] />
    <#assign relatedfieldsList = [] />
    <#list entity.relations as relation>
        <#list relation.fields as relatedFiled>
            <#assign relatedfieldsList = relatedfieldsList + [relatedFiled.field.name] />
        </#list>
    </#list>
    <#list entity.getVisibleFields() as field>
        <#if !relatedfieldsList?seq_contains(field.name)>
            <#assign resultFields = resultFields + [field] />
        </#if>
    </#list>
    <#return resultFields />
</#function>


<#-- Devuelve la funcion que puede utilizar una entidad para obtener su descripcion legible al usuario final-->
<#function getTitleFunctionName entity>
    <#if entity.title?has_content>
        <#return "get" + entity.title?cap_first />
    </#if>
<#-- Busco campos por defecto -->
    <#list defaultTitleFields as titleField>
        <#assign existentTitleField = entity.findFieldByName(titleField)!"" />
        <#if existentTitleField?has_content>
            <#return "get" + existentTitleField.name?cap_first />
        </#if>
    </#list>
    <#return "toString" />
</#function>

<#-- Devuelve el nombre de una variable que vincula una entidad a una entidad relacionada -->
<#function getRelatedEntityVariableName entityRelation>
    <#return entityRelation.relatedName?uncap_first />
</#function>

<#-- El nombre de la variable de un campo -->
<#function getVariableName field>
    <#return field.name?uncap_first />
</#function>

<#-- El getter de una variable -->
<#function getGetterMethodName field>
    <#return "get" + getVariableName(field)?cap_first />
</#function>

<#-- El setter de una variable -->
<#function getSetterMethodName field>
    <#return "set" + getVariableName(field)?cap_first />
</#function>

<#-- El getter de una relacion -->
<#function getRelationGetterMethodName relation>
    <#return "get" + getRelatedEntityVariableName(relation)?cap_first />
</#function>

<#-- El setter de una relacion -->
<#function getRelationSetterMethodName relation>
    <#return "set" + getRelatedEntityVariableName(relation)?cap_first />
</#function>

<#-- Imprime los imports necesarios cuando se manipula una entidad en particular -->
<#macro renderOrmImports entity showRelated=true showGenerated=true showDm=false>
    <#assign entityRelations = getReturnsOneRelations(entity) />
    <#assign package = entity.dataModel.package />
    <#assign dmName = entity.dataModel.name />
    <#if showDm>
import ${package}.${dmName};
    </#if>
import ${package}.${entity.name};
    <#if showRelated>
        <#list entityRelations as entityRelation>
import ${package}.${entityRelation.relatedEntity.name};
        </#list>
    </#if>
    <#assign otherClasses = {} />
    <#list entity.fields as field>
        <#if field.isGeneratedClass>
            <#if showGenerated>
                <#assign otherClasses = otherClasses + {field.type: package + "." + field.type} />
            </#if>
        <#elseif field.isDate>
            <#assign otherClasses = otherClasses + {"Date": "java.util.Date"} />
        </#if>
    </#list>
    <#list otherClasses?values as importDeclaration>
import ${importDeclaration};
    </#list>
</#macro>

<#-- Imprime funciones que sirven como representers de las funciones del container -->
<#macro renderContainerFunctions entity>
    <#assign entityName = entity.name?cap_first />
    <#assign beanName = mojo.getBeanName(entity) />
    /**
    * Agrega elementos obtenidos del Orm
    *
    * @param collection Listado de {@link ${entityName}}
    */
    public void add${entityName}List(Collection<? extends ${entityName}> collection)
    {
        getContainerDataSource().add${entityName}List(collection);
    }

    /**
    * Establece todos los elementos del dominio para {@link ${entityName}} que se mostraran en los controles
    *
    * @param collection Listado de {@link ${entityName}}
    */
    public void set${entityName}List(Collection<? extends ${entityName}> collection)
    {
        getContainerDataSource().set${entityName}List(collection);
    }

</#macro>