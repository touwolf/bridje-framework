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

package org.bridje.core.cfg;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Represents an adapter witch can be use to read/write configuration objects.
 */
public interface ConfigurationAdapter
{
    /**
     * 
     * @return 
     */
    String findDefaultName(Class<?> cls);
    
    /**
     * Writes the configuration object to the especified writer.
     *
     * @param newConfig The configuration object to write.
     * @param writer The writer to write to.
     * @throws java.io.IOException If the writing operation failed.
     */
    void write(Object newConfig, Writer writer) throws IOException;

    /**
     * Reads the especified configuration class into an object.
     *
     * @param cls The configuration class to read.
     * @param reader The reader to read from.
     * @return The configuration object readed.
     * @throws java.io.IOException If the reading operation failed.
     */
    Object read(Class<?> cls, Reader reader) throws IOException;
}
