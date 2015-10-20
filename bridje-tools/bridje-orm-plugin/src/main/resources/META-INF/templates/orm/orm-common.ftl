<#include "relations.ftl">

<#-- Nombre de variable que contendra contenedor de variables multiples -->
<#assign EntityKeyVariableName = "key" />

<#function showParamsDefinition fields>
    <#assign reqfields = [] />
    <#list fields as f>
        <#assign reqfields = reqfields + [f.javaType + " " + f.name] />
    </#list>
    <#return reqfields />
</#function>

<#-- Devuelve si el campo dado es de tipo Enum -->
<#function isEnumType field>
    <#assign enumType = false />
    <#if !field.isNumeric && field.objectJavaType != "String" && field.objectJavaType != "Date" && field.objectJavaType != "Boolean">
        <#assign enumType = true />
    </#if>
    <#return enumType />
</#function>

<#-- Construye y devuelve un valor para el campo dado, el valor se contruye 
teniendo en cuenta el tipo de datos del campo, un contador y el tamaño maximo 
para el caso de los campos tipo String -->
<#function getParamValue field, countValue, stringLength>
    <#assign paramValue = "" />
    <#switch field.objectJavaType>
        <#case "String">
            <#if field.name?length &lt; stringLength>
                <#assign paramValue = "\"" + field.name + countValue + "\"" />
            <#else>
                <#assign paramValue = "\"" + field.name?substring(0, stringLength - 1) + countValue + "\"" />
            </#if>
            <#break>
        <#case "Long">
            <#assign paramValue = countValue + "l" />
            <#break>
        <#case "Float">
            <#assign paramValue = countValue + "f" />
            <#break>
        <#case "Double">
            <#assign paramValue = countValue + "d" />
            <#break>
        <#case "Date">
            <#assign paramValue = field.name + "Value" />
            <#break>
        <#case "Short">
        <#case "Byte">
        <#case "Integer">
            <#assign paramValue = countValue />
            <#break>
        <#case "Boolean">
            <#assign paramValue = "true" />
            <#break>
        <#default>
            <#assign paramValue = field.type + ".values()[" + countValue + "]" />
    </#switch>
    <#return paramValue />
</#function>

<#-- Construye y devuelve una declaración en donde se setea un valor al campo de
 una entidad, el valor que es seteado se construye teniendo en cuenta el tipo de
 datos del campo -->
<#function setFieldValue entityName, field, countValue, dateFieldName>
    <#assign result = "" />
    <#if field.javaType == "String">
        <#assign result = entityName + ".set" + field.name?cap_first + "(" + getParamValue(field, countValue, field.length) + ");" />
    </#if>
    <#if field.isNumeric>
        <#assign result = entityName + ".set" + field.name?cap_first + "(" + getParamValue(field, countValue, 0) + ");" />
    </#if>
    <#if field.isDate>
        <#assign result = entityName + ".set" + field.name?cap_first + "(" + dateFieldName + ");" />
    </#if>
    <#if common.isEnumType(field) || field.objectJavaType == "Boolean">
        <#assign result = entityName + ".set" + field.name?cap_first + "(" + getParamValue(field, countValue - 1, 0) + ");" />
    </#if>
    <#return result />
</#function>

<#-- Devuelve la declaración de los parámetros de una función correspondientes a los filtros declarados para esa entidad -->
<#function showFilterParamsDefinition params>
    <#assign result = "" />
    <#list params as filterParam>
        <#list filterParam.paramsPrefix as prefix>
            <#if result != "">
                <#assign result = result + ", " />
            </#if>
            <#if prefix != "">
                <#assign result = result + filterParam.paramType + " " + prefix + filterParam.paramName?cap_first />
            <#else>
                <#assign result = result + filterParam.paramType + " " + filterParam.paramName />
            </#if>
        </#list>
    </#list>
    <#return result />
</#function>

<#function showFilterParamsDefinitionVars params>
    <#assign result = "" />
    <#list params as filterParam>
        <#list filterParam.paramsPrefix as prefix>
            <#if result != "">
                <#assign result = result + ", " />
            </#if>
            <#if prefix != "">
                <#assign result = result + prefix + filterParam.paramName?cap_first />
            <#else>
                <#assign result = result + filterParam.paramName />
            </#if>
        </#list>
    </#list>
    <#return result />
</#function>

<#-- Devuelve los campos del listado links dado que pertenecen a la entidad dada -->
<#function createFieldsFromLink entity, links>
    <#assign fields = [] />
    <#list links as lfield>
        <#if lfield.name!="">
            <#assign field = entity.findFieldByName(lfield.name) />
            <#if !(field.autoIncrement?? && field.autoIncrement)>
                <#assign fields = fields + [field] />
            </#if>
        </#if>
    </#list>
    <#return fields />
