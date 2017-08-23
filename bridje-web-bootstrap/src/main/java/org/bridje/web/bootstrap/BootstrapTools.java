
package org.bridje.web.bootstrap;

import org.bridje.web.view.themes.ThemeTool;
import org.bridje.ioc.Component;
import org.bridje.ioc.thls.Thls;
import org.bridje.web.WebScope;

@Component
@ThemeTool(name = "bootstrap")
public class BootstrapTools
{
    public String getRandomId()
    {
        return Utils.randomId();
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
