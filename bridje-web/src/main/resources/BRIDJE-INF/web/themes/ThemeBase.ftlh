<#ftl encoding="UTF-8">
<#-- 
 # Copyright 2016 Bridje Framework.
 #
 # Licensed under the Apache License, Version 2.0 (the "License");
 # you may not use this file except in compliance with the License.
 # You may obtain a copy of the License at
 #
 #      http://www.apache.org/licenses/LICENSE-2.0
 #
 # Unless required by applicable law or agreed to in writing, software
 # distributed under the License is distributed on an "AS IS" BASIS,
 # WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 # See the License for the specific language governing permissions and
 # limitations under the License.
-->

<#macro renderStyle theme style>
    <#if style?starts_with("http://") || style?starts_with("https://") || style?starts_with("/")>
        <link rel="stylesheet" href="${style}" />
    <#else>
        <#if renderMode == "dinamic">
            <link rel="stylesheet" href="/__themes/${theme}/${style}" />
        <#else>
            <style>
                ${resourceRenderer.renderResource(theme, style)}
            </style>
        </#if>
        
    </#if>
</#macro>

<#macro renderLink theme rel href sizes>
    <#if href?starts_with("http://") || href?starts_with("https://") || href?starts_with("/")>
        <link rel="${rel!}" href="${href}" <#if sizes != "">sizes="${sizes!}"</#if> />
    <#else>
        <link rel="${rel!}" href="/__themes/${theme}/${href}" <#if sizes != "">sizes="${sizes!}"</#if> />
    </#if>
</#macro>

<#macro renderScript theme script async=false defer=false>
    <#if script?starts_with("http://") || script?starts_with("https://") || script?starts_with("/")>
        <script src="${script}" type="text/javascript"<#if async> async</#if><#if defer> defer</#if>></script>
    <#else>
        <script src="/__themes/${theme}/${script}" type="text/javascript"<#if async> async</#if><#if defer> defer</#if>></script>
    </#if>
</#macro>

<#macro renderThemeScripts themeName>
</#macro>

<#macro renderThemeStyles themeName>
</#macro>

<#macro renderControl control>
    macro renderControl must be implemented for this theme.
</#macro>

<#macro renderAllControls controls>
    <#list controls as w>
        <@renderControl w />
    </#list>
</#macro>

<#macro renderMetaTag>
    <#list view.metaTags![] as meta>
        <meta name="${meta.name!}" content="${meta.content!}">
    </#list>
</#macro>

<#macro renderViewContainer>
    <#nested />
    <#if renderMode == "dinamic">
        <script>
            if( !window.__bridje ) window.__bridje = {};
            window.__bridje.info = {
                dataBridjeView: '${view.name}',
                enctype: '<#if view.hasFileInput()>multipart/form-data<#else>application/x-www-form-urlencoded</#if>',
                currState: '${stateProvider.currentState!}'
            };
        </script>
    </#if>
</#macro>

<#macro renderBody>
    <body>
        <#nested />
    </body>
</#macro>

<#macro renderHead>
    <head>
        <#if renderMode == "dinamic">
            <title>${view.title!}</title>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </#if>
        <#nested />
    </head>
</#macro>

<#macro renderFullView themeName>
    <!DOCTYPE html>
    <html>
        <@renderHead>
            <#if renderMode == "dinamic">
                <@renderScript "bridje" "bridje-ajax.js" />
                <@renderMetaTag />
            </#if>
            <@renderThemeStyles themeName />
        </@renderHead>
        <@renderBody>
            <@renderViewContainer>
                <#if view.root??>
                    <@renderPartialView view.root themeName />
                </#if>
            </@renderViewContainer>

            <#if renderMode == "dinamic">
                <@renderThemeScripts themeName />
            </#if>
        </@renderBody>
    </html>
</#macro>

<#macro renderPartialView currentControl themeName>
    <@renderControl currentControl true />
</#macro>

<#macro renderMain themeName >
    <#if control??>
        <#assign renderType = "partial" />
        <@renderPartialView control themeName />
    <#else>
        <#assign renderType = "full" />
        <@renderFullView themeName />
    </#if>
</#macro>

<#macro doDynamicControl control doStatic=false>
    <#if control.data?? && !doStatic>
        <#local itemVarName = control.var />
        <#local indexVarName = control.var + "Index" />
        <#list control.data as dataItem >
            ${env.pushVar(itemVarName, dataItem)}
            ${env.pushVar(indexVarName, dataItem_index)}
                <#nested />
            ${env.popVar(itemVarName)}
            ${env.popVar(indexVarName)}
        </#list>
    <#else>
        <#nested />
    </#if>
</#macro>