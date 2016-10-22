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
package org.bridje.vfs.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

class MimeTypeUtils
{
    private static final Logger LOG = Logger.getLogger(MimeTypeUtils.class.getName());

    private static MimeTypeUtils INSTANCE;

    public synchronized static MimeTypeUtils getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new MimeTypeUtils();
            INSTANCE.initMimeTypes();
        }
        return INSTANCE;
    }

    private Map<String, String> mimeTypes;

    public String getMimeType(String extension)
    {
        if (mimeTypes.containsKey(extension))
        {
            return mimeTypes.get(extension);
        }
        return "text/plain";
    }

    private synchronized void initMimeTypes()
    {
        mimeTypes = new HashMap<>();

        try(InputStream is = getClass().getResourceAsStream("/BRIDJE-INF/vfs/mime-types.properties"))
        {
            Properties prop = new Properties();
            prop.load(is);
            prop.forEach((key, value) -> mimeTypes.put((String)key, (String)value));
        }
        catch(IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
