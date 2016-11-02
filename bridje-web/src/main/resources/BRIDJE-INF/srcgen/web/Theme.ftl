[#ftl]
<#ftl encoding="UTF-8">
<#include "../ThemeBase.ftl" >

<#assign resourcesMap = {} />
[#list theme.resources as r]
<#assign scripts = [] />
[#list r.scripts as s]
<#assign scripts = scripts + ["${s.href}"] />
[/#list]
<#assign styles = [] />
[#list r.styles as s]
<#assign styles = styles + ["${s.href}"] />
[/#list]
<#assign resource = {} />
<#assign resource = resource + {"scripts": scripts} />
<#assign resource = resource + {"styles": styles} />
<#assign resourcesMap = resourcesMap + {"${r.name}":resource} />

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
        <#list resourcesMap[r].scripts![] as s>
            <@renderScript themeName s />
        </#list>
    </#list>
</#macro>

<#macro renderThemeStyles themeName>
    [#list theme.defaultResources.styles as s]
    <@renderStyle themeName "${s.href}" />
    [/#list]
    <#list view.resources as r>
        <#list resourcesMap[r].styles![] as s>
            <@renderStyle themeName s />
        </#list>
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