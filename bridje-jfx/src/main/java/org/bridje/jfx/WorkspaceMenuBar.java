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

package org.bridje.jfx;

import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * 
 */
class WorkspaceMenuBar extends MenuBar
{
    public void addMenuItem(MenuItem item, MenuAction action)
    {
        String path = action.path();
        String[] menuTitles = path.split("/");
        Menu currentMenu = null;
        for (String menuTitle : menuTitles)
        {
            currentMenu = findMenu(currentMenu, menuTitle);
        }
        addMenuItem(currentMenu, item);
    }

    private Menu findMenu(Menu currentMenu, String menuTitle)
    {
        List<Menu> menus = findMenus(currentMenu);
        for (Menu menu : menus)
        {
            if(menu.getText().equals(menuTitle))
            {
                return menu;
            }
        }

        return addNewMenu(currentMenu, menuTitle);
    }

    private List<Menu> findMenus(Menu currentMenu)
    {
        if(currentMenu == null)
        {
            return getMenus();
        }
        else
        {
            return currentMenu.getItems()
                    .stream()
                    .filter((mi) -> mi instanceof Menu)
                    .map((mi) -> (Menu)mi)
                    .collect(Collectors.toList());
        }
    }

    private Menu addNewMenu(Menu currentMenu, String menuTitle)
    {
        Menu result = new Menu(menuTitle);
        if(currentMenu == null)
        {
            getMenus().add(result);
        }
        else
        {
            currentMenu.getItems().add(result);
        }
        return result;
    }

    private void addMenuItem(Menu currentMenu, MenuItem item)
    {
        if(currentMenu != null)
        {
            currentMenu.getItems().add( item );
        }
    }
}
