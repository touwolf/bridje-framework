<#--- Para interfaces implementadas -->
<#include "orm-common.ftl">
package ${interface.packageName};

<#list interface.importDeclarations as importDeclaration>
${importDeclaration}
</#list>

/**
*  Interface explicitamente implementada por entidades de <b>${dm.name}</b>
*/
public interface ${interface.serviceName}<T>
{

<#list interface.methods as method>
    <#assign params = ["T caller"] + getCUParamsDefinitionList(method)/>
    public ${method.type} ${method.name}(${params?join(", ")});

</#list>
}

