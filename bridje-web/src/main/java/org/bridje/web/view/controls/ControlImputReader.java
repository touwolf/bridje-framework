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

package org.bridje.web.view.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bridje.http.HttpBridletRequest;
import org.bridje.http.HttpReqParam;
import org.bridje.http.UploadedFile;

/**
 * Utility object that allows to read the input values from the HTTP request.
 */
public class ControlImputReader
{
    private final Map<String,List<UploadedFile>> filesMap;

    private final Map<String,List<HttpReqParam>> parametersMap;

    /**
     * Default constructor.
     * 
     * @param req The HTTP request.
     */
    public ControlImputReader(HttpBridletRequest req)
    {
        filesMap = new LinkedHashMap<>();
        parametersMap = new LinkedHashMap<>();
        UploadedFile[] files = req.getAllUploadedFiles();
        for (UploadedFile file : files)
        {
            List<UploadedFile> lst = filesMap.get(file.getName());
            if(lst == null)
            {
                lst = new ArrayList<>();
                filesMap.put(file.getName(), lst);
            }
            lst.add(file);
        }
        Map<String, HttpReqParam> postParameters = req.getPostParameters();
        for (Entry<String, HttpReqParam> param : postParameters.entrySet())
        {
            List<HttpReqParam> lst = parametersMap.get(param.getKey());
            if(lst == null)
            {
                lst = new ArrayList<>();
                parametersMap.put(param.getKey(), lst);
            }
            lst.addAll(Arrays.asList(param.getValue().separate()));
        }
    }

    /**
     * Gets the given uploaded file.
     * 
     * @param parameter The name of the uploaded file.
     * @return The uploaded file or null if it does not exists.
     */
    public UploadedFile getUploadedFile(String parameter)
    {
        List<UploadedFile> result = filesMap.get(parameter);
        if(result != null && !result.isEmpty()) return result.get(0);
        return null;
    }

    /**
     * Reads and remove an uploaded file parameter.
     * 
     * @param parameter The name of the uploaded file.
     * @return The uploaded file or null if it does not exists.
     */
    public UploadedFile popUploadedFile(String parameter)
    {
        List<UploadedFile> result = filesMap.get(parameter);
        if(result != null && !result.isEmpty())
        {
            UploadedFile file = result.get(0);
            if(file != null) result.remove(0);
            if(result.isEmpty()) filesMap.remove(parameter);
            return file;
        }
        return null;
    }

    /**
     * Reads the given parameter from the request.
     * 
     * @param parameter The name of the parameter.
     * @return The readed parameter.
     */
    public HttpReqParam getParameter(String parameter)
    {
        List<HttpReqParam> result = parametersMap.get(parameter);
        if(result != null && !result.isEmpty()) return result.get(0);
        return null;
    }

    /**
     * Reads and remove the given parameter from the request.
     * 
     * @param parameter The name of the parameter.
     * @return The readed parameter.
     */
    public HttpReqParam popParameter(String parameter)
    {
        List<HttpReqParam> result = parametersMap.get(parameter);
        if(result != null && !result.isEmpty())
        {
            HttpReqParam param = result.get(0);
            if(param != null) result.remove(0);
            if(result.isEmpty()) parametersMap.remove(parameter);
            return param;
        }
        return null;
    }
}
