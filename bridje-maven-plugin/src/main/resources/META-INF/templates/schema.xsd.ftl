<#include "utils.ftl" />
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xs:schema attributeFormDefault="unqualified" elementFormDefault="qualified" version="1.0" targetNamespace="${namespace}" xmlns:tns="${namespace}" xmlns:xs="http://www.w3.org/2001/XMLSchema">

    <xs:element name="${name?lower_case}" type="tns:${name?uncap_first}"/>

    <xs:complexType name="${name?uncap_first}">
        <#if description??>
        <xs:annotation>
            <xs:documentation>
                ${description?trim}
            </xs:documentation>
        </xs:annotation>
        </#if>
        <#if extendsFrom??>
        <xs:complexContent>
            <xs:extension base="tns:${extendsFrom?uncap_first}">
                <@xmlFields fields=fields![] indent=2 />
            </xs:extension>
        </xs:complexContent>
        <#else>
        <@xmlFields fields![] />
        </#if>
    </xs:complexType>

    <#list entitys as entity>
    <xs:complexType name="${entity.name?uncap_first}">
        <#if entity.description??>
        <xs:annotation>
            <xs:documentation>
                ${entity.description?trim}
            </xs:documentation>
        </xs:annotation>
        </#if>
        <#if entity.extendsFrom??>
        <xs:complexContent>
            <xs:extension base="tns:${entity.extendsFrom?uncap_first}">
                <@xmlFields fields=entity.fields![] indent=2 />
            </xs:extension>
        </xs:complexContent>
        <#else>
        <@xmlFields entity.fields![] />
        </#if>
    </xs:complexType>

    </#list>
    <#list enums![] as enum>
    <xs:simpleType name="${enum.name?uncap_first}">
        <#if enum.description??>
        <xs:annotation>
            <xs:documentation>
                ${enum.description?trim}
            </xs:documentation>
        </xs:annotation>
        </#if>
        <xs:restriction base="xs:string">
            <#list enum.values as value>
            <xs:enumeration value="${value.name}">
                <#if value.description??>
                <xs:annotation>
                    <xs:documentation>
                        ${value.description?trim}
                    </xs:documentation>
                </xs:annotation>
                </#if>
            </xs:enumeration>
            </#list>
        </xs:restriction>
    </xs:simpleType>

    </#list>
</xs:schema>