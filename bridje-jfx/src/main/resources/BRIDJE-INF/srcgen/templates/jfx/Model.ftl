<#include "./ObjectUtils.ftl" />

package ${model.package};

<#list model.includes![] as inc>
import ${inc.fullName};
</#list>
import javafx.beans.property.*;
import javafx.collections.ObservableList;

public class ${model.name}
{
    <#list model.properties as property>
    private final ${property.propertyDec} ${property.name}Property = new ${property.propertyDimDec}();

    </#list>
    public ${model.name}()
    {
    }

    <#list model.properties as property>
    public ${property.propertyDec} ${property.name}Property()
    {
        return this.${property.name}Property;
    }

    public ${property.javaType} get${property.name?cap_first}()
    {
        return this.${property.name}Property.get();
    }

    public void set${property.name?cap_first}(${property.javaType} ${property.name})
    {
        this.${property.name}Property.set(${property.name});
    }

    </#list>
}