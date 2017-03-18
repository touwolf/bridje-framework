
package org.bridje.sip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A SIP messaje.
 */
public class SipMessage
{
    private Map<String,List<String>> headers;

    /**
     * Add a new header to the SIP messaje.
     * 
     * @param header The header name.
     * @param value The header value.
     */
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

    /**
     * Get the map os headers for this SIP messaje.
     * 
     * @return The list of headers for this SIP messaje.
     */
    public Map<String, List<String>> getHeaders()
    {
        if(headers == null)
        {
            headers = new HashMap<>();
        }
        return headers;
    }
}
