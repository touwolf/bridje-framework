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

(function()
{
    if( !window.__bridje ) window.__bridje = {};
    window.__bridje.callbacks = [];
    window.__bridje.callback = function(callbackFunc)
    {
        window.__bridje.callbacks.push(callbackFunc);
    };
    window.__bridje.initialize = function()
    {
        for (i = 0; i < window.__bridje.callbacks.length; i++)
        {
            let func = window.__bridje.callbacks[i];
            func(document, {});
        }
    };
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
        window.console && console.log('      form: ' + data.formId);
        window.console && console.log('      container: ' + data.containerId);
        window.console && console.log('      state: ' + window.__bridje.info.currState);

        try
        {
            let xhr = new XMLHttpRequest();
            xhr.open(data.method, window.location, true);
            if(data.isUrlEncoded) xhr.setRequestHeader('Content-type', data.enctype);
            xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
            xhr.setRequestHeader('Bridje-View', encodeURI(data.view));
            xhr.setRequestHeader('Bridje-Form', data.formId);
            xhr.setRequestHeader('Bridje-Container', data.containerId);
            xhr.setRequestHeader('Bridje-State', window.__bridje.info.currState);

            xhr.onload = function()
            {
                if (xhr.status === 200)
                {
                    window.__bridje.info.currState = xhr.getResponseHeader('Bridje-State') || '';
                    const location = xhr.getResponseHeader('Bridje-Location');
                    if(location && (typeof(location) === 'string'))
                    {
                        window.location = location;
                    }
                    else
                    {
                        let renderEl = document.getElementById(data.containerId);
                        if (renderEl) renderEl.outerHTML = xhr.responseText;
                        renderEl = document.getElementById(data.containerId);
                        if (renderEl) 
                        {
                            initializeActions(renderEl);
                            window.__bridje.inAction = false;
                            for (i = 0; i < window.__bridje.callbacks.length; i++)
                            {
                                let func = window.__bridje.callbacks[i];
                                func(renderEl, data);
                            }
                        }
                    }
                }
                else
                {
                    window.console && console.error('Request failed. Status: ' + xhr.status);
                }
                window.__bridje.inAction = false;
                removeElementActionClass(data.eventEl);
            };
            xhr.send(data.sendData);
        }
        catch (e)
        {
            window.console && console.error(e);
            window.__bridje.inAction = false;
            removeElementActionClass(data.eventEl);
        }
    };

    const execute = function(eventEl)
    {
        if (window.__bridje.inAction)
        {
            return;
        }

        window.__bridje.inAction = true;
        const form = findForm(eventEl);
        if (!form) return;

        addElementActionClass(eventEl);

        const eventId = eventEl.getAttribute('data-eventid');
        const eventInput = document.getElementById(eventId);
        let eventName = '';
        if (eventInput)
        {
            eventName = eventInput.getAttribute('name');
            eventInput.setAttribute("value", "t");
        }

        let enctype = form.getAttribute('enctype') || window.__bridje.info.enctype;
        let view = form.getAttribute('data-bridje-view') || window.__bridje.info.dataBridjeView;
        let sendData = new FormData(form);
        let method = form.getAttribute('method') || 'post';

        let isUrlEncoded = enctype === 'application/x-www-form-urlencoded';
        if(isUrlEncoded)
        {
            let params = "";
            for(let pair of sendData.entries())
            {
                if(typeof(pair[1]) === 'string')
                {
                    if (params.length > 0) { params += "&"; }
                    params += pair[0] + "=" + pair[1];
                }
            }
            sendData = params;
        }
        let containerId = form.getAttribute('data-container')
        if(!containerId) containerId = form.id;
        ajax({
            view: view,
            enctype: enctype,
            event: eventName,
            method: method,
            isUrlEncoded: isUrlEncoded,
            formId: form.id,
            containerId: containerId,
            sendData: sendData,
            eventEl: eventEl
        });
    };
    window.__bridje.execute = function(element)
    {
        execute(element);
    };

    const refresh = function(container)
    {
        if (window.__bridje.inAction)
        {
            return;
        }

        window.__bridje.inAction = true;
        let enctype = 'application/x-www-form-urlencoded';
        let method = 'post';
        let containerId = container.id;
        ajax({
            view: window.__bridje.info.dataBridjeView,
            enctype: enctype,
            event: '',
            method: method,
            isUrlEncoded: true,
            formId: '',
            containerId: containerId,
            sendData: '',
            eventEl: container
        });
    };
    window.__bridje.refresh = function(element)
    {
        refresh(element);
    };

    const removeElementActionClass = function(element)
    {
        const actionClass = element.getAttribute('data-action-class');
        if (actionClass)
        {
            const regex = new RegExp('\\b' + actionClass + '\\b','g')
            element.className.replace(regex, "");
        }
    };

    const addElementActionClass = function(element)
    {
        const actionClass = element.getAttribute('data-action-class');
        if (actionClass)
        {
            const clsArr = element.className.split(" ");
            if (clsArr.indexOf(actionClass) < 0)
            {
                element.className += " " + name;
            }
        }
    };

    const initializeActions = function(element)
    {
        let actions = element.getElementsByClassName('bridje-action-click');
        for (let i = 0; i < actions.length; i++)
        {
            const action = actions[i];
            if (findForm(action))
            {
                const eventId = action.getAttribute('data-eventid');
                const eventInput = document.getElementById(eventId);
                if (eventInput)
                {
                    action.onclick = function(e)
                    {
                        e && e.preventDefault();
                        execute(action);
                    };
                }
            }
        }

        actions = element.getElementsByClassName('bridje-action-change');
        for (let i = 0; i < actions.length; i++)
        {
            const action = actions[i];
            if (findForm(action))
            {
                const eventId = action.getAttribute('data-eventid');
                const eventInput = document.getElementById(eventId);
                if (eventInput)
                {
                    action.onchange = function(e)
                    {
                        e && e.preventDefault();
                        execute(action);
                    };
                }
            }
        }

        actions = element.getElementsByClassName('bridje-action-enter');
        for (let i = 0; i < actions.length; i++)
        {
            const action = actions[i];
            if (findForm(action))
            {
                const eventId = action.getAttribute('data-eventid');
                const eventInput = document.getElementById(eventId);
                if (eventInput)
                {
                    action.onkeypress = function(e)
                    {
                        if(e)
                        {
                            if (e.which == 13 || e.keyCode == 13)
                            {
                                e.preventDefault();
                                execute(action);
                            }
                        }
                    };
                }
            }
        }
    };

    window.onload = function()
    {
        initializeActions(document);
    };
})();