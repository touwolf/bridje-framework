 
package ${doc.model.@package};

import org.bridje.orm.Entity;
import org.bridje.orm.Field;
import org.bridje.orm.DbObject;
import org.bridje.orm.TableColumn;

@Entity(table = "${node.@name?uncap_first}")
public class ${node.@name}
{
    <#list node.* as field>
    @DbObject("${field.@name}")
    private TableColumn<${field?node_name?cap_first}, ${field?node_name?cap_first}> ${field.@name?upper_case};

    </#list>
    <#list node.* as field>
    @Field
    private ${field?node_name?cap_first} ${field.@name};

    </#list>
    <#list node.* as field>
    private ${field?node_name?cap_first} get${field.@name?cap_first}()
    {
        return this.${field.@name};
    }

    private void set${field.@name?cap_first}(${field?node_name?cap_first} ${field.@name})
    {
        this.${field.@name} = ${field.@name};
    }

    </#list>
}