[#ftl]
<#ftl encoding="UTF-8" output_format="HTML" auto_esc=true >
<#include "../ThemeBase.ftl" >
[#list uisuite.ftlIncludes![] as ftlInc]
<#include "${ftlInc}" >
[/#list]
[#list uisuite.ftlImports![] as ftlImp]
<#import "${ftlImp.file}" as  $(ftlImp.name) >
[/#list]

[#list uisuite.ftlMacros![] as ftlMac]
[@compress single_line=true][#compress]
<#macro ${ftlMac.name} ${ftlMac.params!} >
    ${ftlMac.content}
</#macro>
[/#compress][/@compress]

[/#list]
[#list uisuite.ftlFunctions![] as ftlFunc]
<#function ${ftlFunc.name} ${ftlFunc.params!} >
    [@compress single_line=true][#compress]${ftlFunc.content}[/#compress][/@compress]
</#function>

[/#list]
[#list uisuite.resources![] as r]
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
[#list uisuite.macros![] as m]
[@compress single_line=true][#compress]
<#macro ${m.name} ${m.params!}>
    ${w.content}
</#macro>
[/#compress][/@compress]

[/#list]
[#list uisuite.controls as w]
[#list w.macros![] as m]
[@compress single_line=true][#compress]
<#macro render${w.name}${m.name?cap_first} ${m.params!}>
    ${w.replaceMacros(m.content)}
</#macro>
[/#compress][/@compress]

[/#list]
[@compress single_line=true][#compress]
<#macro render${w.name}Control control>
    [#if w.base?? && w.base.render??]
    <@render${w.base.name}Control control >
    [/#if]
    ${w.replaceMacros(w.render)}
    [#if w.base?? && w.base.render??]
    </@render${w.base.name}Control>
    [/#if]
</#macro>
[/#compress][/@compress]

[/#list]
<#macro renderControl control>
    <#if control.class.package.name == "${uisuite.package}">
        <#assign macroName = "render" + control.class.simpleName?cap_first + "Control" />
        <#if .vars[macroName]?? >
            <@.vars[macroName] control />
        <#else>
            <!-- ERROR Control ${r"${control.class.simpleName}"} Not Found -->
        </#if>
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
[@compress single_line=true][#compress]
<#macro renderBody>
    ${uisuite.renderBody!}
</#macro>
[/#compress][/@compress]

[/#if]
[#if uisuite.renderHead?? && uisuite.renderHead?has_content]
[@compress single_line=true][#compress]
<#macro renderHead>
    ${uisuite.renderHead!}
</#macro>
[/#compress][/@compress]

[/#if]
[#if uisuite.renderView?? && uisuite.renderView?has_content]
[@compress single_line=true][#compress]
<#macro renderViewContainer>
    ${uisuite.renderView!}
</#macro>
[/#compress][/@compress]

[/#if]
<@renderMain "${uisuite.name?lower_case}" />
