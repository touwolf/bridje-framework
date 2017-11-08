/*
 * Copyright 2016 Bridje Framework.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

window.onload = function()
{
    const findForm = function(element)
    {
        let node = element;
        while (node.nodeName !== 'FORM' && node.parentNode)
        {
            node = node.parentNode;
        }
        return node.nodeName === 'FORM' ? node : null;
    };

    const findControlId = function(element, form)
    {
        let controlId = element.getAttribute('control-id');
        if (form)
        {
            controlId = form.id || action.getAttribute('control-id');
        }
        return controlId;
    };

    const ajax = function(data)
    {
        window.console && console.log('executing bridje action: ');
        window.console && console.log('      view: ' + data.view);
        window.console && console.log('      enctype: ' + data.enctype);
        window.console && console.log('      event: ' + data.event);
        let dataStr = data.sendData;
        if (typeof(dataStr) !== 'string' && typeof(dataStr.entries) === 'function')
        {
            dataStr = [...dataStr.entries()]
                      .map(e => encodeURIComponent(e[0]) + "=" + encodeURIComponent(e[1]));
        }
        window.console && console.log('      data: ' + dataStr);
        window.console && console.log('      control-id: ' + data.controlId);
        window.console && console.log('      result-id: ' + data.resultId);

        try
        {
            let xhr = new XMLHttpRequest();
            xhr.open(data.method, window.location, true);
            if(data.isUrlEncoded) xhr.setRequestHeader('Content-type', data.enctype);
            xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
            xhr.setRequestHeader('Bridje-View', encodeURI(data.view));
            xhr.setRequestHeader('Bridje-ControlId', data.controlId);
            if (data.resultId) xhr.setRequestHeader('Bridje-ResultId', data.resultId);

            xhr.onload = function()
            {
                if (xhr.status === 200)
                {
                    const location = xhr.getResponseHeader('Bridje-Location');
                    if(location && (typeof(location) === 'string'))
                    {
                        window.location = location;
                    }
                    else
                    {
                        const renderId = data.resultId || data.controlId;
                        const renderEl = document.getElementById(renderId);
                        if (renderEl)
                        {
                            renderEl.innerHTML = xhr.responseText;
                            initialize(renderEl);
                            window.bridjeActionExecutionComplete && window.bridjeActionExecutionComplete();
                        }
                    }
                }
                else
                {
                    window.console && console.error('Request failed. Status: ' + xhr.status);
                }
            };
            xhr.send(data.sendData);
        }
        catch (e)
        {
            window.console && console.error(e);
        }
    };

    /**
     * Executes a bridje action to the server, and updates the HTML of the current view.
     *
     * @param {HTMLElement} eventEl.
     */
    const bridjeExecuteAction = function(eventEl)
    {
        let enctype = window.__bridje_info.enctype;
        let view = window.__bridje_info.dataBridjeView;
        eventEl.setAttribute('value', 't');

        const form = findForm(eventEl);
        const controlId = findControlId(eventEl, form);

        let sendData = null;
        let method = 'get';
        if (form)
        {
            method = form.getAttribute('method') || 'post';
            sendData = new FormData(form);
            enctype = form.getAttribute('enctype') || window.__bridje_info.enctype;
            view = form.getAttribute('data-bridje-view') || window.__bridje_info.dataBridjeView;
        }

        let isUrlEncoded = enctype === 'application/x-www-form-urlencoded';
        if(isUrlEncoded)
        {
            let params = new URLSearchParams();
            for(let pair of sendData.entries())
            {
                if(typeof(pair[1]) === 'string')
                {
                    params.append(pair[0], pair[1]);
                }
            }
            sendData = params.toString();
        }
        ajax({
            view: view,
            enctype: enctype,
            event: eventEl.getAttribute('event'),
            method: method,
            isUrlEncoded: isUrlEncoded,
            controlId: controlId,
            resultId: eventEl.getAttribute('result-id'),
            sendData: sendData
        });
    };

    const initialize = function(element)
    {
        const actions = element.getElementsByClassName('bridje-action');
        for (let i = 0; i < actions.length; i++)
        {
            const action = actions[i];
            const controlId = findControlId(action, findForm(action));
            if (controlId)
            {
                action.onclick = function(e)
                {
                    e && e.preventDefault();
                    bridjeExecuteAction(action);
                };
            }
        }
    };

    initialize(document);
};
