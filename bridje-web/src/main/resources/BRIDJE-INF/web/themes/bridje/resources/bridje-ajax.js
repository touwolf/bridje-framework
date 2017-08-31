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

/**
 * Executes a bridje action to the server, and updates the HTML of the current view.
 * 
 * @param string event The event to invoke on the server.
 */
function bridjeExecuteAction(id)
{
    var form = document.getElementById("bridje-view-container");
    var eventEl = document.getElementById(id);
    var event = eventEl.getAttribute("name");
    eventEl.setAttribute("value", "t");
    var multiPartData = new FormData(form);
    var enctype = form.getAttribute("enctype");
    var view = form.getAttribute("data-bridje-view");
    var url = window.location;

    window.console && console.log('executing bridje action: ');
    window.console && console.log('      view: ' + view);
    window.console && console.log('      enctype: ' + enctype);
    window.console && console.log('      event: ' + event);
    window.console && console.log('      data: ' + multiPartData);

    var isUrlEncoded = enctype === "application/x-www-form-urlencoded";
    if(isUrlEncoded)
    {
        var params = new URLSearchParams();
        for(var pair of multiPartData.entries())
        {
            if(typeof pair[1] === 'string')
            {
                params.append(pair[0], pair[1]);
            }
        }
        encodedData = params.toString();
    }

    try
    {
        xhr = new XMLHttpRequest();
        xhr.open(form.getAttribute("method"), url, 1);
        if(isUrlEncoded) xhr.setRequestHeader('Content-type', enctype);
        xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
        xhr.setRequestHeader('Bridje-View', encodeURI(view));
        xhr.onload = function()
        {
            if (xhr.status === 200)
            {
                var location = xhr.getResponseHeader("Bridje-Location");
                if(typeof location === "string")
                {
                    window.location = location;
                }
                else
                {
                    form.innerHTML = xhr.responseText;
                    window.bridjeActionExecutionComplete && window.bridjeActionExecutionComplete();
                }
            }
            else if (xhr.status !== 200)
            {
                window.console && console.log('Request failed. Returned status of ' + xhr.status);
            }
        };
        if(isUrlEncoded)
        {
            xhr.send(encodedData);
        }
        else
        {
            xhr.send(multiPartData);
        }
        
    }
    catch (e)
    {
        window.console && console.log(e);
    }
}
