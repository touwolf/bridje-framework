<#include "utils.ftl" />
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xs:schema attributeFormDefault="unqualified" elementFormDefault="qualified" version="1.0" targetNamespace="http://www.bridje.org/schemas/hirarchical" xmlns:tns="http://www.bridje.org/schemas/hirarchical" xmlns:xs="http://www.w3.org/2001/XMLSchema">

    <#list entitys as entity>
    <xs:complexType name="${entity.name?uncap_first}">
        <xs:sequence>
          <xs:choice minOccurs="0" maxOccurs="unbounded">
            <xs:element name="string" type="tns:hStringField"/>
            <xs:element name="list" type="tns:hListField"/>
            <xs:element name="enum" type="tns:hEnumField"/>
            <xs:element name="boolean" type="tns:hBooleanField"/>
          </xs:choice>
        </xs:sequence>
        <xs:attribute name="name" type="xs:string"/>
        <xs:attribute name="extends" type="xs:string"/>
        <xs:attribute name="customizable" type="xs:boolean"/>
    </xs:complexType>
    </#list>

</xs:schema>