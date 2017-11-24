<#function findPropertyType field>
    <#return "SimpleObjectProperty<" + field.javaType + ">" />
</#function>

<#function findPropertyTypeDimond field>
    <#return "SimpleObjectProperty<>" />
</#function>
