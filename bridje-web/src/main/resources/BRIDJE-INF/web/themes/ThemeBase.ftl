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
    <#if style?starts_with("http://") || style?starts_with("https://")>
        <link rel="stylesheet" href="${style}" />
    <#else>
        <link rel="stylesheet" href="/__themes/${theme}/${style}" />
    </#if>
</#macro>

<#macro renderLink theme rel href sizes>
    <#if href?starts_with("http://") || href?starts_with("https://")>
        <link rel="${rel!}" href="${href}" <#if sizes != "">sizes="${sizes!}"</#if> />
    <#else>
        <link rel="${rel!}" href="/__themes/${theme}/${href}" <#if sizes != "">sizes="${sizes!}"</#if> />
    </#if>
</#macro>

<#macro renderScript theme script async=false defer=false>
    <#if script?starts_with("http://") || script?starts_with("https://")>
        <script src="${script}" type="text/javascript"<#if async> async</#if><#if defer> defer</#if>></script>
    <#else>
        <script src="/__themes/${theme}/${script}" type="text/javascript"<#if async> async</#if><#if defer> defer</#if>></script>
    </#if>
</#macro>

<#macro renderViewScripts themeName>
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
    <form data-bridje-view="${view.name}" id="bridje-view-container" enctype="multipart/form-data" method="post"  >
        <#nested />
    </form>
</#macro>

<#macro renderBody>
    <body>
        <#nested />
    </body>
</#macro>

<#macro renderHead>
    <head>
        <title>${view.title!}</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <#nested />
    </head>
</#macro>

<#macro renderFullView themeName>
    <!DOCTYPE html>
    <html>
        <@renderHead>
            <@renderScript "bridje" "bridje-ajax.js" />
            <@renderMetaTag />
            <@renderThemeStyles themeName />
        </@renderHead>
        <@renderBody>
            <@renderViewContainer>
                <#if view.root??>
                    <@renderPartialView view.root themeName />
                </#if>
            </@renderViewContainer>

            <@renderThemeScripts themeName />
        </@renderBody>
    </html>
</#macro>

<#macro renderPartialView currentControl themeName>
    <@renderState />
    <@renderControl currentControl />
    <@renderViewScripts themeName />
</#macro>

<#macro renderState>
    <#assign currState = stateProvider.currentState />
    <#list currState?keys as k>
        <input type="hidden" name="__state.${k}" value="${currState[k]}" />
    </#list>
</#macro>

<#macro renderMain themeName >
    <#if eventResult??
         && eventResult.data??
         && eventResult.data?is_hash
         && eventResult.data.class.simpleName == "RedirectTo"
         && eventResult.data.status??
         && eventResult.data.resource??>
        <script>
            window.location = '${eventResult.data.resource}';
        </script>
    <#else>
        <#if control??>
            <#assign renderType = "partial" />
            <@renderPartialView control themeName />
        <#else>
            <#assign renderType = "full" />
            <@renderFullView themeName />
        </#if>
    </#if>
</#macro>
