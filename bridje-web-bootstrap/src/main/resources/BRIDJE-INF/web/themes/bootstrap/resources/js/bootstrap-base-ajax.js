
/**
 * Actualiza el valor seleccionado basandose en los valores data-selectid y 
 * data-selectvalue del elemento dado.
 * 
 * @param {object} target El elemento que contiene el id del campo y el valor a actualizar.
 */
function updateSelectedValue(target)
{
    var el = $(target);
    var selectId = el.data('selectid');
    if(typeof selectId === "string" && selectId !== "")
    {
        var selectValue = el.data('selectvalue');
        if(typeof selectValue !== "undefined")
        {
            if(typeof selectValue === "object")
            {
                $('#' + selectId).val(JSON.stringify(selectValue));
            }
            else
            {
                $('#' + selectId).val(selectValue);
            }
        }
    }
}

/**
 * Listener de eventos que postea la vista de bridje al servidor para los 
 * elementos bridje-action.
 * 
 * @param {object} event El evento lanzado.
 */
function postBridjeView(event)
{
    event.preventDefault();
    updateSelectedValue(event.target);
    var action = $(event.target).data('action');
    bridjeExecuteAction(action);
}

/**
 * Inicializa los elemento de clase bridje-action. para que al ser accionados 
 * envien al servidor la accion de bridje que debe realizarse.
 */
function initBridjeActionElements()
{
    var el = $('#bridje-view-container');
    el.find('a.bridje-action').click(postBridjeView);
    el.find('button.bridje-action').click(postBridjeView);
    el.find('select.bridje-action').change(postBridjeView);
}

/**
 * Mostrar los mensajes toast.
 */
function bootstrapBaseShowToast()
{
    $('#bridje-view-container').find('.show-toast').each(function(index, element)
    {
        var title = $(element).data("alerttitle");
        var text = $(element).data("alerttext");
        var icon = $(element).data("icon");
        var position = $(element).data("position");
        var loaderbg = $(element).data("loaderbg");

        $.toast({
            heading: title,
            text: text,
            position: position,
            loaderBg: loaderbg,
            textColor: 'white',
            bgColor : icon,
            icon: icon,
            hideAfter: 3000, 
            stack: 6
       });        
    });
}

/**
 * Funcion llamada por bridje al completarse una actualizacion de una vista.
 */
function bridjeActionExecutionComplete()
{
    initBridjeActionElements();
    bootstrapBaseShowToast();
    window.bootstrapActionExecutionComplete && bootstrapActionExecutionComplete();
}
    
$(document).ready(bridjeActionExecutionComplete);
