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

/**
 * This class represents the ${node.attrs.name} entity.
 * ${findEntityDescription(node)}
 */
@Entity(table = "${findTableName(node)}")
public class ${node.attrs.name}
{
    /**
     * This static field holds a reference to the Table object that represents
     * the SQL table used by the ${node.attrs.name} entity.
     * ${findEntityDescription(node)}
     */
    @DbObject
    public static Table<${node.attrs.name}> TABLE;

    <#list node.fields?keys as fieldName>
    <#assign field = node.fields[fieldName] />
    /**
     * This static field holds a reference to the TableColumn object that represents
     * the SQL column used by the ${field.@name} field.
     * ${findFieldDescription(field)}
     */
    @DbObject("${field.@name}")
    public static ${findTableColumn(field)}<${node.attrs.name}<#if !isString(field)>, ${findType(field)}</#if>> ${findColumnName(field)?upper_case};

    </#list>
    <#list node.fields?keys as fieldName>
    <#assign field = node.fields[fieldName] />
    <#if isKey(field) >
    @Key<#if isAutoIncrement(field)>(autoIncrement = true)</#if>
    </#if>
    @Field(column = "${findColumnName(field)}"<#if hasLength(field)>, length = ${getLength(field)}</#if><#if isIndex(field)>, index = true</#if><#if hasSqlType(field)>, type = JDBCType.${getSqlType(field)}</#if><#if hasAdapter(field)>, adapter = ${getAdapter(field)}.class</#if>)
    private ${findType(field)} ${field.@name};

    </#list>
    <#list node.fields?keys as fieldName>
    <#assign field = node.fields[fieldName] />
    /**
     * Gets the value of the ${field.@name} field.
     * ${findFieldDescription(field)}
     * @return A ${findType(field)} object representing the value 
     * of the ${field.@name} field.
     */
    public ${findType(field)} get${field.@name?cap_first}()
    {
        return this.${field.@name};
    }

    /**
     * Sets the value of the ${field.@name} field.
     * ${findFieldDescription(field)}
     * @param ${field.@name} The ${findType(field)} object representing the value 
     * of the ${field.@name} field.
     */
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
        final ${node.attrs.name} other = (${node.attrs.name}) obj;
        return Objects.equals(this.get${findKeyField().@name?cap_first}(), other.get${findKeyField().@name?cap_first}());
    }
}