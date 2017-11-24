/*
 * Copyright 2017 Bridje Framework.
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

package org.bridje.jfx.ace;

/**
 * Handler for replace operation on editor.
 */
public interface AceReplaceHandler
{
    /**
     * Called when replace action was called on editor.
     *
     * @param text the selected text in editor to replace
     *
     * @return the value that was gathered to replace.
     */
    String replace(String text);

}
