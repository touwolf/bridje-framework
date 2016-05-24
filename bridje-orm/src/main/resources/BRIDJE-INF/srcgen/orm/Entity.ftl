<#include "./EntityUtils.ftl" />

package ${doc.model.@package};

import org.bridje.orm.*;
<#if hasType("date") >
import java.util.Date;
</#if>

@Entity(table = "${node.@name?uncap_first}")
public class ${node.@name}
{
    <#list node.* as field>
    @DbObject("${field.@name}")
    public ${findTableColumn(field)}<${findType(field)}, ${findType(field)}> ${field.@name?upper_case};

    </#list>
    <#list node.* as field>
    <#if (field.@autoIncrement[0]?? && field.@autoIncrement == "true") 
            || (field.@key[0]?? && field.@key == "true")>
    @Key<#if (field.@autoIncrement[0]?? && field.@autoIncrement == "true")>(autoIncrement = true)</#if>
    </#if>
    @Field
    private ${findType(field)} ${field.@name};

    </#list>
    <#list node.* as field>
    public ${findType(field)} get${field.@name?cap_first}()
    {
        return this.${field.@name};
    }

    public void set${field.@name?cap_first}(${findType(field)} ${field.@name})
    {
        this.${field.@name} = ${field.@name};
    }

    </#list>
}