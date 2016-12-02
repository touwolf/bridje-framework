
package org.bridje.sip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SipMessage
{
    private Map<String,List<String>> headers;

    public void addHeader(String header, String value)
    {
        List<String> lst = getHeaders().get(header);
        if(lst == null)
        {
            lst = new ArrayList<>();
            getHeaders().put(header, lst);
        }
        lst.add(value);
    }

    public Map<String, List<String>> getHeaders()
    {
        if(headers == null)
        {
            headers = new HashMap<>();
        }
        return headers;
    }
}
