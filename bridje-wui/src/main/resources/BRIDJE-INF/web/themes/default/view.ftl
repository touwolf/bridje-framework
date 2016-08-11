<#ftl encoding="UTF-8">

<#include "../base/utils.ftl"/>
<#include "../base/views.ftl"/>

<#macro renderThemeScripts themeName>
    <@renderScript "default" "jquery-min.js" />
    <@renderScript "default" "bridje-wui.js" />
</#macro>

<#macro renderThemeStyles themeName>
    <@renderStyle "default" "bridje-wui.css" />
</#macro>

<@renderMain "default" />