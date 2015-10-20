<#include "orm-common.ftl">

package ${dm.package}.impl;

import com.touwolf.bridje.orm.exceptions.*;
import com.touwolf.bridje.orm.impl.QueryBuilder;
import com.touwolf.bridje.orm.impl.DataContextImpl;
import ${dm.package}.*; 
import java.util.List;
import java.util.LinkedList;
import java.util.Date;

/**
 * 
 */
class ${entity.name}QueryImpl <#if entity.isQueryable>implements ${entity.name}Query</#if>
{
    private final DataContextImpl context;
    
    private final QueryBuilder queryBuilder;

    ${entity.name}QueryImpl(DataContextImpl context)
    {
        this.context = context;
        this.queryBuilder = new QueryBuilder(context, "${entity.table}");
    }

    ${entity.name}QueryImpl(DataContextImpl context, QueryBuilder queryBuilder)
    {
        this.context = context;
        this.queryBuilder = queryBuilder;
    }

    <#list entity.fields as f>
    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl by${f.name?cap_first}(${f.objectJavaType}... values)
    {
        this.queryBuilder.by("${f.column}","=",(Object[])values);
        return this;
    }

    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl by${f.name?cap_first}Not(${f.objectJavaType}... values)
    {
        this.queryBuilder.byNot("${f.column}","=",(Object[])values);
        return this;
    }

    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl or${f.name?cap_first}(${f.objectJavaType}... values)
    {
        this.queryBuilder.or("${f.column}","=",(Object[])values);
        return this;
    }

    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl or${f.name?cap_first}Not(${f.objectJavaType}... values)
    {
        this.queryBuilder.orNot("${f.column}","=",(Object[])values);
        return this;
    }

    <#if f.isNumeric || f.isDate>
    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl by${f.name?cap_first}Ge(${f.objectJavaType}... values)
    {
        this.queryBuilder.by("${f.column}",">=",(Object[])values);
        return this;
    }

    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl by${f.name?cap_first}NotGe(${f.objectJavaType}... values)
    {
        this.queryBuilder.byNot("${f.column}",">=",(Object[])values);
        return this;
    }

    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl or${f.name?cap_first}Ge(${f.objectJavaType}... values)
    {
        this.queryBuilder.or("${f.column}",">=",(Object[])values);
        return this;
    }

    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl or${f.name?cap_first}NotGe(${f.objectJavaType}... values)
    {
        this.queryBuilder.orNot("${f.column}",">=",(Object[])values);
        return this;
    }

    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl by${f.name?cap_first}Gt(${f.objectJavaType}... values)
    {
        this.queryBuilder.by("${f.column}",">",(Object[])values);
        return this;
    }

    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl by${f.name?cap_first}NotGt(${f.objectJavaType}... values)
    {
        this.queryBuilder.byNot("${f.column}",">",(Object[])values);
        return this;
    }

    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl or${f.name?cap_first}Gt(${f.objectJavaType}... values)
    {
        this.queryBuilder.or("${f.column}",">",(Object[])values);
        return this;
    }

    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl or${f.name?cap_first}NotGt(${f.objectJavaType}... values)
    {
        this.queryBuilder.orNot("${f.column}",">",(Object[])values);
        return this;
    }

    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl by${f.name?cap_first}Le(${f.objectJavaType}... values)
    {
        this.queryBuilder.by("${f.column}","<=",(Object[])values);
        return this;
    }

    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl by${f.name?cap_first}NotLe(${f.objectJavaType}... values)
    {
        this.queryBuilder.byNot("${f.column}","<=",(Object[])values);
        return this;
    }

    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl or${f.name?cap_first}Le(${f.objectJavaType}... values)
    {
        this.queryBuilder.or("${f.column}","<=",(Object[])values);
        return this;
    }

    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl or${f.name?cap_first}NotLe(${f.objectJavaType}... values)
    {
        this.queryBuilder.orNot("${f.column}","<=",(Object[])values);
        return this;
    }

    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl by${f.name?cap_first}Lt(${f.objectJavaType}... values)
    {
        this.queryBuilder.by("${f.column}","<",(Object[])values);
        return this;
    }

    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl by${f.name?cap_first}NotLt(${f.objectJavaType}... values)
    {
        this.queryBuilder.byNot("${f.column}","<",(Object[])values);
        return this;
    }

    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl or${f.name?cap_first}Lt(${f.objectJavaType}... values)
    {
        this.queryBuilder.or("${f.column}","<",(Object[])values);
        return this;
    }

    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl or${f.name?cap_first}NotLt(${f.objectJavaType}... values)
    {
        this.queryBuilder.orNot("${f.column}","<",(Object[])values);
        return this;
    }

    </#if>
    <#if f.javaType == "String">
    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl by${f.name?cap_first}Like(${f.objectJavaType}... values)
    {
        this.queryBuilder.by("${f.column}","like",(Object[])values);
        return this;
    }

    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl by${f.name?cap_first}NotLike(${f.objectJavaType}... values)
    {
        this.queryBuilder.byNot("${f.column}","like",(Object[])values);
        return this;
    }

    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl or${f.name?cap_first}Like(${f.objectJavaType}... values)
    {
        this.queryBuilder.or("${f.column}","like",(Object[])values);
        return this;
    }

    <#if f.visible && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl or${f.name?cap_first}NotLike(${f.objectJavaType}... values)
    {
        this.queryBuilder.orNot("${f.column}","like",(Object[])values);
        return this;
    }

    </#if>
    <#if f.visible && !(f.autoIncrement?? && f.autoIncrement) && entity.isQueryable>
    @Override
    </#if>
    <#if f.visible>public </#if>${entity.name}QueryImpl set${f.name?cap_first}(${f.javaType} value)
    {
        this.queryBuilder.set("${f.column}", value);
        return this;
    }

