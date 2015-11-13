
package ${model.package};

/**
 * 
 */
public interface ${name}
{
    <#list fields as f>
    ${f.javaType} get${f.name?cap_first}();
    </#list>
}