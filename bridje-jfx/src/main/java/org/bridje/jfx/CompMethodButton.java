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

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.bridje.ioc.IocContext;

class CompMethodButton extends Button
{
    private static final Logger LOG = Logger.getLogger(CompMethodButton.class.getName());

    private Object compObj;

    public CompMethodButton(Method method, Class component, ToolBarAction annotation, IocContext context)
    {
        super(annotation.title());
        ImageView imgView = new ImageView(new Image(component.getResourceAsStream(annotation.icon())));
        imgView.setFitHeight(18);
        imgView.setFitWidth(18);
        setGraphic(imgView);
        setOnAction((e) -> {
            if(compObj == null)
            {
                compObj = context.find(component);
            }
            if(compObj != null)
            {
                try
                {
                    method.invoke(compObj);
                }
                catch (Exception ex)
                {
                    LOG.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        });
    }
}
