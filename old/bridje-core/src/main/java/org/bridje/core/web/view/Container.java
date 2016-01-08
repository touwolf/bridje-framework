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

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlTransient
public abstract class Container extends BasicComponent
{
    public abstract List<Component> getChilds();
    
    boolean isAbstract()
    {
        List<Component> childs = getChilds();
        for (Component child : childs)
        {
            if(child instanceof Define)
            {
                return true;
            }
            else if(child instanceof Container)
            {
                if( ((Container)child).isAbstract() )
                {
                    return true;
                }
            }
        }
        return false;
    }
    
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
    
    void injectFragments(UIContext context)
    {
        List<Component> childs = getChilds();
        for (Component child : childs)
        {
            if(child instanceof Container)
            {
                ((Container)child).injectFragments(context);
            }
        }
    }
}
