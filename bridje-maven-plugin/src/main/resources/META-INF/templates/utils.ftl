<#macro fieldsDeclaration fields>
    <#list fields as f>
    <#if f.isTransient?? && f.isTransient>
    @javax.xml.bind.annotation.XmlTransient
    <#elseif f.isList?? && f.isList >
    <#if f.wrapper?? && f.wrapper >
    @javax.xml.bind.annotation.XmlElementWrapper(name = "${f.name}")
    </#if>
    @javax.xml.bind.annotation.XmlElements(
    {
        <#list f.elements as e>
        @javax.xml.bind.annotation.XmlElement(name = "${e.name}", type = ${e.type}.class)<#sep>, </#sep>
        </#list>
    })
    <#elseif f.access?? && f.access == "ATTRIBUTE" >
    @javax.xml.bind.annotation.XmlAttribute
    </#if>
    private ${f.javaType!""} ${f.name}<#if f.isNullable?? && !f.isNullable && f.defaultValue??> = ${f.defaultValueExp}</#if>;
    
    </#list>
</#macro>

<#macro fieldsGetterSetters fields>
    <#list fields as f>
    /**
     * ${f.description!""}
     * @return A ${f.javaType?html} object representing the value of the ${f.name} field.
     */
    public ${f.javaType!""} get${f.name?cap_first}()
    {
        <#if f.isList?? && f.isList>
        if(this.${f.name} == null)
        {
            return null;
        }
        return java.util.Collections.unmodifiableList(this.${f.name});
        <#else>
        <#if f.isNullable && f.defaultValue??>
        if(this.${f.name} == null)
        {
            this.${f.name} = ${f.defaultValueExp};
        }
        </#if>
        return this.${f.name};
        </#if>
    }

    <#if !(f.isList?? && f.isList)>
    /**
     * ${f.description!""}
     * @param ${f.name} The new value for the ${f.name} field.
     */
    <#if !f.readonly>public </#if>void set${f.name?cap_first}(${f.javaType} ${f.name})
    {
        this.${f.name} = ${f.name};
    }

    </#if>
    </#list>
</#macro>

