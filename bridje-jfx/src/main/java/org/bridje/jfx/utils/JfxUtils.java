/*
 * Copyright 2017 Bridje Framework.
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

package org.bridje.jfx.utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Utilities for javafx applications.
 */
public class JfxUtils
{
    /**
     * Loads an image from the given resource, and creates an ImageView for it.
     * 
     * @param cls The class to load the resource.
     * @param resource The resource name or path.
     * @param height The height of the ImageView
     * @param width The width of the ImageView
     * @return The new ImageView.
     */
    public static ImageView loadImage(Class<?> cls, String resource, double height, double width)
    {
        Image img = new Image(cls.getResourceAsStream(resource));
        ImageView iv = new ImageView(img);
        iv.setFitHeight(height);
        iv.setFitWidth(width);
        return iv;
    }

    /**
     * Create an item for a menu.
     * 
     * @param title The title of the item.
     * @param icon The icon of the item.
     * @param handler The handler for the item.
     * @return The new created MenuItem.
     */
    public static MenuItem createMenuItem(String title, ImageView icon, EventHandler<ActionEvent> handler)
    {
        MenuItem mi = new MenuItem(title, icon);
        mi.setOnAction(handler);
        return mi;
    }

    /**
     * Create a button with no text, only with the image.
     * 
     * @param icon The icon of the button.
     * @param handler The handler for the button.
     * @return The new created Button.
     */
    public static Button createToolButton(ImageView icon, EventHandler<ActionEvent> handler)
    {
        Button mi = new Button("", icon);
        mi.setOnAction(handler);
        return mi;
    }
}
