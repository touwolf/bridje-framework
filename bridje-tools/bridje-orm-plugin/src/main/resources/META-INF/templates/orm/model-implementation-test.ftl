<#import "orm-common.ftl" as common/>
<#assign dmName = dm.name />
<#assign dmEntitys = dm.entitys />

package ${dm.package}.impl;

import com.touwolf.bridje.ioc.Ioc;
import com.touwolf.bridje.ioc.IocContext; 
import com.touwolf.bridje.orm.impl.*;
import com.touwolf.bridje.orm.*; 
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import ${dm.package}.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ${dmName}Test
{
    public ${dmName}Test()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    private ${dmName}Impl dm;
    private DataSource ds;
    private DataContextImpl context;

    @Before
    public void setUp()
    {
        dm = (${dmName}Impl) Ioc.context().find(${dmName}.class);
        dm.fix();

        DataSourceManager dsm = Ioc.context().find(DataSourceManager.class);
        ds = dsm.getDefaultDataSource(${"\"" + dmName?cap_first + "\""});
        context = new DataContextImpl(ds, Ioc.context());
    }
    
    @After
    public void tearDown()
    {
    }

    @Test
    public void test${dmName?cap_first}() throws Exception
    {
        //test createNew${dmName}
        assertNotNull(dm.createNew${dmName}(ds));
         
        <#assign isInstanciatedEntity = {} />
        <#list dmEntitys as entity>
        //test query${entity.name?cap_first}
        assertNotNull(dm.query${entity.name?cap_first}());
        <#assign isInstanciatedEntity = isInstanciatedEntity + {entity.name : false} />

        </#list>
        <#list dmEntitys as entity>
        <#assign constructorsComment = "// tests contructors for entity " + entity.name?cap_first />
        <#assign pushComment = true />
        <#assign queryBuilder = "dm.query${entity.name?cap_first}()" />
        <#assign fieldCountMap = {} />
        <#list entity.fields as field>
        <#assign fieldCountMap = fieldCountMap + {field.name : 0} />
        </#list>
        <#list entity.constructors as construct>
        <#if pushComment>
        
        ${constructorsComment}
        <#assign pushComment = false />

        </#if>
        <#assign paramsName = [] />
	<#assign paramsValue = [] />
        <#list construct.fields as field>
        <#assign variableName =  field.name + entity.name?cap_first />
	<#assign fieldCountMap = fieldCountMap + {field.name : fieldCountMap[field.name] + 1} />
        <#assign paramsName = paramsName + [field.name] />
        <#if field.javaType == "String">
	<#assign paramsValue = paramsValue + [common.getParamValue(field, fieldCountMap[field.name], field.length)] />
        <#if field.visible>
        <#assign queryBuilder = queryBuilder + ".by" + "${field.name?cap_first}" + "(" + common.getParamValue(field, fieldCountMap[field.name], field.length) + ")" />
        </#if>
        </#if>
        <#if field.isNumeric>
        <#assign paramsValue = paramsValue + [common.getParamValue(field, fieldCountMap[field.name], 0)] />
        <#if field.visible>
        <#assign queryBuilder = queryBuilder + ".by" + "${field.name?cap_first}" + "(" + common.getParamValue(field, fieldCountMap[field.name], 0) + ")" />
        </#if>
        </#if>
        <#if field.isDate>
        <#if fieldCountMap[field.name] == 1>
        Date ${variableName} = new Date();
        </#if>
        <#assign paramsValue = paramsValue + ["new Date(" + variableName + ".getTime() + " + fieldCountMap[field.name] + ")"] />
        <#if field.visible>
        <#assign queryBuilder = queryBuilder + ".by" + "${field.name?cap_first}" + "(" + "new Date(" + variableName + ".getTime() + " + fieldCountMap[field.name] + ")" + ")" />
        </#if>
        </#if>
        <#if common.isEnumType(field) || field.objectJavaType == "Boolean">
        <#assign paramsValue = paramsValue + [common.getParamValue(field, 0, 0)] />
        <#if field.visible>
        <#assign queryBuilder = queryBuilder + ".by" + "${field.name?cap_first}" + "(" + common.getParamValue(field, 0, 0) + ")" />
        </#if>
        </#if>
	</#list>
        <#list construct.relatedEntitys as relEntity>
        <#assign relatedVariableName = relEntity.name?uncap_first />
        <#if !(isInstanciatedEntity[relEntity.name])>
        ${relEntity.name?cap_first} ${relatedVariableName} = new ${relEntity.name?cap_first}Impl(context);
        <#assign isInstanciatedEntity = isInstanciatedEntity + {relEntity.name : true} />
        <#list relEntity.updatableFields as field >
        <#if field.visible>
        <#if field.isDate>
        Date ${field.name + relEntity.name?cap_first}Related = new Date();
        ${common.setFieldValue(relatedVariableName, field, 1, field.name + relEntity.name?cap_first + "Related")}
        <#else>
        ${common.setFieldValue(relatedVariableName, field, 1, "")}
        </#if>
        </#if>
        </#list>
        ${relatedVariableName}.save();
        
        </#if>
        <#assign paramsName = paramsName + [relEntity.name] />
        <#assign paramsValue = paramsValue + [relatedVariableName] />
        </#list>
        // test add${entity.name?cap_first}(${paramsName?join(", ")})
        //assertNotNull(dm.add${entity.name?cap_first}(${paramsValue?join(", ")})); //TODO: Eliecer --> Esto ha de arreglarse luego
        //assertNotNull(${queryBuilder}.findOne());
        
        </#list>
        </#list>
        <#list dmEntitys as entity>
        <#assign variableName =  entity.name?uncap_first />
        <#assign filtersComment = "// tests filters for entity " + entity.name?cap_first />
        <#assign pushComment = true />
        <#list entity.filters as efilter>
        <#if pushComment>
        
        ${filtersComment}
        <#assign pushComment = false />

        </#if>
        // tests filter ${efilter.name}
        <#assign funcName = entity.name?cap_first+efilter.name!?cap_first />
        <#if !(isInstanciatedEntity[entity.name])>

        ${entity.name?cap_first} ${variableName} = new ${entity.name?cap_first}Impl(context);
        <#assign isInstanciatedEntity = isInstanciatedEntity + {entity.name : true} />
        <#list entity.updatableFields as field >
        <#if field.visible>
        <#if field.isDate>
        Date ${field.name + entity.name?cap_first}Filter = new Date();
        ${common.setFieldValue(variableName, field, 1, field.name + entity.name?cap_first + "Filter")}
        <#else>
        ${common.setFieldValue(variableName, field, 1, "")}
        </#if>
        </#if>
        </#list>
        </#if>
        <#assign paramsName = [] />
	<#assign paramsValue = [] />
        <#list efilter.params as p>
        <#if p.isRelationFilterParam()>

        <#assign paramsName = paramsName + [p.paramType] />
        <#assign paramsValue = paramsValue + [p.paramName] />
        <#assign relFilterEntity = dm.findEntityByName(p.paramType) />
        <#if !(isInstanciatedEntity[relFilterEntity.name])> 
        <#assign relatedVariableName = relFilterEntity.name?uncap_first />
        <#assign isInstanciatedEntity = isInstanciatedEntity + {relFilterEntity.name : true} />
        ${relFilterEntity.name?cap_first} ${relatedVariableName} = new ${relFilterEntity.name?cap_first}Impl(context);
        <#list relFilterEntity.updatableFields as field >
        <#if field.visible>
        <#if field.isDate>
        Date ${field.name + relFilterEntity.name?cap_first}Filter = new Date();
        ${common.setFieldValue(relatedVariableName, field, 1, field.name + relFilterEntity.name?cap_first + "Filter")}
        <#else>
        ${common.setFieldValue(relatedVariableName, field, 1, "")}
        </#if>
        </#if>
        </#list>
        ${relatedVariableName}.save();
        </#if>
        <#list entity.relations as relation >
        <#if relation.relatedEntity.name == relFilterEntity.name>
        <#list relation.fields as fieldsRelated> 
        <#if fieldsRelated.field.visible>
        ${entity.name?uncap_first}.set${fieldsRelated.field.name?cap_first}(${relFilterEntity.name?uncap_first}.get${fieldsRelated.relatedField.name?cap_first}());
        </#if>
        </#list>
        </#if>
        </#list>
        <#else>
        <#assign paramsName = paramsName + [p.paramName] />
        <#assign paramsValue = paramsValue + [variableName + ".get" + p.paramName?cap_first + "()"] />
        </#if>
        </#list>

        dm.query${entity.name?cap_first}().deleteAll();
        ${variableName}.save();

        <#if efilter.exist?? && efilter.exist>
        // test exist${funcName}(${paramsName?join(", ")})
        assertTrue(dm.exist${funcName}(${paramsValue?join(", ")}));

        </#if>
        <#if efilter.count?? && efilter.count>
        // test count${funcName}(${paramsName?join(", ")})
        assertTrue(dm.count${funcName}(${paramsValue?join(", ")}) == 1);

        </#if>
        <#if efilter.find?? && efilter.find>
        // test find${funcName}(${paramsName?join(", ")})
        assertFalse(dm.find${funcName}(${paramsValue?join(", ")}).isEmpty());

        </#if>
        <#if efilter.delete?? && efilter.delete>
        // test delete${funcName}(${paramsName?join(", ")})
        dm.delete${funcName}(${paramsValue?join(", ")});
        <#if efilter.exist?? && efilter.exist>
        assertFalse(dm.exist${funcName}(${paramsValue?join(", ")}));
        </#if>
        <#if efilter.count?? && efilter.count>
        assertTrue(dm.count${funcName}(${paramsValue?join(", ")}) == 0);
        </#if>
        <#if efilter.find?? && efilter.find>
        assertTrue(dm.find${funcName}(${paramsValue?join(", ")}).isEmpty());
        </#if>
        </#if>

        </#list>
        </#list>
    }
}