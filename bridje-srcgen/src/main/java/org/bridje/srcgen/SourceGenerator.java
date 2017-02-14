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
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

/**
 * 
 * @param <T>
 */
public interface SourceGenerator<T>
{
    /**
     * 
     * @return 
     */
    String getName();

    /**
     * 
     * 
     * @return 
     */
    List<T> findData();
    
    /**
     * 
     * 
     * @param data
     * @return 
     */
    String getName(T data);
    
    /**
     * 
     * 
     * @param data
     * @throws IOException
     */
    void generateSources(T data) throws IOException;
    
    /**
     * 
     * @param data
     * @return 
     */
    TreeItem<T> createTreeNode(T data);

    /**
     * 
     * @return 
     */
    ImageView getImage();
}
