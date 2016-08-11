<#ftl encoding="UTF-8">

<#macro button c>
    <button <#if c.id??>id="${c.id}" </#if>class="action ${c.styleClass!}"
                data-action="${c.action.expression!}">${c.caption!}</button>
</#macro>

<#macro combobox c>
    <select <#if c.id??>id="${c.id}" </#if>class="${c.styleClass!}"
            name="${c.valueExpression.parameter}">
        <option></option>
    </select>
</#macro>

<#macro header c>
    <h1 <#if c.id??>id="${c.id}" </#if>class="${c.styleClass!}">${c.value!}</h1>
</#macro>

<#macro text c>
    <span <#if c.id??>id="${c.id}" </#if>class="text ${c.styleClass!}">${c.value!}</span>
</#macro>

<#macro image c>
    <#if c.href??>
        <a <#if c.id??>id="${c.id-link}" </#if>class="img-link ${c.styleClass!}" href="${c.href!}">
            <img <#if c.id??>id="${c.id}" </#if>class="${c.styleClass!}" src="${c.src}" />
        </a>
    <#else>
        <img <#if c.id??>id="${c.id}" </#if>class="${c.styleClass!}" src="${c.src}" />
    </#if>
</#macro>

<#macro link c>
    <a <#if c.id??>id="${c.id}" </#if>class="${c.styleClass!}"
            href="${c.href!"#"}" >${c.value!"-"}</a>
</#macro>

<#macro paragraph c>
    <p <#if c.id??>id="${c.id}" </#if>class="${c.styleClass!}">
        ${c.value!}
    </p>
</#macro>

<#macro textarea c>
    <textarea <#if c.id??>id="${c.id}" </#if>class="${c.styleClass!}">
        ${c.value!}
    </textarea>
</#macro>

<#macro textbox c>
    <input <#if c.id??>id="${c.id}" </#if>class="textbox ${c.styleClass!}" type="text"
                       name="${c.valueExpression.parameter}" value="${c.value!}" />
</#macro>

<#macro checkbox c>
    <input <#if c.id??>id="${c.id}" </#if>class="checkbox ${c.styleClass!}" type="checkbox"
                       name="${c.valueExpression.parameter}" value="${c.value!}" />
</#macro>

<#macro password c>
    <input <#if c.id??>id="${c.id}" </#if>class="textbox password ${c.styleClass!}" type="password" name="${c.valueExpression.parameter}" value="${c.value!}" />
</#macro>

<#macro empty c>
    <span <#if c.id??>id="${c.id}" </#if>class="empty ${c.styleClass!}"></span>
</#macro>

<#macro table c>
    <table <#if c.id??>id="${c.id}" </#if>class="empty ${c.styleClass!}">
        <thead>
            <tr>
                <#list c.columns as c>
                    <th>${c.title}<th>
                </#list>
            </tr>
        </thead>
        <tbody>
            <#list c.data as row>
                ${env.setVar(table.var, row)!}
                <tr>
                    <#list c.columns as c>
                        <td>${c.value}</td>
                    </#list>
                </tr>
            </#list>
        </tbody>
    </table>
</#macro>

<#macro borderlayout c>
    <div <#if c.id??>id="${c.id}" </#if>class="borderlayout ${c.styleClass!}">
        <table class="borderlayout">
            <#if c.top??>
                <tr class="borderlayout borderlayout-top">
                    <td class="borderlayout borderlayout-top">
                        <@render c.top.component />
                    </td>
                </tr>
            </#if>
            <#if c.left?? || c.center?? || c.right??>
                <tr class="borderlayout borderlayout-middle">
                    <#if c.left??>
                        <td class="borderlayout borderlayout-left">
                            <@render c.left.component />
                        </td>
                    </#if>
                    <#if c.center??>
                        <td class="borderlayout borderlayout-center">
                            <@render c.center.component />
                        </td>
                    </#if>
                    <#if c.right??>
                        <td class="borderlayout borderlayout-right">
                            <@render c.right.component />
                        </td>
                    </#if>
                </tr>
            </#if>
            <#if c.bottom??>
                <tr class="borderlayout borderlayout-bottom">
                    <td class="borderlayout borderlayout-bottom">
                        <@render c.bottom.component />
                    </td>
                </tr>
            </#if>
        </table>
    </div>
</#macro>

<#macro gridlayout c>
    <div <#if c.id??>id="${c.id}" </#if>class="gridlayout ${c.styleClass!}">
        <table class="gridlayout">
            <tr class="gridlayout">
                <#assign numb = 0 />
                <#list c.children as ch>
                    <td class="gridlayout">
                        <@render ch />
                    </td>
                    <#assign numb = numb + 1 />
                    <#if numb % c.columns == 0 >
                        </tr>
                        <tr class="gridlayout">
                    </#if>
                </#list>
            </tr>
        </table>
    </div>
</#macro>

<#macro horizontallayout c>
    <div <#if c.id??>id="${c.id}" </#if>class="hlayout ${c.styleClass!}">
        <#list c.children as ch>
            <div class="hlayout-item" style="float: left">
                <@render ch />
            </div>
            <div style="clear: both;"></div>
        </#list>
    </div>
</#macro>

<#macro verticallayout c>
    <div <#if c.id??>id="${c.id}" </#if>class="vlayout ${c.styleClass!}">
        <#list c.children as ch>
            <div class="vlayout-item">
                <@render ch />
            </div>
        </#list>
    </div>
</#macro>

<#macro render c>
    <#if !(c.visible??) || c.visible>
        <#if c.class.package.name == "org.bridje.wui.comp">
            <#if c.class.simpleName == "Button">
                <@button c />
            <#elseif c.class.simpleName == "ComboBox">
                <@combobox c />
            <#elseif c.class.simpleName == "Header">
                <@header c />
            <#elseif c.class.simpleName == "Text">
                <@text c />
            <#elseif c.class.simpleName == "Image">
                <@image c />
            <#elseif c.class.simpleName == "Link">
                <@link c />
            <#elseif c.class.simpleName == "Paragraph">
                <@paragraph c />
            <#elseif c.class.simpleName == "TextArea">
                <@textarea c />
            <#elseif c.class.simpleName == "TextBox">
                <@textbox c />
            <#elseif c.class.simpleName == "Password">
                <@password c />
            <#elseif c.class.simpleName == "Empty">
                <@empty c />
            <#elseif c.class.simpleName == "Table">
                <@table c />
            <#elseif c.class.simpleName == "BorderLayout">
                <@borderlayout c />
            <#elseif c.class.simpleName == "GridLayout">
                <@gridlayout c />
            <#elseif c.class.simpleName == "HorizontalLayout">
                <@horizontallayout c />
            <#elseif c.class.simpleName == "VerticalLayout">
                <@verticallayout c />
            <#elseif c.class.simpleName == "CheckBox">
                <@checkbox c />
            </#if>
        </#if>
    </#if>
</#macro>
