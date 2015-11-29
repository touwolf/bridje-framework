<#include "utils.ftl" />
/**
 <@javaDocContent license!"" />
 */

@XmlSchema(namespace = "${namespace}",
        attributeFormDefault = XmlNsForm.UNQUALIFIED,
        elementFormDefault = XmlNsForm.QUALIFIED)
package ${packageName};

import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
