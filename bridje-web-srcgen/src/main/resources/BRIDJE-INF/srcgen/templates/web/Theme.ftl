[#ftl]
<#ftl encoding="UTF-8">
<#include "../ThemeBase.ftl" >
[#list uisuite.ftlIncludes![] as ftlInc]
<#include "${ftlInc}" >
[/#list]
[#list uisuite.ftlImports![] as ftlImp]
<#import "${ftlImp.file}" as  $(ftlImp.name) >
[/#list]

[#list uisuite.ftlMacros![] as ftlMac]
<#macro ${ftlMac.name} ${ftlMac.params!} >
    [@compress single_line=true][#compress]${ftlMac.content}[/#compress][/@compress]
</#macro>

[/#list]
[#list uisuite.ftlFunctions![] as ftlFunc]
<#function ${ftlFunc.name} ${ftlFunc.params!} >
    [@compress single_line=true][#compress]${ftlFunc.content}[/#compress][/@compress]
</#function>

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
[#list uisuite.macros![] as m]
<#macro ${m.name} ${m.parameters}>
    [@compress single_line=true][#compress]${w.content}[/#compress][/@compress]
</#macro>

[/#list]
[#list uisuite.templates![] as w]
[#if w.render??]
<#macro render${w.name}Template control>
    [#if w.template?? && w.template.render??]
    <@render${w.template.name}Template control >
    [/#if]
    [@compress single_line=true][#compress]${w.render!}[/#compress][/@compress]
    [#if w.template?? && w.template.render??]
    </@render${w.template.name}Template>
    [/#if]
</#macro>

[/#if]
[/#list]
[#list uisuite.controls as w]
<#macro render${w.name}Control control>
    [#if w.base?? && w.base.render??]
    <@render${w.base.name}Control control >
    [/#if]
    [#if w.template?? && w.template.render??]
    <@render${w.template.name}Template control >
    [/#if]
    [@compress single_line=true][#compress]${w.render!}[/#compress][/@compress]
    [#if w.template?? && w.template.render??]
    </@render${w.template.name}Template>
    [/#if]
    [#if w.base?? && w.base.render??]
    </@render${w.base.name}Control>
    [/#if]
</#macro>

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
<#macro renderBody>
    [@compress single_line=true][#compress]${uisuite.renderBody!}[/#compress][/@compress]
</#macro>
[/#if]

[#if uisuite.renderHead?? && uisuite.renderHead?has_content]
<#macro renderHead>
    [@compress single_line=true][#compress]${uisuite.renderHead!}[/#compress][/@compress]
</#macro>
[/#if]

[#if uisuite.renderView?? && uisuite.renderView?has_content]
<#macro renderViewContainer>
    [@compress single_line=true][#compress]${uisuite.renderView!}[/#compress][/@compress]
</#macro>
[/#if]

<@renderMain "${uisuite.name?lower_case}" />