</#function>

<#-- Retorna el valor dado con el formato correcto para el campo dado teniendo en cuenta su tipo de datos -->
<#function setValue field, value>
    <#assign prefix = "" />
    <#assign sufix = "" />
    <#switch field.javaType>
        <#case "String">
            <#assign prefix = "\"" />
            <#assign sufix = "\"" />
            <#break>
        <#case "Long">
            <#assign sufix = "l" />
            <#break>
        <#case "Float">
            <#assign sufix = "f" />
            <#break>
        <#case "Double">
            <#assign sufix = "d" />
            <#break>
        <#default>
    </#switch>
    <#return prefix + value + sufix />
</#function>

<#-- Devuelve la declaración de la realización de un casteo solo en caso de que el tipo de datos del campo dado sea numérico -->
<#function getFieldCast field>
    <#if field.isNumeric>
        <#return "(" + field.getObjectJavaType() + ")" />
    </#if>
    <#return "" />
</#function>

<#-- Devuelve la definicion de los parametros de los constructores: de campos y relaciones -->
<#function getConstructorDefinitions constructor>
    <#assign result = []/>
    <#list constructor.params as param>
        <#assign result = result + [param.paramDeclaration] />
    </#list>
    <#return result>
</#function>

<#-- Devuelve de un filtro relacionado a una entidad los parametros a utilizar, que no incluyan a la entidad especificada -->
<#function getEntityRelatedFilterParams filter relatedEntity>
    <#assign foundParams = [] />
    <#list filter.params as param>
        <#if !param.isRelatedWith(relatedEntity.name)>
            <#assign foundParams = foundParams + [param] />
        </#if>
    </#list>
    <#return foundParams />
</#function>

<#-- Devuelve el nombre de un filtro sin prefijo desde el punto de vista del domainModel -->
<#function getEntityFilterFunctionName filter>
    <#return filter.entity.name?cap_first + filter.name!?cap_first />
</#function>

<#-- Devuelve la instruccicn para establecer una variable, sin ";" al final -->
<#function getFieldSetterStm field statement fieldPrefix = "">
    <#assign selectedEntity = field.entity />
    <#assign resultStm = "" />
    <#assign result = "" />
    <!-- El if prefix!="original" se aplica luego que se decidiera que los originales no van en el Key -->
    <#if field.key && selectedEntity.isMultiKeys() && fieldPrefix!="original">
        <#assign result = "get" + EntityKeyVariableName?cap_first + "()." />
    </#if>
    <#return  result + "set" + fieldPrefix?cap_first + field.name?cap_first + "(" + statement + ")" />
</#function>

<#-- Devuelve la instruccicn estandard para establecer una variable, sin ";" al final -->
<#function getRegularFieldSetterStm field statement fieldPrefix = "">
    <#return "set" + fieldPrefix?cap_first + field.name?cap_first + "(" + statement + ")" />
</#function>

<#-- Devuelve la instruccicn para obtener una variable, sin ";" al final -->
<#function getFieldGetterStm field prefix="">
    <#assign selectedEntity = field.entity />
    <#assign resultStm = "" />
    <#assign result = "" />
    <!-- El if prefix!="original" se aplica luego que se decidiera que los originales no van en el Key -->
    <#if field.key && selectedEntity.isMultiKeys() && prefix!="original">
        <#assign result = "get" + EntityKeyVariableName?cap_first + "()." />
    </#if>
    <#return  result + "get" + prefix?cap_first + field.name?cap_first + "()" />
</#function>

<#-- Devuelve la instruccicn para obtener una variable en una query, sin ";" al final -->
<#function getRegularFieldGetterStm field prefix="">
    <#return "get" + prefix?cap_first + field.name?cap_first + "()" />
</#function>

<#-- Devuelve el SimpleName de una interface a partir de la ruta completa del mismo -->
<#function getInterfaceName interfacePath>
    <#assign interfacePathTokens = interfacePath?split(".") />
    <#return interfacePathTokens[interfacePathTokens?size - 1] />
</#function>

<#-- Devuelve el listado con los nombres de las interfaces que implementara una entidad o modelo  -->
<#function getEntityInterfacesNames entity>
    <#assign interfacesNameList = [] />
    <#if entity.interfaces?has_content>
        <#list entity.interfaces as interface>
            <#assign interfacesNameList = interfacesNameList + [getInterfaceName(interface)] />
        </#list>
        <#return interfacesNameList />
    </#if>
    <#return interfacesNameList />
</#function>

