<#macro entityContent>
    <#if fields??>
    <#list fields as f>
    <#if f.isTransient?? && f.isTransient>
    @XmlTransient
    <#elseif f.isList?? && f.isList >
    <#if f.wrapper?? && f.wrapper >
    @XmlElementWrapper(name = "${f.name}")
    </#if>
    @XmlElements(
    {
        <#list f.elements as e>
        @XmlElement(name = "${e.name}", type = ${e.type}.class)<#sep>, </#sep>
        </#list>
    })
    <#elseif f.access?? && f.access == "ATTRIBUTE" >
    @XmlAttribute
    </#if>
    private ${f.javaType!""} ${f.name}<#if f.isNullable?? && !f.isNullable && f.defaultValue??> = ${f.defaultValueExp}</#if>;
    
    </#list>
    <#list fields as f>
    /**
     * ${f.description!""}
     * @return A ${f.javaType?html} object representing the value of the ${f.name} field.
     */
    public ${f.javaType!""} get${f.name?cap_first}()
    {
        <#if f.isNullable && f.defaultValue??>
        if(this.${f.name} == null)
        {
            this.${f.name} = ${f.defaultValueExp};
        }
        </#if>
        return this.${f.name};
    }

    /**
     * ${f.description!""}
     * @param ${f.name} The new value for the ${f.name} field.
     */
    <#if !f.readonly>public </#if>void set${f.name?cap_first}(${f.javaType} ${f.name})
    {
        this.${f.name} = ${f.name};
    }

    </#list>
    </#if>
</#macro>

<#macro parentCode>
    <#if parent??>
    @XmlTransient
    private ${parent.type} ${parent.name};

    /**
     * The ${parent.type?html} object representing the parent of this object.
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
    public void afterUnmarshal(Unmarshaller unmarshaller, Object parent)
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