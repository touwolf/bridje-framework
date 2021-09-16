/*
 * Copyright 2018 Bridje Framework.
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

package org.bridje.web.view.themes;

import java.io.*;
import org.bridje.http.HttpBridletResponse;

public class ThemeResourceData
{
    private String contentType;

    private byte[] data;

    public ThemeResourceData(String contentType, InputStream dataIs) throws IOException
    {
        this.contentType = contentType;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream())
        {
            try (InputStream is = dataIs)
            {
                copy(is, os);
                os.flush();
            }
            data = os.toByteArray();
        }
    }

    public ThemeResourceData(String contentType, byte[] data)
    {
        this.contentType = contentType;
        this.data = data;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public byte[] getData()
    {
        return data;
    }

    public void setData(byte[] data)
    {
        this.data = data;
    }

    void serve(HttpBridletResponse resp) throws IOException
    {
        resp.setContentType(getContentType());
        try (OutputStream os = resp.getOutputStream())
        {
            try (InputStream is = new ByteArrayInputStream(getData()))
            {
                copy(is, os);
                os.flush();
            }
        }
    }

    private void copy(InputStream is, OutputStream os) throws IOException
    {
        byte[] buffer = new byte[1024];
        int bytesCount = is.read(buffer);
        while (bytesCount > -1)
        {
            os.write(buffer, 0, bytesCount);
            bytesCount = is.read(buffer);
        }
    }
}
