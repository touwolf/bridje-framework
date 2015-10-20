<#include "orm-common.ftl">

package ${dm.package}.impl;

import com.touwolf.bridje.ioc.Ioc;
import com.touwolf.bridje.ioc.IocContext; 
import com.touwolf.bridje.ioc.annotations.Component; 
import com.touwolf.bridje.ioc.annotations.Inject;
import com.touwolf.bridje.ioc.annotations.Service;
import com.touwolf.bridje.ioc.annotations.Provides;
import com.touwolf.bridje.orm.impl.*;
import com.touwolf.bridje.orm.*;
import com.touwolf.bridje.orm.exceptions.*;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import ${dm.package}.*;
<@renderExtraImports entity = dm />

/**
 * Modelo de datos para ${dm.name}
 */
@Component
@Provides(
{
    @Service(${dm.name}.class)
})
class ${dm.name}Impl implements ${getEntityImplementationInterfacesDeclaration(dm)}
{
    private static final Logger logger = Logger.getLogger(${dm.name}Impl.class.getName());

    @Inject
    private DataSourceManager dsm;

    @Inject
    private IocContext iocContext;

    private DataContextImpl context;

    ${dm.name}Impl()
    {  
    }

    private ${dm.name}Impl(DataSource ds, IocContext iocContext)
    { 
        this.context = new DataContextImpl(ds, iocContext);
        this.iocContext = iocContext;
    }
    
    @PostConstruct
    void init()
    {
        DataSource ds = dsm.getDefaultDataSource("${dm.name}");   
        this.context = new DataContextImpl(ds, iocContext);
    }

    @Override
    public ${dm.name} createNew${dm.name}(DataSource ds)
    {
        return new ${dm.name}Impl(ds, this.iocContext);
    }

    @Override
    public void fix()
    {
        try
        { 
            context.fixDomainModel(dsm.getDomainModel("${dm.name}"));
        }
        catch (Exception e)
        {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    <#list dm.entitys as e>   
    <#list e.constructors as construct> 
    @Override
    public ${e.name} add${construct.name?cap_first}(${construct.paramsDeclaration}) throws QueryException, FieldException
    { 
        <#list construct.fields as field> 
        <#if field.autoIncrement?? && field.autoIncrement>
        if(${field.name} <= 0)
        {
            return null;
        }
        </#if>
        </#list>
        ${e.name?cap_first}Impl entity = new ${e.name?cap_first}Impl(context);
        <#list construct.fields as field>
        entity.${getFieldSetterStm(field,field.name)};
        </#list>
        <#list construct.relatedEntitys as relEntity> 
        <#list construct.findFieldsByRelatedEntity(relEntity) as fieldsRelated>
        <#assign assignStm = relEntity.name?uncap_first + "." + getFieldGetterStm(fieldsRelated.relatedField) />
        entity.${getFieldSetterStm(fieldsRelated.field,assignStm)};
        </#list>
        </#list>
        entity.save();
        return entity;
    }

    </#list>
    <#list e.filters as efilter>
    <#assign funcName = getEntityFilterFunctionName(efilter) />
    <#assign filterParams = showFilterParamsDefinition(efilter.params!) />
    <#if efilter.count?? && efilter.count>
    @Override 
    public int count${funcName}(${filterParams}) throws QueryException
    {
        return new ${e.name}QueryImpl(context)
                <#list efilter.params as p>
                ${p.filterCondition}
                </#list>
                .count();
    }

    </#if>
    <#if efilter.delete?? && efilter.delete>
    @Override 
    public void delete${funcName}(${filterParams}) throws QueryException
    { 
        new ${e.name}QueryImpl(context)
                <#list efilter.params as p>
                ${p.filterCondition}
                </#list>
                .deleteAll();
    }

    </#if> 
    <#if efilter.exist?? && efilter.exist>
    @Override
    public boolean exist${funcName}(${filterParams}) throws QueryException
    {
        return new ${e.name}QueryImpl(context)
                <#list efilter.params as p>
                ${p.filterCondition}
                </#list>
                .exists();
    }

    </#if> 
    <#if efilter.find?? && efilter.find>
    @Override 
    public List<${e.name?cap_first}> find${funcName}(${filterParams}) throws QueryException
    { 
        return new ${e.name}QueryImpl(context)
                <#list efilter.params as p>
                ${p.filterCondition}
                </#list>
                .find();
    }

    </#if>
    </#list>  
    </#list>
    <#list dm.entitys as e>
    <#if e.isQueryable>
    @Override
    </#if>
    public ${e.name}Query<#if !e.isQueryable>Impl</#if> query${e.name?cap_first}()
    {
        return new ${e.name}QueryImpl(context);
    }

    </#list>
<#list dm.interfaces as interfaceName>
    <#assign interface = mojo.getJavaInterfaceData(interfaceName) />
    <@renderInterfaceImplMethods entity=dm interfaceMetaData=interface />
</#list>
}