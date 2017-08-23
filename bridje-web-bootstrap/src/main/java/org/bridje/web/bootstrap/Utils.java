
package org.bridje.web.bootstrap;

import java.util.UUID;

public class Utils
{
    public static String randomId()
    {
        return "bs_" + UUID.randomUUID().toString().replaceAll("[-]", "");
    }
}
