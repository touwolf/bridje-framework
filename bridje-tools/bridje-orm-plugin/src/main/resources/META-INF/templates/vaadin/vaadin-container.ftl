<#import "vaadin-common.ftl" as common/>
<#assign containerName = mojo.getContainerName(entity) />
<#assign beanName = mojo.getBeanName(entity) />
<#assign dmName = entity.dataModel.name />
<#assign entityName = entity.name />
<#assign entityRelations = common.getReturnsOneRelations(entity) />

package ${dm.package}.vaadin;

import com.touwolf.bridje.ioc.annotations.Component;
import com.touwolf.bridje.ioc.annotations.Inject;
import com.touwolf.bridje.ioc.IocContext;
import com.vaadin.data.util.BeanItemContainer;
import java.util.Collection;
import java.util.logging.Logger;
<@common.renderOrmImports entity = entity showDm=true showRelated=false showGenerated=false />

/**
* Contenedor de datos para Vaadin para {@link ${dm.package}.${entityName}}
*/
@Component
public class ${containerName} extends BeanItemContainer<${beanName}>
{
    private static final Logger LOG = Logger.getLogger(${containerName}.class.getName());

    @Inject
    IocContext context;

    /**
    * Crea el contenedor de {@link ${entityName}}
    *
    */
    public ${containerName}()
    {
        this(${beanName}.class);
    }

    /**
     * Crea el contenedor de {@link ${beanName}}
     *
     * @param type Tipo de datos derivado de {@link ${beanName}}
     */
    public ${containerName}(Class<? super ${beanName}> type) throws IllegalArgumentException
    {
        super(type);
    }

    /**
    * Agrega elementos obtenidos del Orm
    *
    * @param collection Listado de {@link ${entityName}}
    */
    public void add${entityName}List(Collection<? extends ${entityName}> collection)
    {
        for (${entityName} ${entityName?lower_case} : collection)
        {
            addItem(new ${beanName}(${entityName?lower_case}, context));
        }
    }

    /**
    * Establece todos los elementos del dominio para {@link ${entityName}} que se mostraran en los controles
    *
    * @param collection Listado de {@link ${entityName}}
    */
    public void set${entityName}List(Collection<? extends ${entityName}> collection)
    {
        removeAllItems();
        add${entityName}List(collection);
    }

    /**
    * Manda a los componentes que tienen a este componente como contenedor, que refresquen su contenido
    */
    public void refreshItems()
    {
        fireItemSetChange();
    }
}


