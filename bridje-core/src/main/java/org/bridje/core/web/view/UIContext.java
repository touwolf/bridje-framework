/*
 * Copyright 2015 Bridje Framework.
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

package org.bridje.core.web.view;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import org.bridje.vfs.VirtualFile;
import org.bridje.vfs.VirtualFolder;

public class UIContext
{
    private static final Logger LOG = Logger.getLogger(UIContext.class.getName());

    private final VirtualFolder rootFolder;

    public UIContext(VirtualFolder rootFolder)
    {
        this.rootFolder = rootFolder;
    }

    public List<Fragment> findFragments(String category)
    {
        List<Fragment> fragments = new LinkedList<>();
        loadFragments(category, rootFolder, fragments);
        return fragments;
    }
    
    public void loadFragments(String category, VirtualFolder folder, List<Fragment> fragments)
    {
        folder.travel((VirtualFile file) -> {
            if(file.getName().endsWith(".fragment.xml"))
            {
                Fragment fragment = AbstractView.loadFragment(file);
                if(fragment != null)
                {
                    String[] categs = fragment.getCategory();
                    for (String categ : categs)
                    {
                        if(categ.equalsIgnoreCase(category))
                        {
                            fragments.add(fragment);
                        }
                    }
                }
            }
        });
    }
    
    public Page findPage(String pagePath)
    {
        Page page = loadPage(pagePath);
        if(page != null && !page.isAbstract())
        {
            Page injPage = injectFragments(page);
            Page assPage = assamblePage(injPage);
            
            return assPage;
        }
        return null;
    }

    private Page loadPage(String pagePath)
    {
        VirtualFile file = rootFolder.findFile(pagePath + ".page.xml");
        if(file == null)
        {
            return null;
        }
        return AbstractView.loadPage(file);
    }

    private Page assamblePage(Page page)
    {
        if(page.getContent() instanceof Extends)
        {
            Extends ext = (Extends)page.getContent();
            String from = ext.getFrom();
            Page loadPage = loadPage(from);
            if(loadPage != null)
            {
                Page injPage = injectFragments(loadPage);
                Page assPage = assamblePage(injPage);
                if(assPage != null)
                {
                    Component content = assPage.getContent();
                    if(content instanceof Container)
                    {
                        ((Container)content).assamble(ext);
                    }
                    return assPage;
                }
            }
            return null;
        }
        return page;
    }

    private Page injectFragments(Page page)
    {
        if(page.getContent() instanceof Container)
        {
            ((Container)page.getContent()).injectFragments(this);
        }
        return page;
    }
}