<#-- Devuelve la declaracion de las interfaces en la declaracion de una entidad o modelo, para ser usada en la declaracion de la clase junto a implements: separados por comas -->
<#function getModelInterfacesDeclaration model>
    <#assign modelInterfacesNames = getEntityInterfacesNames(model) />
    <#if modelInterfacesNames?has_content>
        <#return "extends " +  modelInterfacesNames?join(", ") />
    <#else>
        <#return "" />
    </#if>
</#function>

<#-- Devuelve la declaracion de las interfaces en la declaracion de una entidad o modelo, para ser usada en la declaracion de la clase junto a implements: separados por comas -->
<#function getEntityInterfacesDeclaration entity>
    <#assign entityInterfacesNames = getEntityInterfacesNames(entity) />
    <#if entityInterfacesNames?has_content>
        <#return ", " + entityInterfacesNames?join(", ") />
    <#else >
        <#return ""/>
    </#if>
</#function>

<#-- Devuelve la declaracion de las interfaces en la implementacion de una entidad o modelo, para ser usada en la declaracion de la clase junto a implements: separados por comas -->
<#function getEntityImplementationInterfacesDeclaration entity>
    <#assign entityInterfacesNames = [entity.name]  />
    <#return entityInterfacesNames?join(", ") />
</#function>

<#function getImplementedInterfaceInjectedVariableName implInterfaceName>
    <#return "injected" + implInterfaceName/>
</#function>

<#-- Imprime las definiciones de las variables -->
<#macro renderFieldsDefinitions fields prefix="">
    <#list fields as f>
        <#if prefix?has_content>
    private ${f.javaType} original${f.name?cap_first};
        <#else>
    private ${f.javaType} ${f.name};
        </#if>

    </#list>
</#macro>

<#-- Imprime las definiciones de las variables llaves -->
<#macro renderEntityKeyFieldsDefinitions entity>
    <#if entity.isMultiKeys()>
    ${entityKey} ${EntityKeyVariableName?uncap_first};

    <#else>
        <@renderFieldsDefinitions fields = entity.keyFields />
    </#if>
</#macro>


<#-- Imprime las necesarias para acceder a un listado de variables-->
<#macro renderFieldsFunctions fields override = true fieldPrefix = "">
    <#list fields as f>
        <#assign varName = f.name />
        <#if fieldPrefix?has_content>
            <#assign varName = fieldPrefix + f.name?cap_first />
        </#if>
        <#if f.visible && override>
    @Override
        </#if>
    public ${f.javaType} get${varName?cap_first}()
    {
        <#if f.javaType == "String" && f.trim?? && f.trim>
        if(this.${varName} != null)
        {
            return this.${varName}.trim();
        }
        <#else>
        return this.${varName};
        </#if>
    }

        <#if !f.readonly && override>
    @Override
        </#if>
    public void set${varName?cap_first}(${f.javaType} value)
    {
        <#if f.javaType == "Date" && f.sqlType == "DATE" >
        this.${varName} = truncate(value);
        <#else>
            <#if f.javaType == "String" && f.trim?? && f.trim>
        if(value != null)
        {
            this.${varName} = value.trim();
        }
            <#else>
        this.${varName} = value;
            </#if>
        </#if>
    }

    </#list>
</#macro>

<#-- Imprime las necesarias para acceder a las funciones llaves de una entidad-->
<#macro renderEntityKeyFieldsFunctions entity>
    <#assign variableName = EntityKeyVariableName?uncap_first />
    <#if entity.isMultiKeys()>
    @Override
    public ${entityKey} get${EntityKeyVariableName?cap_first}()
    {
        if(this.${variableName} == null)
        {
            this.${variableName} = new ${entityKey}();
        }
        return this.${variableName};
    }

    @Override
    public void set${EntityKeyVariableName?cap_first}(${entityKey} ${variableName})
    {
        this.${variableName} = ${variableName};
    }

    <#else>
        <@renderFieldsFunctions fields = entity.keyFields />
    </#if>
</#macro>

<#-- Imprime las funciones para establecer y obtener los valores de las llaves de una entidad -->
<#macro renderOriginalEntityKeyFieldsFunctions entity>
    <#list entity.nonKeys as f>
        <#assign variableName = "original" + f.name?cap_first />
    /**
    * Devuelve el valor que esta en base de datos de la llave <b>${f.name}</b>
    * @return devuelve un valor de tipo {@link ${f.javaType}}
    */
    public ${f.javaType} get${variableName?cap_first}()
    {
        <#if f.defaultValue??>
        return context.cast(${f.javaType}.class, record[${f_index}], ${f.getJavaCodeValue(f.defaultValue)});
        <#else>
        return context.cast(${f.javaType}.class, record[${f_index}]);
        </#if>
    }

    /**
    * Establece el valor en base de datos de la llave <b>${f.name}</b>
    * @param ${variableName} Nuevo valor de la llave <b>${f.name}</b> establecido en base de datos
    */
    public void set${variableName?cap_first}(${f.javaType} ${variableName})
    {
        record[${f_index}] = ${variableName};
    }

    </#list>
