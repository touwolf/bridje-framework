<#ftl encoding="UTF-8">

<#include "./utils.ftl" />
<#include "./comps.ftl" />

<#macro renderThemeScripts themeName>
</#macro>

<#macro renderThemeStyles themeName>
</#macro>

<#macro renderMetaTag>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <#list view.metaTags![] as meta>
        <meta name="${meta.name!}" content="${meta.content!}">
    </#list>
</#macro>

<#macro renderFullView themeName>
    <!DOCTYPE html>
    <html>
        <head>
            <title>${view.title!}</title>
            <@renderMetaTag />
            <@renderThemeStyles themeName />
        </head>
        <body class="${themeName}-theme">
            <form id="view-form" >
                <#if view.root??>
                    <@renderPartialView view.root />
                </#if>
            </form>

            <@renderThemeScripts themeName />
        </body>
    </html>
</#macro>

<#macro renderPartialView component>
    <input type="hidden" name="__view" value="${view.name}" />
    <input type="hidden" name="__action" value="" />
    <@renderState />
    <@render component />
</#macro>

<#macro renderState>
    <#list state?keys as k>
        <input type="hidden" name="__state.${k}" value="${state[k]}" />
    </#list>
</#macro>

<#macro renderMain themeName >
    <#if result??
         && result.class.simpleName == "RedirectTo"
         && result.status??
         && result.resource??>
        <script>
            window.location = '${result.resource}';
        </script>
    <#else>
        <#if component??>
            <@renderPartialView component />
        <#else>
            <@renderFullView themeName />
        </#if>
    </#if>
</#macro>