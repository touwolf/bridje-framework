
package org.bridje.web.view.tools;

import org.bridje.ioc.Component;
import org.bridje.ioc.thls.Thls;
import org.bridje.web.WebScope;
import org.bridje.web.view.themes.ThemeTool;

@Component
@ThemeTool(name = "bridje")
public class BridjeTools
{
    public String getRandomId()
    {
        return Thls.get(RandomIdGenerator.class).randomId();
    }

    public String getPath()
    {
        String pathStr = Thls.get(WebScope.class).getPath();
        return pathStr;
    }

    public String getOrigPath()
    {
        String pathStr = Thls.get(WebScope.class).getOrigPath();
        return pathStr;
    }
}
