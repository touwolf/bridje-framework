<#import "vaadin-common.ftl" as common/>
<#assign tableName = mojo.getTableName(entity) />
<#assign selectorName = mojo.getSelectorName(entity) />
<#assign containerName = mojo.getContainerName(entity) />
<#assign beanName = mojo.getBeanName(entity) />
<#assign entityName = entity.name />

package ${dm.package}.vaadin;

import com.touwolf.bridje.ioc.annotations.Inject;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import javax.annotation.PostConstruct;
<@common.renderOrmImports entity = entity />

/**
 * ComboBox de elementos de ${entity.name}
 */ 
public class ${selectorName} extends ComboBox
{

    @Inject
    protected ${containerName} container;

    public ${selectorName}()
    {
        super();
        setInputPrompt("Seleccione un elemento");
        setItemCaptionPropertyId("description");
        setItemCaptionMode(ItemCaptionMode.PROPERTY);
        setFilteringMode(FilteringMode.CONTAINS);
        setImmediate(true);
        setNullSelectionAllowed(false);
    }

    @PostConstruct
    protected void init()
    {
        //Inicializo el control
        setContainerDataSource(container);
    }
    
    /**
     * Agrega un {@link ${beanName}} al listado de elementos
     *
     * @param newItem Nuevo {@link ${beanName}}
     */
    public void addItem(${beanName} newItem)
    {
        BeanItem<${beanName}> item = new BeanItem<>(newItem);
        addItem((Object)item);
    }
}