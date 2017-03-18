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
 * This interface defines the services for source code generation in the bridje
 * modules.
 */
public interface SrcGenService
{
    /**
     * The virtual path for the data in the source code generation process.
     */
    Path DATA_PATH = new Path("/srcgen/data");

    /**
     * The virtual path for the suplementary data in the source code generation
     * process.
     */
    Path SUPL_PATH = new Path("/srcgen/supl");

    /**
     * The virtual path for the generated resources in the source code
     * generation process.
     */
    Path RESOURCE_PATH = new Path("/srcgen/resources");

    /**
     * The virtual path for the generated classes in the source code generation
     * process.
     */
    Path CLASSES_PATH = new Path("/srcgen/classes");

    /**
     * The virtual path for the templates in the source code generation process.
     */
    Path TEMPLATES_PATH = new Path("/srcgen/templates");

    /**
     * Finds the data by the given class, this method will read the data from
     * the default virtual path in the VFS tree and parse all the files that
     * have the content of the given class.
     *
     * @param <T> The type of the data to read.
     * @param cls The class of the data to read.
     *
     * @return A map with the data readed as the key and the corresponding
     *         VFile.
     *
     * @throws IOException If any IO exception occurs.
     */
    <T> Map<T, VFile> findData(Class<T> cls) throws IOException;

    /**
     * Finds the suplementary data by the given class, this method will read the
     * data from the default virtual path in the VFS tree and parse all the
     * files that have the content of the given class.
     *
     * @param <T> The type of the data to read.
     * @param cls The class of the data to read.
     *
     * @return A map with the data readed as the key and the corresponding
     *         VFile.
     *
     * @throws IOException
     */
    <T> List<T> findSuplData(Class<T> cls) throws IOException;

    /**
     * Create a class with the given data and the given template.
     * 
     * @param clsFullName The full name of the class to create.
     * @param tplPath The path of the template to use to create the class.
     * @param data The data to use by the template to create the class.
     *
     * @throws IOException If any IO exception occurs.
     */
    void createClass(String clsFullName, String tplPath, Object data) throws IOException;

    /**
     * Creates a new resource with the given data and the given template.
     * 
     * @param resourcePath The path and name of the resource file to create.
     * @param tplPath The path of the template to use to create the class.
     * @param data The data to use by the template to create the class.
     *
     * @throws IOException If any IO exception occurs.
     */
    void createResource(String resourcePath, String tplPath, Object data) throws IOException;

    /**
     *
     */
    void showDataEditor();

}
