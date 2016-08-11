<#ftl encoding="UTF-8">

<#include "./comps.ftl" />

<#macro renderStyle theme style>
    <link rel="stylesheet" href="/__themes/${theme}/${style}" />
</#macro>

<#macro renderScript theme script>
    <script src="/__themes/${theme}/${script}" type="text/javascript"></script>
</#macro>
