<#include "./ObjectUtils.ftl" />

package ${model.package};

<#list model.includes![] as inc>
import ${inc.fullName};
</#list>
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import org.bridje.jfx.utils.JfxUtils;

public class ${model.name}
{
    <#list model.icons as icon>
    public static ImageView ${icon.name}(int size)
    {
        return JfxUtils.loadImage(${model.name}.class, "${icon.file}", size, size);
    }

    </#list>

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