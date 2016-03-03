package ${dataModel.package}; 

public class ${dataModel.name}
{
    <#list dataModel.concreteEntitys as entity>
    public void query${entity.name}()
    {
        return ;
    }

    </#list>
}