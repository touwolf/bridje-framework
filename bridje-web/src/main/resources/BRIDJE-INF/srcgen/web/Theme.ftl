[#ftl]
<#ftl encoding="UTF-8">
<#include "../ThemeBase.ftl" >

[#list theme.resources as r]
<#macro render${r.name?cap_first}Scripts themeName>
[#list r.scripts as s]
    <@renderScript themeName "${s.href}" />
[/#list]
</#macro>

<#macro render${r.name?cap_first}Styles themeName>
[#list r.styles as s]
    <@renderStyle themeName "${s.href}" />
[/#list]
</#macro>

[/#list]
[#list theme.macros as m]
<#macro ${m.name} ${m.parameters}>
[#compress]${w.content}[/#compress]
</#macro>

[/#list]
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
    [#list theme.defaultResources.scripts as s]
    <@renderScript themeName "${s.href}" />
    [/#list]
    <#list view.resources as r>
        <#assign macroName = "render" + r?cap_first + "Scripts" />
        <@.vars[macroName] themeName />
    </#list>
</#macro>

<#macro renderThemeStyles themeName>
    [#list theme.defaultResources.styles as s]
    <@renderStyle themeName "${s.href}" />
    [/#list]
    [#list theme.defaultResources.links as l]
    <@renderLink themeName "${l.rel}" "${l.href}" />
    [/#list]
    <#list view.resources as r>
        <#assign macroName = "render" + r?cap_first + "Styles" />
        <@.vars[macroName] themeName />
    </#list>
</#macro>

[#if theme.body??]
<#macro renderBody>
[#compress]${theme.renderBody!}[/#compress]
</#macro>
[/#if]

[#if theme.renderViewContainer??]
<#macro renderViewContainer>
[#compress]${theme.renderViewContainer!}[/#compress]
</#macro>
[/#if]

<@renderMain "${theme.name}" />