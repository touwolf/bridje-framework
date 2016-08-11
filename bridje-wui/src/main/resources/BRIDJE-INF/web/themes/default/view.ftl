<#ftl encoding="UTF-8">

<#include "./components.ftl"/>

<#if component??>
    <@renderPartialView component />
<#else>
    <@renderFullView />
</#if>

<#if result??
     && result.class.simpleName == "RedirectTo"
     && result.status??
     && result.resource??>
    <script>
        window.location = '${result.resource}';
    </script>
<#else>
    <#assign themeName = "default" />
    <@renderAll />
</#if>
