<#macro entityContent>
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
    private ${f.javaType} ${f.name};
    
    </#list>
    <#list fields as f>
    public ${f.javaType} get${f.name?cap_first}()
    {
        return this.${f.name};
    }

    <#if !f.readonly>public </#if>void set${f.name?cap_first}(${f.javaType} ${f.name})
    {
        this.${f.name} = ${f.name};
    }

    </#list>
</#macro>