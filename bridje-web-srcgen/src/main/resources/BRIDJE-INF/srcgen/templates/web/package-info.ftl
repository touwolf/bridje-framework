<#ftl encoding="UTF-8">

@XmlSchema(namespace = "${uisuite.namespace}",
        attributeFormDefault = XmlNsForm.UNQUALIFIED,
        elementFormDefault = XmlNsForm.QUALIFIED)
@DefaultTheme(name = "${uisuite.name?lower_case}")
package ${uisuite.package};

import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import org.bridje.web.view.DefaultTheme;
