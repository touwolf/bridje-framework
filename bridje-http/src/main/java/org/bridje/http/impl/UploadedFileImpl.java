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

package org.bridje.http.impl;

import io.netty.buffer.ByteBufInputStream;
import io.netty.handler.codec.http.multipart.FileUpload;
import java.io.IOException;
import java.io.InputStream;
import org.bridje.http.UploadedFile;
/**
 *
 */
public class UploadedFileImpl implements UploadedFile
{
    private final FileUpload fileUpload;
    
    protected UploadedFileImpl(FileUpload fileUpload)
    {
        this.fileUpload = fileUpload;
    }

    @Override
    public String getFilename()
    {
        return fileUpload.getFilename();
    }

    @Override
    public String getName()
    {
        return fileUpload.getName();
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
        return new ByteBufInputStream(fileUpload.getByteBuf());
    }

    @Override
    public String getContentType()
    {
        return fileUpload.getContentType();
    }

}
