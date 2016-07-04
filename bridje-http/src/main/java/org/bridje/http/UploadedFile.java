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

package org.bridje.http;

import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a file uploaded during a HTTP request.
 */
public interface UploadedFile
{
    /**
     * Gets the original file name selected by the user in the browser.
     *
     * @return the name of the uploaded file.
     */
    String getFilename();

    /**
     * Gets the name of the HTML form field where this file was uploaded.
     *
     * @return The name of the HTML form field for this file.
     */
    String getName();

    /**
     * Opens a new InputStream to read the content of the file.
     *
     * @return An InputStream to read the content of the file.
     * @throws IOException If any I/O exception occurs
     */
    InputStream getInputStream() throws IOException;

    /**
     * Gets the mime/type for the file.
     *
     * @return The mime/type for the file.
     */
    String getContentType();
}
