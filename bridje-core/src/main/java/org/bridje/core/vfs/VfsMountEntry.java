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

package org.bridje.core.vfs;

import java.util.Map;

public class VfsMountEntry
{
    private String mountPath;
    
    private String fileSystem;

    private Map<String,String> properties;

    public VfsMountEntry()
    {
    }

    public VfsMountEntry(String mountPath, String fileSystem)
    {
        this.mountPath = mountPath;
        this.fileSystem = fileSystem;
    }
    
    public VfsMountEntry(String mountPath, String fileSystem, Map<String, String> properties)
    {
        this.mountPath = mountPath;
        this.fileSystem = fileSystem;
        this.properties = properties;
    }
    
    public String getMountPath()
    {
        return mountPath;
    }

    public void setMountPath(String mountPath)
    {
        this.mountPath = mountPath;
    }

    public String getFileSystem()
    {
        return fileSystem;
    }

    public void setFileSystem(String fileSystem)
    {
        this.fileSystem = fileSystem;
    }

    public Map<String, String> getProperties()
    {
        return properties;
    }

    public void setProperties(Map<String, String> properties)
    {
        this.properties = properties;
    }
}
