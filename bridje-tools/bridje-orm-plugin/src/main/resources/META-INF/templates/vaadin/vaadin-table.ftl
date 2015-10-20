<#import "vaadin-common.ftl" as common/>
<#assign tableName = mojo.getTableName(entity) />
<#assign containerName = mojo.getContainerName(entity) />
<#assign beanName = mojo.getBeanName(entity) />
<#assign entityName = entity.name />
<#assign entityRelations = common.getReturnsOneRelations(entity) />

package ${dm.package}.vaadin;

import com.touwolf.bridje.dm.Utils;
import com.touwolf.bridje.ioc.IocContext;
import com.touwolf.bridje.ioc.annotations.Inject;
import com.touwolf.bridje.vaadin.NestedFieldSelector;
import com.vaadin.ui.Table;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
<@common.renderOrmImports entity = entity />

/**
*  Tabla de Vaadin para entidades de tipo {@link ${dm.package}.${entityName}}
*/ 
public class ${tableName} extends Table
{
    private static final Logger LOG = Logger.getLogger(${tableName}.class.getName());

    @Inject
    private IocContext context;

    @Inject
    private ${containerName} container;

    /**
    *  Encargado de dar formato a las selecciones
    */
    private NestedFieldSelector fieldSelector;

    /**
     * Propiedades que se mostraran
     */
    private Set<String> visibleProperties;

    public ${tableName}()
    {
        super();
        //Inicializo el control
        setSizeFull();
        setSelectable(true);
        setMultiSelect(true);
        setImmediate(true);
        setColumnCollapsingAllowed(true);
    }

    @PostConstruct
    protected void init()
    {
        //Si nadie implemento este container en este contexto entonces se crea uno propio
        if(container == null)
        { 
            //Inicializo el contenedor de los datos
            container = new ${containerName}();
        }
        setContainerDataSource(container);
        <#if entity.titleField?has_content>
        //Agrego el campo de caption
        ${common.getAddMethodforField(entity.titleField)}();
        </#if>
    }

    /**
     * Muestra la columna de un campo {@link ${entityName}} en la tabla
     *
     * @param fieldName Nombre del campo
     */
    protected void showColumn(String fieldName)
    {
        getVisibleProperties().add(fieldName);
        setVisibleColumns(getVisibleProperties().toArray());
    }

    /**
     * Oculta la columna de un campo {@link ${entityName}} en la tabla
     *
     * @param fieldName Nombre del campo
     */
    protected void hideColumn(String fieldName)
    {
        getVisibleProperties().remove(fieldName);
        setVisibleColumns(getVisibleProperties().toArray());
    }

    <#list common.getControlEditableFields(entity) as field>
    <#assign relatedEntityTitle = common.getFieldTitle(field) />
    <#assign addMethodName = common.getAddMethodforField(field) />

    /**
    * Agrega el campo {@link ${entityName}#${field.name}} a la tabla
    */
    public void ${addMethodName}()
    {
        ${addMethodName}("${relatedEntityTitle}");
    }

    /**
    * Agrega el campo {@link ${entityName}#${field.name}} a la tabla
    * @param title Titulo que tendra la columna correspondiente a "${field.name}"
    */
    public void ${addMethodName}(String title)
    {
        showColumn("${field.name}");
        setColumnHeader("${field.name}", title);
    }

    /**
    * Elimina la columna <b>${relatedEntityTitle}</b> de la tabla de {@link ${entityName}}
    *
    * @return  <code>true</code> si la operacion tuvo exito, sino
    * <code>false</code>
    */
    public boolean ${common.getRemoveMethodforField(field)}()
    {
        return removeContainerProperty("${field.name}");
    }

    </#list>
    <#list entityRelations as entityRelation>
    <#assign relatedEntityTitle = common.getEntityRelatedName(entityRelation) />
    <#assign relatedEntityVariableName = common.getRelatedEntityVariableName(entityRelation) />
    <#assign FieldSelectorName = mojo.getFieldSelectorName(entityRelation.relatedEntity) />
    <#assign addFieldFuncName = common.getNestedFileSelectorGetterMethodName(entityRelation.relatedName) />
    /**
    * Realiza una consulta al <code>${entityRelation.relatedName}</code> correspondiente para agregar alguno de sus campos
    *
    * @return Instancia de {@link {${FieldSelectorName}}, no <code>null</code>
    */
    public ${FieldSelectorName} ${addFieldFuncName}()
    {
        return new ${FieldSelectorName}("${relatedEntityVariableName}", getNestedFieldSelector());
    }

    </#list>
    @Override
    public ${containerName} getContainerDataSource()
    {
        return container;
    }

    /**
    * Devuelve el {@link ${beanName}} seleccionado en la tabla. Si fueron varios, devuelve el primero
    * @return instancia del {@link ${beanName}} seleccionado, o <code>null</code>
    */
    public ${beanName} getSelected${entityName}()
    {
        ${beanName} selected${entityName} = null;
        Object selectedValue = getValue();
        if (selectedValue != null)
        {
            if (isMultiSelect())
            {
                Object[] ${entityName}Array = ((LinkedHashSet) getConvertedValue()).toArray();
                if (${entityName}Array.length > 0)
                {
                    selected${entityName} = (${beanName}) ${entityName}Array[0];
                }
            }
            else
            {
                selected${entityName} = ((${beanName}) getConvertedValue());
            }
        }
        return selected${entityName};
    }

    /**
    * Devuelve las propiedades que estaran visibles en la tabla
    *
    * @return Listado de {@link String} con el nombre de las columnas que
    * estaran visibles, no <code>null</code>
    */
    public Set<String> getVisibleProperties()
    {
        if (visibleProperties == null)
        {
            visibleProperties = new LinkedHashSet<>();
        }
        return visibleProperties;
    } 
    
    /**
     * Devuelve el {@link NestedFieldSelector} para ${tableName}
     * @return Instancia de {@link NestedFieldSelector}, no <code>null</code>
     */
    protected NestedFieldSelector getNestedFieldSelector()
    {
        if(fieldSelector == null)
        {
            fieldSelector = new NestedFieldSelector()
            { 

                @Override
                public String joinFieldTokens(String parentQuery, String newToken)
                {
                    return parentQuery.concat(".").concat(Utils.capitalize(newToken));
                }

                @Override
                public void addComponentSelector(String queryString, String caption)
                { 
                    getContainerDataSource().addNestedContainerProperty(queryString);
                    LOG.log(Level.INFO, "Agregando selector de campo enlazado \"{0}\" a ${tableName}", queryString);
                    showColumn(queryString);
                    setColumnHeader(queryString, caption);
                }
            };
        }
        return fieldSelector;
    }

    <@common.renderContainerFunctions entity = entity />
}
