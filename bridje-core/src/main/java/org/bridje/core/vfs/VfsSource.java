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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface VfsSource
{
    Object[] getFiles(Path path) throws IOException;

    List<String> listFolders(Path path) throws IOException;

    List<String> listFiles(Path path) throws IOException;

    boolean fileExists(Path path) throws IOException;

    boolean folderExists(Path path) throws IOException;
    
    InputStream open(Object data);
}
