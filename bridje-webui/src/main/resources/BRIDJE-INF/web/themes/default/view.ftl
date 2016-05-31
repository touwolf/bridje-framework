
<#macro render c>
    <#if c.class.simpleName == "Header">
        <h1>${c.value!}</h1>
    <#elseif c.class.simpleName == "VLayout">
        <div>
            <#list c.childrends as ch>
                <@render ch />
            </#list>
        </div>
    <#elseif c.class.simpleName == "TextBox">
        <input type="text" name="${c.valueExpression.parameter}" value="${c.value!}" />
    <#elseif c.class.simpleName == "Button">
        <button class="action" data-action="${c.action.expression!}" >${c.value!}</button>
    </#if>
</#macro>

<#if component??>
    <input type="hidden" name="__view" value="${view.name}" />
    <input type="hidden" name="__action" value="" />
    <@render component />
<#else>
    <!DOCTYPE html>
    <html>
        <head>
            <title>Some Page</title>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <link rel="stylesheet" href="/__themes/default/view.css" />
            <script src="/__themes/default/jquery-2.2.3.min.js" type="text/javascript"></script>
            <script src="/__themes/default/view.js" type="text/javascript"></script>
        </head>
        <body>
            <form id="view-form">
                <input type="hidden" name="__view" value="${view.name}" />
                <input type="hidden" name="__action" value="" />
                <@render view.root />
            </form>
        </body>
    </html>
</#if>