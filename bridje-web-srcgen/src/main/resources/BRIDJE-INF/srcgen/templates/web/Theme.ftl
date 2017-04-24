[#ftl]
<#ftl encoding="UTF-8">
<#include "../ThemeBase.ftl" >
[#list uisuite.ftlIncludes![] as ftlInc]
<#include "${ftlInc}" >
[/#list]

[#list uisuite.resources as r]
<#macro render${r.name?cap_first}Scripts themeName>
[#list r.scripts as s]
    <@renderScript theme=themeName script="${s.href}" async=${s.async?c} defer=${s.defer?c} />
[/#list]
</#macro>

<#macro render${r.name?cap_first}Styles themeName>
[#list r.styles as s]
    <@renderStyle themeName "${s.href}" />
[/#list]
</#macro>

[/#list]
[#list uisuite.macros as m]
<#macro ${m.name} ${m.parameters}>
[#compress]${w.content}[/#compress]
</#macro>

[/#list]
[#list uisuite.controls as w]
<#macro render${w.name} control>
[#compress]${w.render}[/#compress]
</#macro>

[/#list]
<#macro renderControl control>
    <#if control.class.package.name == "${uisuite.package}">
        <#switch control.class.simpleName>
            [#list uisuite.controls as w]
            <#case "${w.name}">
              <@render${w.name} control />
              <#break>
            [/#list]
            <#default>
                Control control.class.simpleName Not Found
        </#switch>
    </#if>
</#macro>

<#macro renderThemeScripts themeName>
    [#list uisuite.defaultResources.scripts as s]
    <@renderScript theme=themeName script="${s.href}" async=${s.async?c} defer=${s.defer?c} />
    [/#list]
    <#list view.resources as res>
        <#assign macroName = "render" + res?cap_first + "Scripts" />
        <#if .vars[macroName]?? >
            <@.vars[macroName] themeName />
        <#else>
            <!-- ERROR Invalid resource ${r"${res}"} -->
        </#if>
    </#list>
</#macro>

<#macro renderThemeStyles themeName>
    [#list uisuite.defaultResources.styles as s]
    <@renderStyle themeName "${s.href}" />
    [/#list]
    [#list uisuite.defaultResources.links as l]
    <@renderLink themeName "${l.rel!}" "${l.href!}" "${l.sizes!}" />
    [/#list]
    <#list view.resources as res>
        <#assign macroName = "render" + res?cap_first + "Styles" />
        <#if .vars[macroName]?? >
            <@.vars[macroName] themeName />
        <#else>
            <!-- ERROR Invalid resource ${r"${res}"} -->
        </#if>
    </#list>
</#macro>

[#if uisuite.renderBody?? && uisuite.renderBody?has_content]
<#macro renderBody>
[#compress]${uisuite.renderBody!}[/#compress]
</#macro>
[/#if]

[#if uisuite.renderHead?? && uisuite.renderHead?has_content]
<#macro renderHead>
[#compress]${uisuite.renderHead!}[/#compress]
</#macro>
[/#if]

[#if uisuite.renderViewContainer?? && uisuite.renderViewContainer?has_content]
<#macro renderViewContainer>
[#compress]${uisuite.renderViewContainer!}[/#compress]
</#macro>
[/#if]

<@renderMain "${uisuite.name?lower_case}" />