</#macro>

<#-- Imprime las necesarias para acceder a los campos de una entidad-->
<#macro renderFieldsFunctionsDefinitions fields>
    <#list fields as f>
        <#if f.visible>
    /**
    * Obtiene el campo ${f.name}, ${f.description}
    * @return Un objeto ${f.javaType} que representa el valor del campo ${f.name}
    */
    public ${f.javaType} get${f.name?cap_first}();

            <#if !f.readonly>
    /**
    * Establece el campo ${f.name}, ${f.description}
    * @param value Un objeto ${f.javaType} que representa el nuevo valor del campo ${f.name}
    */
    public void set${f.name?cap_first}(${f.javaType} value);

            </#if>
        </#if>
    </#list>
</#macro>

<#-- Imprime las necesarias para acceder a las funciones llaves de una entidad-->
<#macro renderEntityKeyFieldsFunctionsDefinitions entity>
    <#assign variableName = EntityKeyVariableName?uncap_first />
    <#if entity.isMultiKeys()>
    /**
    * Obtiene el contenedor de los campos llaves de ${entity.name}
    * @return Un objeto {@link ${entityKey}}
    */
    public ${entityKey} get${EntityKeyVariableName?cap_first}();

    /**
    * Establece el campo ${variableName}, que contiene las llaves de las llaves de ${entity.name}
    * @param ${variableName} Un objeto {@link ${entityKey}}
    */
    public void set${EntityKeyVariableName?cap_first}(${entityKey} ${variableName});

    <#else>
        <@renderFieldsFunctionsDefinitions fields = entity.keyFields />
    </#if>
</#macro>

<#-- Imprime los imports necesarios cuando se manipula una entidad en particular -->
<#macro renderExtraImports entity>
    <#if entity.interfaces?has_content>
        <#assign interfacesImports = [] />
        <#list entity.interfaces as interfaceName>
import ${interfaceName};
            <#assign cu = mojo.getJavaInterfaceData(interfaceName) />
            <#if cu?has_content>
                <#list cu.importDeclarations as importDeclaration>
                    <#if !interfacesImports?seq_contains(importDeclaration)>
                        <#assign interfacesImports = interfacesImports + [importDeclaration] />
                    </#if>
                </#list>
            </#if>
        </#list>
        <#list interfacesImports as interfaceImportDeclaration>
${interfaceImportDeclaration}
        </#list>
    </#if>
</#macro>

<#function getCUParamsDefinitionList method>
    <#assign parameterList = [] />
    <#list method.parameters![] as parameter>
        <#assign parameterList = parameterList + [parameter.type + " " + parameter.id.name] />
    </#list>
    <#return parameterList />
</#function>

<#function getCUParamsValuesList method>
    <#assign parameterList = [] />
    <#list method.parameters![] as parameter>
        <#assign parameterList = parameterList + [ parameter.id.name] />
    </#list>
    <#return parameterList />
</#function>

<#-- Imprime la declaracion de los metodos de la implementacion de una interface -->
<#macro renderInterfaceImplMethodsDeclaration methods>
    <#list methods as method>
        <#assign paramsDeclaration =
        (method)?join(", ") />
    public ${method.type} ${method.name}(${paramsDeclaration});

    </#list>

</#macro>

<#-- Imprime la implementacion de los metodos de la implementacion de una interface -->
<#macro renderInterfaceImplMethods entity interfaceMetaData>
    <#list interfaceMetaData.methods as method>
        <#assign paramsDeclaration = getCUParamsDefinitionList(method)?join(", ") />
        <#assign paramsValues = ["this"] + getCUParamsValuesList(method) />
        <#assign injectedCmpVariableName = getImplementedInterfaceInjectedVariableName(interfaceMetaData.serviceName) />
        <#assign execStm = injectedCmpVariableName + "." +  method.name + "(" + paramsValues?join(", ") + ")" />
    @Override
    public ${method.type} ${method.name}(${paramsDeclaration})
    {
        ${interfaceMetaData.serviceName}<${entity.name}> ${injectedCmpVariableName} = Ioc.context().findOf(${interfaceMetaData.serviceName}.class, ${entity.name}.class);
        <#if method.type != "void">return </#if>${execStm};
    }

    </#list>
</#macro>