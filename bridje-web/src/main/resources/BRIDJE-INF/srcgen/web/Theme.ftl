[#ftl]
<#include "../ThemeBase.ftl />

[#list theme.widgets as w]
<#macro render${w.name} widget>
[#compress]${w.render}[/#compress]
</#macro>
[/#list]

<#macro render widget>
    <#if widget.class.package.name == "org.bridje.wui.widgets">
        <#switch widget.class.simpleName>
            [#list theme.widgets as w]
            <#case "${w.name}">
              <@render${w.name} widget />
              <#break>
            [/#list]
            <#default>
                Widget widget.class.simpleName Not Found
        </#switch>
    </#if>
</#macro>