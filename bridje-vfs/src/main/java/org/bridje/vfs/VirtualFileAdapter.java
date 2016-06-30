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

package org.bridje.vfs;

import java.io.IOException;

/**
 * 
 */
public interface VirtualFileAdapter
{
    /**
     * 
     * @return 
     */
    String[] getExtensions();

    /**
     * 
     * @return 
     */
    Class<?>[] getClasses();
    
    /**
     * 
     * @param vf
     * @param resultCls
     * @return 
     */
    boolean canHandle(VirtualFile vf, Class<?> resultCls);

    /**
     * 
     * @param <T>
     * @param vf
     * @param resultCls
     * @return
     * @throws IOException 
     */
    <T> T read(VirtualFile vf, Class<T> resultCls) throws IOException;

    /**
     * 
     * @param <T>
     * @param vf
     * @param contentObj
     * @throws IOException 
     */
    <T> void write(VirtualFile vf, T contentObj) throws IOException;
}
