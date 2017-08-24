
/**
 * Listener de eventos que postea la vista de bridje al servidor para los 
 * elementos bridje-action.
 * 
 * @param {object} event El evento lanzado.
 */
function postBridjeView(event)
{
    event.preventDefault();
    bridjeExecuteAction($(event.target).data('eventid'));
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
 * Funcion llamada por bridje al completarse una actualizacion de una vista.
 */
function bridjeActionExecutionComplete()
{
    initBridjeActionElements();
    window.bootstrapActionExecutionComplete && bootstrapActionExecutionComplete();
}

$(document).ready(bridjeActionExecutionComplete);
