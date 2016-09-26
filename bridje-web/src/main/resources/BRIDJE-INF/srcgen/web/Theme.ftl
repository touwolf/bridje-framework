[#ftl]
<#ftl encoding="UTF-8">
<#include "../ThemeBase.ftl" >

[#list theme.widgets as w]
<#macro render${w.name} widget>
[#compress]${w.render}[/#compress]
</#macro>

[/#list]
<#macro renderWidget widget>
    <#if widget.class.package.name == "${theme.package}">
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

<#macro renderThemeScripts themeName>
    [#list theme.resources as r]
        [#list r.scripts as s]
            <@renderScript themeName "${s.href}" />
        [/#list]
    [/#list]
    <@renderDefaultThemeScripts themeName />
</#macro>

<#macro renderThemeStyles themeName>
    [#list theme.resources as r]
        [#list r.styles as s]
            <@renderStyle themeName "${s.href}" />
        [/#list]
        [#list r.fonts as f]
            <!-- font "${f.href}" -->
        [/#list]
    [/#list]
</#macro>

<@renderMain "${theme.name}" />