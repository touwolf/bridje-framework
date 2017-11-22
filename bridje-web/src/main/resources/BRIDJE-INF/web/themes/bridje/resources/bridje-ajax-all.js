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
        window.console && console.log('      container: ' + data.controlId);
        window.console && console.log('      state: ' + window.__bridje_info.currState);

        try
        {
            let xhr = new XMLHttpRequest();
            xhr.open(data.method, window.location, true);
            if(data.isUrlEncoded) xhr.setRequestHeader('Content-type', data.enctype);
            xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
            xhr.setRequestHeader('Bridje-View', encodeURI(data.view));
            xhr.setRequestHeader('Bridje-Container', data.controlId);
            xhr.setRequestHeader('Bridje-State', window.__bridje_info.currState);

            xhr.onload = function()
            {
                if (xhr.status === 200)
                {
                    window.__bridje_info.currState = xhr.getResponseHeader('Bridje-State') || '';
                    const location = xhr.getResponseHeader('Bridje-Location');
                    if(location && (typeof(location) === 'string'))
                    {
                        window.location = location;
                    }
                    else
                    {
                        const renderEl = document.getElementById(data.controlId);
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

    const bridjeExecuteAction = function(eventEl)
    {
        const form = findForm(eventEl);
        if (!form)
        {
            return;
        }

        const eventId = eventEl.getAttribute('data-eventid');
        const eventInput = document.getElementById(eventId);
        let eventName = '';
        if (eventInput)
        {
            eventName = eventInput.getAttribute('name');
            eventInput.setAttribute("value", "t");
        }

        let enctype = form.getAttribute('enctype') || window.__bridje_info.enctype;
        let view = form.getAttribute('data-bridje-view') || window.__bridje_info.dataBridjeView;
        let sendData = new FormData(form);
        let method = form.getAttribute('method') || 'post';

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
            event: eventName,
            method: method,
            isUrlEncoded: isUrlEncoded,
            controlId: form.id,
            sendData: sendData
        });
    };

    const initialize = function(element)
    {
        const actions = element.getElementsByClassName('bridje-action');
        for (let i = 0; i < actions.length; i++)
        {
            const action = actions[i];
            if (findForm(action))
            {
                var eventType = 'click';
                if(action.nodeName == 'SELECT') eventType = 'change';
                if(action.nodeName == 'INPUT') 
                {
                    var inputType = action.getAttribute('type');
                    if(inputType == 'button'
                            || inputType == 'radio' 
                            || inputType == 'submit'
                            || inputType == 'reset'
                            || inputType == 'checkbox')
                    {
                        eventType = 'click';
                    }
                    else
                    {
                        eventType = 'change';
                    }
                }
                
                if(eventType == 'change')
                {
                    action.onchange = function(e)
                    {
                        e && e.preventDefault();
                        bridjeExecuteAction(action);
                    };
                }
                else
                {
                    action.onclick = function(e)
                    {
                        e && e.preventDefault();
                        bridjeExecuteAction(action);
                    };
                }
            }
        }
    };

    initialize(document);
};
