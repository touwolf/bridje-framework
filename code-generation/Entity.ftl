 
package ${doc.model.@package};

import org.bridje.orm.Entity;
import org.bridje.orm.Field;

@Entity(table = "${node.@name?uncap_first}")
public class ${node.@name}
{
    <#list node.* as field>
    @Field
    private ${field?node_name?cap_first} ${field.@name};

    </#list>
}