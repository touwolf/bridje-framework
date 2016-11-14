<#ftl encoding="UTF-8">

@XmlSchema(namespace = "${theme.namespace}",
        attributeFormDefault = XmlNsForm.UNQUALIFIED,
        elementFormDefault = XmlNsForm.QUALIFIED)
@DefaultTheme(name = "${theme.name}")
package ${theme.package};

import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
import org.bridje.web.view.DefaultTheme;
