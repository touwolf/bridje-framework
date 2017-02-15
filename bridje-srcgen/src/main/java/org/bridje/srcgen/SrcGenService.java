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

package org.bridje.srcgen;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.bridje.vfs.Path;
import org.bridje.vfs.VFile;

/**
 * 
 */
public interface SrcGenService
{
    /**
     * 
     */
    Path DATA_PATH = new Path("/srcgen/data");

    /**
     * 
     */
    Path SUPL_PATH = new Path("/srcgen/supl");

    /**
     * 
     */
    Path RESOURCE_PATH = new Path("/srcgen/resources");

    /**
     * 
     */
    Path CLASSES_PATH = new Path("/srcgen/classes");

    /**
     * 
     */
    Path TEMPLATES_PATH = new Path("/srcgen/templates");

    /**
     * 
     * @param <T>
     * @param cls
     * @return
     * @throws IOException 
     */
    <T> Map<T, VFile> findData(Class<T> cls) throws IOException;

    /**
     * 
     * @param <T>
     * @param cls
     * @return
     * @throws IOException 
     */
    <T> List<T> findSuplData(Class<T> cls) throws IOException;

    /**
     * 
     * @param clsFullName
     * @param tplPath
     * @param data
     * @throws IOException 
     */
    void createClass(String clsFullName, String tplPath, Object data) throws IOException;

    /**
     * 
     * @param resourcePath
     * @param tplPath
     * @param data
     * @throws IOException 
     */
    void createResource(String resourcePath, String tplPath, Object data) throws IOException;

    /**
     * 
     */
    void showDataEditor();
}
