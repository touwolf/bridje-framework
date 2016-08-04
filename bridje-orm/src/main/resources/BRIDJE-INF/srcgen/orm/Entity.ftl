<#include "./EntityUtils.ftl" />

package ${doc.model.@package};

<#if hasAtLeastOneSqlType() >
import java.sql.JDBCType;
</#if>
import org.bridje.orm.*;
<#if hasAtLeastOneType("date") >
import java.util.Date;
</#if>
<#if hasAtLeastOneType("time") >
import java.sql.Time;
</#if>
import java.util.Objects;

@Entity(table = "${findTableName(node)}")
public class ${node.@name}
{
    @DbObject
    public static Table<${node.@name}> TABLE;

    <#list node.* as field>
    @DbObject("${field.@name}")
    public static ${findTableColumn(field)}<${node.@name}<#if !isString(field)>, ${findType(field)}</#if>> ${field.@name?upper_case};

    </#list>
    <#list node.* as field>
    <#if isKey(field) >
    @Key<#if isAutoIncrement(field)>(autoIncrement = true)</#if>
    </#if>
    @Field(column = "${findColumnName(field)}"<#if isIndex(field)>, index = true</#if><#if hasSqlType(field)>, type = JDBCType.${getSqlType(field)}</#if><#if hasAdapter(field)>, adapter = ${getAdapter(field)}.class</#if>)
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
    @Override
    public int hashCode()
    {
        if(get${findKeyField().@name?cap_first}() == null)
        {
            return super.hashCode();
        }
        return get${findKeyField().@name?cap_first}().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final ${node.@name}Proxy other = (${node.@name}Proxy) obj;
        return Objects.equals(this.get${findKeyField().@name?cap_first}(), other.get${findKeyField().@name?cap_first}());
    }
}