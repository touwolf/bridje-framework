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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class BorderLayout extends Container
{
    private HorizontalLayout top;
    
    private VerticalLayout right;
    
    private VerticalLayout left;
    
    private HorizontalLayout bottom;
    
    private VerticalLayout center;

    public BasicContainer getTop()
    {
        return top;
    }

    public BasicContainer getRight()
    {
        return right;
    }

    public BasicContainer getLeft()
    {
        return left;
    }

    public BasicContainer getBottom()
    {
        return bottom;
    }

    @Override
    public List<Component> getChilds()
    {
        List<Component> childs = new LinkedList<>();
        if(top != null)
        {
            childs.addAll(top.getChilds());
        }
        if(left != null)
        {
            childs.addAll(left.getChilds());
        }
        if(right != null)
        {
            childs.addAll(right.getChilds());
        }
        if(bottom != null)
        {
            childs.addAll(bottom.getChilds());
        }
        if(center != null)
        {
            childs.addAll(bottom.getChilds());
        }
        return childs;
    }

    @Override
    void assamble(Extends ext)
    {
        List<Component> childs = getChilds();
        for (Component child : childs)
        {
            if(child instanceof Container)
            {
                ((Container)child).assamble(ext);
            }
        }
    }
}
