<#--- Contenedor de llaves multiples -->
<#include "orm-common.ftl">
package ${dm.package};

/**
* Llaves primarias de ${entity.description!(entity.name)}
*/
public class ${entityKey}
{
<@renderFieldsDefinitions fields = entity.keyFields/>
<@renderFieldsFunctions fields = entity.keyFields override = false/>
}