<#macro listFieldsAddRemoveMethods fields>
    <#list fields as f>
    <#if f.isList?? && f.isList >
    <#if f.elements??>
    <#list f.elements as e>
    /**
     * Adds a ${e.type} object to the ${f.name} list.
     * @param value The object to be added
     */
    <#if !f.readonly>public </#if>void add${e.name?cap_first}(${e.type} value)
    {
        if(value == null)
        {
            return;
        }
        if(this.${f.name} == null)
        {
            this.${f.name} = new java.util.ArrayList<>();
        }
        this.${f.name}.add(value);
    }

    /**
     * Removes a ${e.type} object from the ${f.name} list.
     * @param value The object to be removed.
     */
    <#if !f.readonly>public </#if>void remove${e.name?cap_first}(${e.type} value)
    {
        if(value == null)
        {
            return;
        }
        if(this.${f.name} == null)
        {
            return;
        }
        this.${f.name}.remove(value);
    }

    </#list>
    </#if>
    <#if f.filters??>
    <#list f.filters as ft>
    public ${f.of} find${ft.name}(<#list ft.fields as ftf>${(f.entity.model.findEntity(f.of).findField(ftf.field).javaType)!"Object"} ${ftf.field}<#sep>, </#sep></#list>)
    {
        for (${f.of} e : get${f.name?cap_first}())
        {
            if(<#list ft.fields as ftf>e.get${ftf.field?cap_first}().equals(${ftf.field})<#sep> && </#sep></#list>)
            {
                return e;
            }
        }
        return null;
    }

    </#list>
    </#if>
    </#if>
    </#list>
</#macro>

<#macro entityContent>
    <#if fields??>
    <@fieldsDeclaration fields />
    <@fieldsGetterSetters fields />
    <@listFieldsAddRemoveMethods fields />
    </#if>
</#macro>

<#macro parentCode>
    <#if parent??>
    @javax.xml.bind.annotation.XmlTransient
    private ${parent.type} ${parent.name};

    /**
     * The ${parent.type?html} parent of this object.
     * @return The ${parent.type?html} object representing the parent of this object.
     */
    public ${parent.type} get${parent.name?cap_first}()
    {
        return this.${parent.name};
    }

    /**
     * The ${parent.type?html} object representing the parent of this object.
     */
    void set${parent.name?cap_first}(${parent.type} ${parent.name})
    {
        this.${parent.name} = ${parent.name};
    }

    /**
     * This method is call by the JAXB Unmarshaller after the object's creation.
     * @param unmarshaller The unmarshaller object being used.
     * @param parent The parent object for this object.
     */
    public void afterUnmarshal(javax.xml.bind.Unmarshaller unmarshaller, Object parent)
    {
        if(parent instanceof ${parent.type})
        {
            set${parent.name?cap_first}((${parent.type})parent);
        }
    }

    </#if>
</#macro>

<#macro xmlFields fields indent=0>
    <#local spc>${""?left_pad(indent * 4)}</#local>
        ${spc}<xs:sequence>
    <#list fields as field>
    <#if field.isList>
        <#if field.wrapper>
            ${spc}<xs:element name="${field.name}" minOccurs="0">
                <#if field.description??>
                ${spc}<xs:annotation>
                    ${spc}<xs:documentation>
                        ${spc}${field.description?trim}
                    ${spc}</xs:documentation>
                ${spc}</xs:annotation>
                </#if>
                ${spc}<xs:complexType>
                    ${spc}<xs:sequence>
                        <#list field.elements![] as element >
                        ${spc}<xs:element name="${element.name}" type="tns:${element.type?uncap_first}" minOccurs="0" maxOccurs="unbounded">
                            <#if element.description??>
                            ${spc}<xs:annotation>
                                ${spc}<xs:documentation>
                                    ${spc}${element.description?trim}
                                ${spc}</xs:documentation>
                            ${spc}</xs:annotation>
                            </#if>
                        ${spc}</xs:element>
                        </#list>
                    ${spc}</xs:sequence>
                ${spc}</xs:complexType>
            ${spc}</xs:element>
        <#else>
            ${spc}<xs:choice minOccurs="0" maxOccurs="unbounded">
                <#if field.description??>
                ${spc}<xs:annotation>
                    ${spc}<xs:documentation>
                        ${spc}${field.description?trim}
                    ${spc}</xs:documentation>
                ${spc}</xs:annotation>
                </#if>
                <#list field.elements![] as element >
                ${spc}<xs:element name="${element.name}" type="tns:${element.type?uncap_first}">
                    <#if element.description??>
                    ${spc}<xs:annotation>
                        ${spc}<xs:documentation>
                            ${spc}${element.description?trim}
                        ${spc}</xs:documentation>
                    ${spc}</xs:annotation>
                    </#if>
                ${spc}</xs:element>
                </#list>
            ${spc}</xs:choice>
        </#if>
    <#elseif !(field.access??) || field.access == "ELEMENT">
            ${spc}<xs:element name="${field.name}" type="tns:${field.xmlType?uncap_first}" minOccurs="0" maxOccurs="1">
                <#if field.description??>
                ${spc}<xs:annotation>
                    ${spc}<xs:documentation>
                        ${spc}${field.description?trim}
                    ${spc}</xs:documentation>
                ${spc}</xs:annotation>
                </#if>
            ${spc}</xs:element>
    </#if>
    </#list>
        ${spc}</xs:sequence>
    <#list fields as field>
    <#if field.access?? && field.access == "ATTRIBUTE">
        ${spc}<xs:attribute name="${field.name}" type="xs:${field.xmlType}">
            <#if field.description??>
            ${spc}<xs:annotation>
                ${spc}<xs:documentation>
                    ${spc}${field.description?trim}
                ${spc}</xs:documentation>
            ${spc}</xs:annotation>
            </#if>
        ${spc}</xs:attribute>
    </#if>
    </#list>
</#macro>

<#macro javaDocContent value trim=false>
<#list value?split("\n") as l>
<#if trim>
 * ${l?trim}
<#else>
 * ${l}
</#if>
</#list>
</#macro>