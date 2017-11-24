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

/**
 * Defines a meta information for the current view.
 */
public interface MetaTag
{
    /**
     * The name of the meta tag.
     * 
     * @return The name of the meta tag.
     */
    String getName();

    /**
     * The name of the meta tag.
     * 
     * @param name The name of the meta tag.
     */
    void setName(String name);

    /**
     * The content of the meta tag.
     * 
     * @return The content of the meta tag.
     */
    String getContent();

    /**
     * The name of the meta tag.
     * 
     * @param content The content of the meta tag.
     */
    void setContent(String content);
}
