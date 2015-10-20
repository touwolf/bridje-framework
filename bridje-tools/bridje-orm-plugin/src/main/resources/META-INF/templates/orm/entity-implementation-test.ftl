<#import "orm-common.ftl" as common/>
<#assign dmName = dm.name />
<#assign entityName = entity.name />
<#assign queryEntityName =  "dm.query" + entityName?cap_first />
<#assign updatableFields = entity.updatableFields />

package ${dm.package}.impl;

import com.touwolf.bridje.ioc.Ioc;
import com.touwolf.bridje.orm.*;
import com.touwolf.bridje.orm.impl.*;
import com.touwolf.bridje.orm.exceptions.*;
import ${dm.package}.*;
import static com.touwolf.bridje.orm.DataUtils.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Date;
import org.junit.*;
import javax.sql.DataSource;
import static org.junit.Assert.*;

public class ${entityName}Test
{

    private ${dmName}Impl dm;

    private DataContextImpl context;

    public ${entityName}Test()
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

    @Before
    public void setUp() throws QueryException
    {
        dm = (${dmName}Impl) Ioc.context().find(${dmName}.class);
        dm.fix();
        ${queryEntityName}().deleteAll();

        DataSourceManager dsm = Ioc.context().find(DataSourceManager.class);
        DataSource ds = dsm.getDefaultDataSource(${"\"" + dmName?cap_first + "\""});
        context = new DataContextImpl(ds, Ioc.context());
    }
    
    @After
    public void tearDown()
    {
    }

    @Test
    public void testEntity${entityName?cap_first}() throws Exception
    {
        <#assign variableName =  entityName?uncap_first />
        ${entityName?cap_first}Impl ${variableName} = new ${entityName?cap_first}Impl(context);
        <#list updatableFields as field >
        <#if field.visible>
        <#if field.isDate>
        Date ${field.name}Value = new Date();
        ${common.setFieldValue(variableName, field, 1, common.getParamValue(field, 0, 0))}
        <#else>
        ${common.setFieldValue(variableName, field, 1, "")}
        </#if>
        </#if>
        </#list>
       
        //test exists
        assertFalse(${variableName}.exists());

        //test save
        ${variableName}.save();
        <#list updatableFields as field >
        <#if field.visible>
        <#if field.javaType == "String">
        assertEquals(${queryEntityName}().by${field.name?cap_first}(${common.getParamValue(field, 1, field.length)}).findOne().get${field.name?cap_first}(), ${common.getParamValue(field, 1, field.length)});
        </#if>
        <#if field.isNumeric>
        assertTrue(${queryEntityName}().by${field.name?cap_first}(${common.getParamValue(field, 1, 0)}).findOne().get${field.name?cap_first}() == ${common.getParamValue(field, 1, 0)});
        </#if>
        <#if field.isDate>
        assertEquals(${queryEntityName}().by${field.name?cap_first}(${common.getParamValue(field, 0, 0)}).findOne().get${field.name?cap_first}().getTime(), ${common.getParamValue(field, 0, 0)}.getTime());
        </#if>
        <#if common.isEnumType(field) || field.objectJavaType == "Boolean">
        assertEquals(${queryEntityName}().by${field.name?cap_first}(${common.getParamValue(field, 0, 0)}).findOne().get${field.name?cap_first}(), ${common.getParamValue(field, 0, 0)});
        </#if>
        </#if>
        </#list>
        assertTrue(${variableName}.exists()); 

        //establece nuevos datos antes del rollback
        <#list updatableFields as field >
        <#if field.visible>
        <#if field.javaType == "String">
        ${variableName}.set${field.name?cap_first}(${common.getParamValue(field, 2, field.length)});
        </#if>
        <#if field.isNumeric>
        ${variableName}.set${field.name?cap_first}(${common.getParamValue(field, 2, 0)});
        </#if>
        <#if field.isDate>
        ${variableName}.set${field.name?cap_first}(new Date(${common.getParamValue(field, 0, 0)}.getTime() + (24l * 60l * 60l * 1000l)));
        </#if>
        <#if field.objectJavaType == "Boolean">
        ${variableName}.set${field.name?cap_first}(false);
        </#if>
        <#if common.isEnumType(field)>
        ${variableName}.set${field.name?cap_first}(null);
        </#if>
        </#if>
        </#list>
        ${variableName}.refresh();

        //comprobamos que esos datos nuevos datos no se guardaron
        <#list updatableFields as field >
        <#if field.visible>
        <#if field.javaType == "String">
        assertEquals(${variableName}.get${field.name?cap_first}(), ${common.getParamValue(field, 1, field.length)});
        </#if>
        <#if field.isNumeric>
        assertTrue(${variableName}.get${field.name?cap_first}() == ${common.getParamValue(field, 1, 0)});
        </#if>
        <#if field.isDate>
        assertEquals(${variableName}.get${field.name?cap_first}().getTime(), ${common.getParamValue(field, 0, 0)}.getTime());
        </#if>
        <#if common.isEnumType(field) || field.objectJavaType == "Boolean">
        assertEquals(${variableName}.get${field.name?cap_first}(), ${common.getParamValue(field, 0, 0)});
        </#if>
        </#if>
        </#list>

        //test delete
        ${variableName}.delete();
        assertFalse(${variableName}.exists());
        assertNull(${queryEntityName}().findOne()); 
    }
}