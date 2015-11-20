<#macro entityContent>
    <#if fields??>
    <#list fields as f>
    <#if f.transient>
    @XmlTransient
    <#elseif f.isList >
    @XmlElements(
    {
        <#list f.elements as e>
        @XmlElement(name = "${e.name}", type = ${e.type}.class)<#sep>, </#sep>
        </#list>
    })
    <#elseif f.access == "ATTRIBUTE" >
    @XmlAttribute
    </#if>
    private ${f.javaType} ${f.name}<#if !f.isNullable && f.defaultValue??> = ${f.defaultValueExp}</#if>;
    
    </#list>
    <#list fields as f>
    public ${f.javaType} get${f.name?cap_first}()
    {
        <#if f.isNullable && f.defaultValue??>
        if(this.${f.name} == null)
        {
            this.${f.name} = ${f.defaultValueExp};
        }
        </#if>
        return this.${f.name};
    }

    <#if !f.readonly>public </#if>void set${f.name?cap_first}(${f.javaType} ${f.name})
    {
        this.${f.name} = ${f.name};
    }

    </#list>
    </#if>
</#macro>