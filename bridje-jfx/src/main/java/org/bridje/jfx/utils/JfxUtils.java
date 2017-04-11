
package org.bridje.jfx.utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

public class JfxUtils
{
    public static Node loadImage(Class<?> cls, String resource, double height, double width)
    {
        Image img = new Image(cls.getResourceAsStream(resource));
        ImageView iv = new ImageView(img);
        iv.setFitHeight(height);
        iv.setFitWidth(width);
        return iv;
    }

    public static MenuItem createMenuItem(String title, Node icon, EventHandler<ActionEvent> handler)
    {
        MenuItem mi = new MenuItem(title, icon);
        mi.setOnAction(handler);
        return mi;
    }

    public static Button createToolButton(Node icon, EventHandler<ActionEvent> handler)
    {
        Button mi = new Button("", icon);
        mi.setOnAction(handler);
        return mi;
    }
}
