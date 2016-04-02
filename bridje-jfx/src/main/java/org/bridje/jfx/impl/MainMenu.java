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

package org.bridje.jfx.impl;

import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.annotation.PostConstruct;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.ioc.Ioc;
import org.bridje.ioc.IocContext;
import org.bridje.jfx.TopMenu;

/**
 * 
 */
@Component
public class MainMenu extends MenuBar
{
    @Inject
    private IocContext context;
    
    @PostConstruct
    public void init()
    {
        context.getClassRepository()
                .navigateAnnotClasses(TopMenu.class, this::accept);
    }

    private void accept(Class component, TopMenu annotation)
    {
        if(MenuItem.class.isAssignableFrom(component))
        {
            String path = annotation.path();
            String[] menuTitles = path.split("/");
            Menu currentMenu = null;
            for (String menuTitle : menuTitles)
            {
                currentMenu = findMenu(currentMenu, menuTitle);
            }
            addMenuItem(currentMenu, component, annotation);
        }
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

    private void addMenuItem(Menu currentMenu, Class component, TopMenu annotation)
    {
        if(currentMenu != null)
        {
            MenuItem menuItem = (MenuItem)Ioc.context().find(component);
            if(annotation.icon() != null && !annotation.icon().isEmpty())
            {
                Image image = new Image(component.getResourceAsStream(annotation.icon()));
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(18);
                imageView.setFitWidth(18);
                menuItem.setGraphic(imageView);
            }
            currentMenu.getItems().add( menuItem );
        }
    }
}