    </#list>
    <#list entity.relations as relation>
    <#if entity.isQueryable>
    @Override
    </#if>
    public ${relation.relatedEntity.name}QueryImpl join${relation.relatedName?cap_first}()
    {
        <#assign currentFields = "" />
        <#assign relatedFields = "" />
        <#list relation.fields as f>
            <#if currentFields == "" >
                <#assign currentFields = "\"" + f.field.column + "\"" />
            <#else>
                <#assign currentFields = currentFields + ", \"" + f.field.column + "\"" />
            </#if>
            <#if relatedFields == "" >
                <#assign relatedFields = "\"" + f.relatedField.column + "\"" />
            <#else>
                <#assign relatedFields = relatedFields + ", \"" + f.relatedField.column + "\"" />
            </#if>
        </#list>
        return new ${relation.relatedEntity.name}QueryImpl(context, this.queryBuilder.innerJoin("${relation.relatedEntity.table}", new String[] { ${currentFields} }, new String[] { ${relatedFields} }));
    }

    <#if entity.isQueryable>
        @Override
    </#if>
    public ${entity.name}QueryImpl by${relation.relatedName?cap_first}(${relation.relatedEntity.name} ${relation.relatedName?uncap_first})
    {
        if( ${relation.relatedName?uncap_first} != null )
        {
            <#list relation.fields as f>
            by${f.field.name?cap_first}( ((${relation.relatedEntity.name}Impl)${relation.relatedName?uncap_first}).get${f.relatedField.name?cap_first}() );
            </#list>
        }
        return this;
    }

    <#if entity.isQueryable>
        @Override
    </#if>
    public ${entity.name}QueryImpl set${relation.relatedName?cap_first}(${relation.relatedEntity.name} ${relation.relatedName?uncap_first})
    {
        if( ${relation.relatedName?uncap_first} != null )
        {
            <#list relation.fields as f>
            <#if !f.field.autoIncrement?? || !f.field.autoIncrement>
            set${f.field.name?cap_first}( ((${relation.relatedEntity.name}Impl)${relation.relatedName?uncap_first}).get${f.relatedField.name?cap_first}() );
            </#if>
            </#list>
        }
        return this;
    }

    </#list>
<#if entity.isQueryable>
    @Override
</#if>
    public int count() throws QueryException
    {
        return this.queryBuilder.countAll();
    }

<#if entity.isQueryable>
    @Override
</#if>
    public boolean exists() throws QueryException
    {
        return count() > 0;
    }

<#if entity.isQueryable>
    @Override
</#if>
    public List<${entity.name}> find() throws QueryException
    {
        if(!this.queryBuilder.hasSelectFields())
        {
            <#list entity.fields as f>
            this.queryBuilder.select("${f.column}");
            </#list>
        }
        List<${entity.name}> result = new LinkedList<>();
        List<Object[]> fetchAll = this.queryBuilder.fetchAll();
        for (Object[] record : fetchAll)
        {
            result.add(new ${entity.name}Impl(context, record));
        }
        return result;
    }

<#if entity.isQueryable>
    @Override
</#if>
    public List<${entity.name}> find(int page, int pageSize) throws QueryException
    {
        if(!this.queryBuilder.hasSelectFields())
        {
            <#list entity.fields as f>
            this.queryBuilder.select("${f.column}");
            </#list>
        }
        List<${entity.name}> result = new LinkedList<>();
        List<Object[]> fetchAll = this.queryBuilder.fetchAll();
        int pageEnd = page + pageSize - 2;
        if(fetchAll.size() - 1 < pageEnd)
            pageEnd = fetchAll.size() - 1;
        for(int i = page - 1; i <= pageEnd; i++)
        {
            result.add(new ${entity.name}Impl(context, fetchAll.get(i)));
        }
        return result;
    }

<#if entity.isQueryable>
    @Override
</#if>
    public ${entity.name} findOne() throws QueryException
    {
        if(!this.queryBuilder.hasSelectFields())
        {
            <#list entity.fields as f>
            this.queryBuilder.select("${f.column}");
            </#list>
        }
        Object[] record = this.queryBuilder.fetchOne();
        if(record == null)
        {
            return null;
        }
        return new ${entity.name}Impl(context, record);
    }

<#if entity.isQueryable>
    @Override
</#if>
    public int deleteOne() throws QueryException
    {
        return this.queryBuilder.deleteOne();
    }

<#if entity.isQueryable>
    @Override
</#if>
    public int deleteAll() throws QueryException
    {
        return this.queryBuilder.deleteAll();
    }

    List<Object[]> fetchAll() throws QueryException
    {
        if(!this.queryBuilder.hasSelectFields())
        {
            <#list entity.fields as f>
            this.queryBuilder.select("${f.column}");
            </#list>
        }
        return this.queryBuilder.fetchAll();
    }

    Object[] fetchOne() throws QueryException
    {
        if(!this.queryBuilder.hasSelectFields())
        {
            <#list entity.fields as f>
            this.queryBuilder.select("${f.column}");
            </#list>
        }
        return this.queryBuilder.fetchOne();
    }

    Object fetchValue(String field) throws QueryException
    {
        return this.queryBuilder.fetchValue(field);
    }

    int updateOne() throws QueryException
    {
        return this.queryBuilder.updateOne();
    }

    int updateAll() throws QueryException
    {
        return this.queryBuilder.updateAll();
    }

    Object[] insert() throws QueryException
    {
        return this.queryBuilder.insert(<#list entity.incrementFields as f>"${f.column}"<#if f_has_next>, </#if></#list>);
    }
}