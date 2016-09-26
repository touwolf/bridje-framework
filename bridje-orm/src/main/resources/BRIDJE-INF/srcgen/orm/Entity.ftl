
package ${entity.package};

import java.sql.JDBCType;
import org.bridje.orm.*;
import java.util.Objects;

/**
 * This class represents the  ${entity.name} entity.
 * ${entity.description!}
 */
@Entity(table = "${entity.table}")
public class ${entity.name}
{
    /**
     * This static field holds a reference to the Table object that represents
     * the SQL table used by the ${entity.name} entity.
     * ${entity.description!}
     */
    @DbObject
    public static Table<${entity.name}> TABLE;

    <#list entity.fields as field>
    /**
     * This static field holds a reference to the TableColumn object that represents
     * the SQL column used by the ${field.name} field.
     * ${field.description!}
     */
    @DbObject("${field.name}")
    public static ${field.tableColumn}<${entity.name}<#if !field.isString>, ${field.javaType}</#if>> ${field.column?upper_case};

    </#list>
    <#list entity.fields as field>
    <#if field.isKey >
    @Key<#if field.isAutoIncrement>(autoIncrement = true)</#if>
    </#if>
    @Field(column = "${field.column}"<#if field.length != "">, length = ${field.length}</#if><#if field.isIndexed>, index = true</#if><#if field.sqlType != "">, type = JDBCType.${field.sqlType}</#if><#if field.adapter != "">, adapter = ${field.adapter}.class</#if>)
    private ${field.javaType} ${field.name};

    </#list>
    <#list entity.fields as field>
    /**
     * Gets the value of the ${field.name} field.
     * ${field.description!}
     * @return A ${field.javaType} object representing the value 
     * of the ${field.name} field.
     */
    public ${field.javaType} get${field.name?cap_first}()
    {
        return this.${field.name};
    }

    /**
     * Sets the value of the ${field.name} field.
     * ${field.description!}
     * @param ${field.name} The ${field.javaType} object representing the value 
     * of the ${field.name} field.
     */
    public void set${field.name?cap_first}(${field.javaType} ${field.name})
    {
        this.${field.name} = ${field.name};
    }

    </#list>
    @Override
    public int hashCode()
    {
        if(get${entity.keyField.name?cap_first}() == null)
        {
            return super.hashCode();
        }
        return get${entity.keyField.name?cap_first}().hashCode();
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
        final ${entity.name} other = (${entity.name}) obj;
        return Objects.equals(this.get${entity.keyField.name?cap_first}(), other.get${entity.keyField.name?cap_first}());
    }
}