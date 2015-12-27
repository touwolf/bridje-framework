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

package org.bridje.core.impl.vfs.cfg;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import org.bridje.core.vfs.VfsMountEntry;

@XmlRootElement(name = "vfs")
@XmlAccessorType(XmlAccessType.FIELD)
public class VfsConfig
{
    @XmlElements(
    {
        @XmlElement(name = "file", type = FileConfig.class),
        @XmlElement(name = "classpath", type = ClassPathConfig.class)
    })
    private List<MountEntryFactory> entries;

    public List<MountEntryFactory> getEntries()
    {
        return entries;
    }

    public void setEntries(List<MountEntryFactory> entries)
    {
        this.entries = entries;
    }
    
    public List<VfsMountEntry> createEntries()
    {
        List<VfsMountEntry> result = new LinkedList<>();
        for (MountEntryFactory entry : entries)
        {
            result.add(entry.createMountEntry());
        }
        return result;
    }
}
