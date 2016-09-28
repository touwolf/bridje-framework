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

<#macro renderViewUpdateScript>
    <script>
        (function($){
            $(document).ready(function()
            {
                window.initViewForm = function(el)
                {
                    el.find('button.action').click(function(event)
                    {
                        event.preventDefault();

                        $('input[name=__action]').val($(event.target).data('action'));

                        $.ajax(
                        {
                            type: 'POST',
                            url: window.location,
                            data: $('form#view-form').serialize(),
                            success: function(response)
                            {
                                $('form#view-form').html(response);
                                initViewForm($('form#view-form'));
                                if(typeof initBridjeView != 'undefined')
                                {
                                    initBridjeView();
                                    $(window).trigger( "load" );
                                }
                            }
                        });
                    });
                };

                initViewForm($('form#view-form'));
            });
         })(jQuery); 
    </script>
</#macro>

<#macro renderThemeScripts themeName>
    <#list view.resources as r>
        <#list resourcesMap[r].scripts![] as s>
            <@renderScript themeName s />
        </#list>
    </#list>
    [#list theme.defaultResources as r]
    [#list r.scripts as s]
    <@renderScript themeName "${s.href}" />
    [/#list]
    [/#list]
    <@renderViewUpdateScript />
</#macro>

<#macro renderThemeStyles themeName>
    <#list view.resources as r>
        <#list resourcesMap[r].styles![] as s>
            <@renderStyle themeName s />
        </#list>
    </#list>
    [#list theme.defaultResources as r]
    [#list r.styles as s]
    <@renderStyle themeName "${s.href}" />
    [/#list]
    [/#list]
</#macro>

<@renderMain "${theme.name}" />