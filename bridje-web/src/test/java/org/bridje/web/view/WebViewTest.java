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

package org.bridje.web.view;

import java.io.IOException;
import org.bridje.ioc.Ioc;
import org.bridje.web.view.test.controls.Header;
import org.bridje.web.view.test.controls.VerticalLayout;
import org.junit.Assert;
import org.junit.Test;

public class WebViewTest
{
    @Test
    public void testReadView() throws IOException
    {
        WebView view = Ioc.context().find(WebViewsManager.class).findView("/public/index");
        Assert.assertNotNull(view);
        Assert.assertNotNull(view.getRoot());
    }

    @Test
    public void testReadExtView() throws IOException
    {
        WebView view = Ioc.context().find(WebViewsManager.class).findView("/public/exts");
        Assert.assertNotNull(view);
        Assert.assertNotNull(view.getRoot());
        Assert.assertTrue(view.getRoot() instanceof VerticalLayout);
        VerticalLayout vlayout = (VerticalLayout)view.getRoot();
        Assert.assertNotNull(vlayout.getChildren());
        Assert.assertEquals(1, vlayout.getChildren().size());
        Assert.assertTrue(vlayout.getChildren().get(0) instanceof Header);
    }
}
