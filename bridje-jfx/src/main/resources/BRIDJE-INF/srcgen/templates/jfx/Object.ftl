<#include "./ObjectUtils.ftl" />

package ${object.package};

<#list model.includes![] as inc>
import ${inc.fullName};
</#list>
<#list object.includes![] as inc>
import ${inc.fullName};
</#list>
import javafx.beans.property.*;
import java.util.Objects;
import javafx.collections.ObservableList;

public class ${object.name}
{
    <#list object.allProperties as property>
    private final ${property.propertyDec} ${property.name}Property = new ${property.propertyDimDec}();

    </#list>
    public ${object.name}()
    {
    }

    <#list object.allProperties as property>
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
    @Override
    public int hashCode()
    {
        if(get${object.keyProperty.name?cap_first}() == null)
        {
            return super.hashCode();
        }
        return get${object.keyProperty.name?cap_first}().hashCode();
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
        final ${object.name} other = (${object.name}) obj;
        return Objects.equals(this.get${object.keyProperty.name?cap_first}(), other.get${object.keyProperty.name?cap_first}());
    }

    <#if object.toStringProperty??>
    @Override
    public String toString()
    {
        return get${object.toStringProperty.name?cap_first}();
    }

    </#if>
}