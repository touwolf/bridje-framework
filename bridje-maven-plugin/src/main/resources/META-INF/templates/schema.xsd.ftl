<#include "utils.ftl" />
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xs:schema attributeFormDefault="unqualified" elementFormDefault="qualified" version="1.0" targetNamespace="http://www.bridje.org/schemas/hirarchical" xmlns:tns="http://www.bridje.org/schemas/hirarchical" xmlns:xs="http://www.w3.org/2001/XMLSchema">

    <xs:element name="${name?uncap_first}" type="tns:${name?uncap_first}"/>

    <xs:complexType name="${name?uncap_first}">
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
        <xs:restriction base="xs:string">
            <#list enum.values as value>
            <xs:enumeration value="${value.name}"/>
            </#list>
        </xs:restriction>
    </xs:simpleType>

    </#list>
</xs:schema>