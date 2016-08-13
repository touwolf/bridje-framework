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
import java.net.URISyntaxException;
import org.bridje.ioc.Ioc;
import org.bridje.vfs.VfsService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class WebViewTest
{
    
    public WebViewTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp() throws IOException, URISyntaxException
    {
        Ioc.context().find(VfsService.class).mountResource("/web", "/BRIDJE-INF/web/public");
    }
    
    @After
    public void tearDown()
    {
    }

    @Test
    public void testReadView() throws IOException
    {
        WebView view = Ioc.context().find(WebViewsManager.class).findView("/index");
        Assert.assertNotNull(view);
        Assert.assertNotNull(view.getRoot());
    }
}
