<#import "orm-common.ftl" as common/>
<#assign dmName = dm.name />
<#assign entityName = entity.name />
<#assign queryEntityName =  "dm.query" + entityName?cap_first />
<#assign updatableFields = entity.updatableFields />
<#assign allFields = entity.fields />

package ${dm.package}.impl;

import com.touwolf.bridje.ioc.Ioc;
import com.touwolf.bridje.orm.exceptions.*;
import com.touwolf.bridje.orm.*;
import com.touwolf.bridje.orm.impl.*;
import ${dm.package}.*; 
import java.util.List;
import java.util.LinkedList; 
import java.util.Date;
import org.junit.*;
import javax.sql.DataSource;
import static org.junit.Assert.*;

public class ${entityName}QueryTest
{
    
    private ${dmName}Impl dm;
    private DataContextImpl context;

    public ${entityName}QueryTest()
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
    public void setUp() throws QueryException, FieldException
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
    public void test${entityName?cap_first}Querys() throws Exception
    {
        //Primera instancia de ${entityName}
        <#assign entityName1 = entityName?uncap_first + "1"/>
        ${entityName?cap_first} ${entityName1} = new ${entityName?cap_first}Impl(context);
        <#list updatableFields as field >
        <#assign fieldName =  field.name?cap_first />
        <#if field.visible>
        <#if field.isDate>
        Date ${field.name}Value = new Date();
        ${common.setFieldValue(entityName1, field, 1, common.getParamValue(field, 0, 0))}
        <#else>
        ${common.setFieldValue(entityName1, field, 1, "")}
        </#if>
        </#if>
        </#list>
        ${entityName1}.save();

        //Segunda instancia de ${entityName}
        <#assign entityName2 = entityName?uncap_first + "2"/>
        ${entityName?cap_first} ${entityName2} = new ${entityName?cap_first}Impl(context);
        <#list updatableFields as field >
        <#assign fieldName =  field.name?cap_first />
        <#if field.visible>
        <#if field.isDate>
        ${field.name}Value = new Date(${common.getParamValue(field, 0, 0)}.getTime() + (24l * 60l * 60l * 1000l));
        ${common.setFieldValue(entityName2, field, 2, common.getParamValue(field, 0, 0))}
        <#else>
        ${common.setFieldValue(entityName2, field, 2, "")}
        </#if>
        </#if>
        </#list>

        <#assign fieldTestCount = 0/>
        <#list allFields as field>
        <#assign fieldName =  field.name?cap_first />
        <#assign fieldTestCount = fieldTestCount + 1/>
        <#if fieldTestCount != 1>

        </#if>
	<#if field.visible>
        //field ${fieldName}
        <#assign getFieldValue1 = entityName1 + ".get" + fieldName + "()" />
        <#assign getFieldValue2 = entityName2 + ".get" + fieldName + "()" />
        <#if field.javaType == "String">
        //test by${fieldName}
	assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}(${getFieldValue1}).findOne().get${fieldName}(), ${common.getFieldCast(field)}${getFieldValue1});
	assertNotNull(${queryEntityName}().by${fieldName}(${getFieldValue1}).find().size());
        //test by${fieldName}Not
        assertNull(${queryEntityName}().by${fieldName}Not(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}Not(${getFieldValue1}).find().isEmpty());
        //test or${fieldName}
        assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}(${getFieldValue1}).findOne().get${fieldName}(), ${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}(${getFieldValue1}).find().size() == 1);
        //test or${fieldName}Not
        assertNull(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Not(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Not(${getFieldValue1}).find().isEmpty());
        //test by${fieldName}Like
        assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}Like("%" + ${getFieldValue1} + "%").findOne().get${fieldName}(), ${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}Like("%" + ${getFieldValue1} + "%").find().size() == 1);
        //test by${fieldName}NotLike
        assertNull(${queryEntityName}().by${fieldName}NotLike("%" + ${getFieldValue1} + "%").findOne());
	assertTrue(${queryEntityName}().by${fieldName}NotLike("%" + ${getFieldValue1} + "%").find().isEmpty());
        //test or${fieldName}Like
	assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Like("%" + ${getFieldValue1} + "%").findOne().get${fieldName}(), ${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Like("%" + ${getFieldValue1} + "%").find().size() == 1);
        //test or${fieldName}NotLike
        assertNull(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotLike("%" + ${getFieldValue1} + "%").findOne());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotLike("%" + ${getFieldValue1} + "%").find().isEmpty());
        </#if>
        <#if field.isNumeric>
        <#if field.required>
        //test by${fieldName}
	assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}(${getFieldValue1}).findOne().get${fieldName}(), ${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue1}).find().size() == 1);
        //test by${fieldName}Not
        assertNull(${queryEntityName}().by${fieldName}Not(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}Not(${getFieldValue1}).find().isEmpty());
        //test or${fieldName}
        assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}(${getFieldValue1}).findOne().get${fieldName}(), ${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}(${getFieldValue1}).find().size() == 1);
        //test or${fieldName}Not
        assertNull(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Not(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Not(${getFieldValue1}).find().isEmpty());
	//test by${fieldName}Ge
	assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}Ge(${getFieldValue1}).findOne().get${fieldName}(), ${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}Ge(${getFieldValue1}).find().size() == 1);
        //test by${fieldName}NotGe
        assertNull(${queryEntityName}().by${fieldName}NotGe(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}NotGe(${getFieldValue1}).find().isEmpty());
	//test by${fieldName}Gt
	assertNull(${queryEntityName}().by${fieldName}Gt(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}Gt(${getFieldValue1}).find().isEmpty());
        //test by${fieldName}NotGt
        assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}Ge(${getFieldValue1}).findOne().get${fieldName}(), ${common.getFieldCast(field)}${getFieldValue1} );
	assertTrue(${queryEntityName}().by${fieldName}NotGt(${getFieldValue1}).find().size() == 1);
	//test by${fieldName}Le
	assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}Le(${getFieldValue1}).findOne().get${fieldName}(), ${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}Le(${getFieldValue1}).find().size() == 1);
        //test by${fieldName}NotLe
        assertNull(${queryEntityName}().by${fieldName}NotLe(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}NotLe(${getFieldValue1}).find().isEmpty());
	//test by${fieldName}Lt
	assertNull(${queryEntityName}().by${fieldName}Lt(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}Lt(${getFieldValue1}).find().isEmpty());
        //test by${fieldName}NotLt
        assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}NotLt(${getFieldValue1}).findOne().get${fieldName}(),${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}NotLt(${getFieldValue1}).find().size() == 1);
        //test or${fieldName}Ge
	assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Ge(${getFieldValue1}).findOne().get${fieldName}(),${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Ge(${getFieldValue1}).find().size() == 1);
        //test or${fieldName}NotGe
        assertNull(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotGe(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotGe(${getFieldValue1}).find().isEmpty());
	//test or${fieldName}Gt
	assertNull(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Gt(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Gt(${getFieldValue1}).find().isEmpty());
        //test or${fieldName}NotGt
        assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotGt(${getFieldValue1}).findOne().get${fieldName}(), ${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotGt(${getFieldValue1}).find().size() == 1);
	//test or${fieldName}Le
	assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Le(${getFieldValue1}).findOne().get${fieldName}(), ${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Le(${getFieldValue1}).find().size() == 1);
        //test or${fieldName}NotLe
        assertNull(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotLe(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotLe(${getFieldValue1}).find().isEmpty());
	//test or${fieldName}Lt
	assertNull(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Lt(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Lt(${getFieldValue1}).find().isEmpty());
        //test or${fieldName}NotLt
        assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotLt(${getFieldValue1}).findOne().get${fieldName}(), ${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotLt(${getFieldValue1}).find().size() == 1);
        <#else>
        //test by${fieldName}
	assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}(${getFieldValue1}).findOne().get${fieldName}(), ${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue1}).find().size() == 1);
        //test by${fieldName}Not
        assertNull(${queryEntityName}().by${fieldName}Not(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}Not(${getFieldValue1}).find().isEmpty());
        //test or${fieldName}
        assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}(${getFieldValue1}).findOne().get${fieldName}(),${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}(${getFieldValue1}).find().size() == 1);
        //test or${fieldName}Not
        assertNull(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Not(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Not(${getFieldValue1}).find().isEmpty());
	//test by${fieldName}Ge
	assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}Ge(${getFieldValue1}).findOne().get${fieldName}(),${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}Ge(${getFieldValue1}).find().size() == 1);
        //test by${fieldName}NotGe
        assertNull(${queryEntityName}().by${fieldName}NotGe(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}NotGe(${getFieldValue1}).find().isEmpty());
	//test by${fieldName}Gt
	assertNull(${queryEntityName}().by${fieldName}Gt(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}Gt(${getFieldValue1}).find().isEmpty());
        //test by${fieldName}NotGt
        assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}NotGt(${getFieldValue1}).findOne().get${fieldName}(), ${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}NotGt(${getFieldValue1}).find().size() == 1);
	//test by${fieldName}Le
	assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}Le(${getFieldValue1}).findOne().get${fieldName}(), ${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}Le(${getFieldValue1}).find().size() == 1);
        //test by${fieldName}NotLe
        assertNull(${queryEntityName}().by${fieldName}NotLe(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}NotLe(${getFieldValue1}).find().isEmpty());
	//test by${fieldName}Lt
	assertNull(${queryEntityName}().by${fieldName}Lt(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}Lt(${getFieldValue1}).find().isEmpty());
        //test by${fieldName}NotLt
        assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}NotLt(${getFieldValue1}).findOne().get${fieldName}(), ${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}NotLt(${getFieldValue1}).find().size() == 1);
        //test or${fieldName}Ge
	assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Ge(${getFieldValue1}).findOne().get${fieldName}(), ${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Ge(${getFieldValue1}).find().size() == 1);
        //test or${fieldName}NotGe
        assertNull(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotGe(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotGe(${getFieldValue1}).find().isEmpty());
	//test or${fieldName}Gt
	assertNull(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Gt(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Gt(${getFieldValue1}).find().isEmpty());
        //test or${fieldName}NotGt
        assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotGt(${getFieldValue1}).findOne().get${fieldName}(),${common.getFieldCast(field)} ${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotGt(${getFieldValue1}).find().size() == 1);
	//test or${fieldName}Le
	assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Le(${getFieldValue1}).findOne().get${fieldName}(), ${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Le(${getFieldValue1}).find().size() == 1);
        //test or${fieldName}NotLe
        assertNull(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotLe(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotLe(${getFieldValue1}).find().isEmpty());
	//test or${fieldName}Lt
	assertNull(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Lt(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Lt(${getFieldValue1}).find().isEmpty());
        //test or${fieldName}NotLt
        assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotLt(${getFieldValue1}).findOne().get${fieldName}(), ${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotLt(${getFieldValue1}).find().size() == 1);
        </#if>
        </#if>
        <#if field.isDate>
        //test by${fieldName}
	assertEquals(${queryEntityName}().by${fieldName}(${getFieldValue1}).findOne().get${fieldName}().getTime(),${getFieldValue1}.getTime());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue1}).find().size() == 1);
        //test by${fieldName}Not
        assertNull(${queryEntityName}().by${fieldName}Not(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}Not(${getFieldValue1}).find().isEmpty());
        //test or${fieldName}
        assertEquals(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}(${getFieldValue1}).findOne().get${fieldName}().getTime(), ${getFieldValue1}.getTime());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}(${getFieldValue1}).find().size() == 1);
        //test or${fieldName}Not
        assertNull(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Not(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Not(${getFieldValue1}).find().isEmpty());
	//test by${fieldName}Ge
	assertEquals(${queryEntityName}().by${fieldName}Ge(${getFieldValue1}).findOne().get${fieldName}().getTime(), ${getFieldValue1}.getTime());
	assertTrue(${queryEntityName}().by${fieldName}Ge(${getFieldValue1}).find().size() == 1);
        //test by${fieldName}NotGe
        assertNull(${queryEntityName}().by${fieldName}NotGe(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}NotGe(${getFieldValue1}).find().isEmpty());
	//test by${fieldName}Gt
	assertNull(${queryEntityName}().by${fieldName}Gt(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}Gt(${getFieldValue1}).find().isEmpty());
        //test by${fieldName}NotGt
        assertEquals(${queryEntityName}().by${fieldName}NotGt(${getFieldValue1}).findOne().get${fieldName}().getTime(), ${getFieldValue1}.getTime());
	assertTrue(${queryEntityName}().by${fieldName}NotGt(${getFieldValue1}).find().size() == 1);
	//test by${fieldName}Le
	assertEquals(${queryEntityName}().by${fieldName}Le(${getFieldValue1}).findOne().get${fieldName}().getTime(), ${getFieldValue1}.getTime());
	assertTrue(${queryEntityName}().by${fieldName}Le(${getFieldValue1}).find().size() == 1);
        //test by${fieldName}NotLe
        assertNull(${queryEntityName}().by${fieldName}NotLe(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}NotLe(${getFieldValue1}).find().isEmpty());
	//test by${fieldName}Lt
	assertNull(${queryEntityName}().by${fieldName}Lt(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}Lt(${getFieldValue1}).find().isEmpty());
        //test by${fieldName}NotLt
        assertEquals(${queryEntityName}().by${fieldName}NotLt(${getFieldValue1}).findOne().get${fieldName}().getTime(), ${getFieldValue1}.getTime());
	assertTrue(${queryEntityName}().by${fieldName}NotLt(${getFieldValue1}).find().size() == 1);
        //test or${fieldName}Ge
	assertEquals(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Ge(${getFieldValue1}).findOne().get${fieldName}().getTime(),${getFieldValue1}.getTime());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Ge(${getFieldValue1}).find().size() == 1);
        //test or${fieldName}NotGe
        assertNull(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotGe(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotGe(${getFieldValue1}).find().isEmpty());
	//test or${fieldName}Gt
	assertNull(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Gt(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Gt(${getFieldValue1}).find().isEmpty());
        //test or${fieldName}NotGt
        assertEquals(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotGt(${getFieldValue1}).findOne().get${fieldName}().getTime(),${getFieldValue1}.getTime());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotGt(${getFieldValue1}).find().size() == 1);
	//test or${fieldName}Le
	assertEquals(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Le(${getFieldValue1}).findOne().get${fieldName}().getTime(),${getFieldValue1}.getTime());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Le(${getFieldValue1}).find().size() == 1);
        //test or${fieldName}NotLe
        assertNull(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotLe(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotLe(${getFieldValue1}).find().isEmpty());
	//test or${fieldName}Lt
	assertNull(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Lt(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Lt(${getFieldValue1}).find().isEmpty());
        //test or${fieldName}NotLt
        assertEquals(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotLt(${getFieldValue1}).findOne().get${fieldName}().getTime(),${getFieldValue1}.getTime());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}NotLt(${getFieldValue1}).find().size() == 1);
        </#if>
        <#if common.isEnumType(field)>
        //test by${fieldName}
	assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}(${getFieldValue1}).findOne().get${fieldName}(),${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue1}).find().size() == 1);
        //test by${fieldName}Not
        assertNull(${queryEntityName}().by${fieldName}Not(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}Not(${getFieldValue1}).find().isEmpty());
        //test or${fieldName}
        assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}(${getFieldValue1}).findOne().get${fieldName}(),${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}(${getFieldValue1}).find().size() == 1);
        //test or${fieldName}Not
        assertNull(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Not(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Not(${getFieldValue1}).find().isEmpty());
        </#if>
        <#if field.objectJavaType == "Boolean">
        <#if field.required>
        //test by${fieldName}
	assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}(${getFieldValue1}).findOne().get${fieldName}(), ${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue1}).find().size() == 1);
        //test by${fieldName}Not
        assertNull(${queryEntityName}().by${fieldName}Not(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}Not(${getFieldValue1}).find().isEmpty());
        //test or${fieldName}
        assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}(${getFieldValue1}).findOne().get${fieldName}(), ${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}(${getFieldValue1}).find().size() == 1);
        //test or${fieldName}Not
        assertNull(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Not(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Not(${getFieldValue1}).find().isEmpty());
        <#else>
        //test by${fieldName}
	assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}(${getFieldValue1}).findOne().get${fieldName}(),${common.getFieldCast(field)} ${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue1}).find().size() == 1);
        //test by${fieldName}Not
        assertNull(${queryEntityName}().by${fieldName}Not(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}Not(${getFieldValue1}).find().isEmpty());
        //test or${fieldName}
        assertEquals(${common.getFieldCast(field)}${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}(${getFieldValue1}).findOne().get${fieldName}(),${common.getFieldCast(field)}${getFieldValue1});
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}(${getFieldValue1}).find().size() == 1);
        //test or${fieldName}Not
        assertNull(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Not(${getFieldValue1}).findOne());
	assertTrue(${queryEntityName}().by${fieldName}(${getFieldValue2}).or${fieldName}Not(${getFieldValue1}).find().isEmpty());
        </#if>
        </#if>
        </#if>
        </#list>

        //test count
	assertTrue(${queryEntityName}().count() == 1);
		
	//test exist
	assertTrue(${queryEntityName}().exists());
		
	//test deleteAll
	${queryEntityName}().deleteAll();
	assertTrue(${queryEntityName}().find().isEmpty());
        assertFalse(${queryEntityName}().exists());
    }
}