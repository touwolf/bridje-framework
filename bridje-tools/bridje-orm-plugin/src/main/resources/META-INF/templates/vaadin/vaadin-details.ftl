<#import "vaadin-common.ftl" as common/>
<#assign detailName = mojo.getDetailName(entity) />
<#assign containerName = mojo.getContainerName(entity) />
<#assign beanName = mojo.getBeanName(entity) />
<#assign entityName = entity.name />

package ${dm.package}.vaadin;

import com.touwolf.bridje.ioc.IocContext;
import com.touwolf.bridje.ioc.annotations.Inject;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import java.util.LinkedHashSet;
import java.util.logging.Logger;
import java.util.Set;
<@common.renderOrmImports entity = entity />

/**
* Formulario de Vaadin para ver los datos de un {@link ${dm.package}.${entityName}}
*/ 
public abstract class ${detailName} extends FormLayout
{
    private static final Logger LOG = Logger.getLogger(${detailName}.class.getName());

    @Inject
    private IocContext context;

    private ${containerName} containerDataSource;

    private BeanFieldGroup<${beanName}> binder;

    private Set<String> visibleProperties;

    private final Button saveButton;

    /**
    * Crea el formulario de Vaadin para ver los datos de un {@link ${dm.package}.${entityName}}
    */
    public ${detailName}()
    {
        super();
        setImmediate(true);
        saveButton = new Button("Salvar", new Button.ClickListener()
        {
            @Override
            public void buttonClick(final Button.ClickEvent event)
            {
                try
                {
                    getBinder().commit();
                    ${containerName} dataContainer = getContainerDataSource();
                    if (dataContainer != null && save(getBinder().getItemDataSource().getBean()))
                    {
                        dataContainer.refreshItems();
                    }
                }
                catch (final FieldGroup.CommitException e)
                {
                    Notification
                            .show("Actualizacion fallida: " + e.getMessage(),
                                    Notification.Type.TRAY_NOTIFICATION);
                }
            }
        });
    }

    /**
     * Establece un origen de datos del {@link ${beanName}} cargado, asi se sincronizaran los cambios efectuados,
     * en dicho container.
     *
     * @param container Contenedor del cual se obtuvo el dato cargado
     */
    public void setContainerDataSource(${containerName} container)
    {
        this.containerDataSource = container;
    }

    /**
     * Devuelve el origen de datos al que pertence el {@link ${beanName}} cargado.
     *
     * @return Instancia de {@link ${beanName}}, puede ser <code>null</code>
     */
    public ${containerName} getContainerDataSource()
    {
        return containerDataSource;
    }

    <#list entity.getVisibleFields() as field>
        <#assign fieldTitle = common.getFieldTitle(field) />

    /**
    * Agrega el campo {@link ${entityName}#${field.name}} al formulario
    */
    public void ${common.getAddMethodforField(field)}()
    {
        getVisibleProperties().add("${field.name}");
    }

    /**
    * Elimina el campo <b>${fieldTitle}</b> del formulario de {@link ${entityName}}
    *
    * @return  <code>true</code> si la operacion tuvo exito, sino
    * <code>false</code>
    */
    public boolean ${common.getRemoveMethodforField(field)}()
    {
        return getVisibleProperties().remove("${field.name}");
    }

    </#list> 

    /**
     * Establece un {@link Item} como la fuente de los datos a mostrar en
     * el formulario
     * 
     * @param item {@link Item} que contiene los datos a mostrar
     */
    public void setItemDataSource(Item item)
    {
        getBinder().setItemDataSource(item);
        for (String propertyId : getVisibleProperties())
        {
            bindProperty(propertyId);
        }
        addComponent(saveButton);
    }

    /**
     * Establece un {@link ${beanName}} como la fuente de los datos a mostrar en
     * el formulario
     *
     * @param ${beanName?uncap_first} {@link ${beanName}} que contiene los datos a mostrar
     */
    public void setItemDataSource(${beanName} ${beanName?uncap_first})
    {
        BeanItem<${beanName}> item = new BeanItem<>(${beanName?uncap_first}, getVisibleProperties());
        setItemDataSource(item);
    }

    /**
     * Obtiene listado de las propiedades que se muestran en el formulario
     *
     * @return Listado de propiedades (no repetido), no <code>null</code>
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
     * Crea un control de formulario correspondiente a un campo especificado de {@link
     * ${beanName}}
     *
     * @param propertyName Nombre de la propiedad de {@link ${beanName}}
     */
    protected void bindProperty(String propertyName)
    {
        unBindProperty(propertyName);
        Field createdField = null;
        switch (propertyName)
        {
            <#list entity.getVisibleFields() as field>
            <#assign title = common.getFieldTitle(field) />
            case "${field.name}":
                createdField = getBinder().buildAndBind("${title}", "${field.name}");
                <#if field.readonly>
                createdField.setReadOnly(true);
                </#if>
                break;
            </#list>
        }
        if (createdField != null)
        {
            addComponent(createdField);
        }
    }

    /**
     * Elimina del formulario el {@link Field} correspondiente a una propiedad
     * de {@link ${beanName}}
     *
     * @param propertyName Nombre de la propiedad de {@link ${beanName}}
     */
    private void unBindProperty(String propertyName)
    {
        Field<?> field = getBinder().getField(propertyName);
        if (field != null)
        {
            getBinder().unbind(field);
            removeComponent(field);
        }
    }

    /**
     * Obtiene el {@link BeanFieldGroup} que creara por cada propiedad de {@link
     * ${beanName}} un {@link Field} para mostrar sus datos
     *
     * @return Instancia de {@link BeanFieldGroup}, no <code>null</code>
     */
    public BeanFieldGroup<${beanName}> getBinder()
    {
        if (binder == null)
        {
            binder = new BeanFieldGroup<>(${beanName}.class);
        }
        return binder;
    }

    /**
     * Salva el contenido del bean en base de datos
     *
     * @param bean Bean con el contenido a guardar
     * @return true|false se pudo guardar el contenido en base de datos o no,
     * respectivamente
     */
    protected abstract boolean save(${beanName} bean);

}
