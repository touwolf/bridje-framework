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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.util.ReferenceCounted;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.bridje.http.HttpBridletRequest;
import org.bridje.http.HttpCookie;
import org.bridje.http.HttpReqParam;
import org.bridje.http.UploadedFile;

class HttpBridletRequestImpl implements HttpBridletRequest
{
    private ByteBuf buffer;

    private final HttpRequest headers;

    private String[] headersArr;

    private InputStream inputStream;

    private final List<FileUpload> uploadedFiles;

    private String contentType;

    private Map<String, HttpReqParamImpl> postParameters;

    private Map<String, HttpReqParamImpl> getParameters;

    private String[] postParamsNames;

    private String[] getParamsNames;

    private Map<String, HttpCookie> cookies;

    private String[] cookiesNames;

    private String path;
    
    private final String clientIp;

    public HttpBridletRequestImpl(HttpRequest headers, String clientIp)
    {
        this.headers = headers;
        this.uploadedFiles = new ArrayList<>();
        this.clientIp = clientIp;
    }

    @Override
    public InputStream getInputStream()
    {
        if(inputStream == null)
        {
            if(buffer != null)
            {
                inputStream = new ByteBufInputStream(buffer);
            }
            else
            {
                inputStream = new ByteArrayInputStream("".getBytes());
            }
        }
        return inputStream;
    }

    public ByteBuf getBuffer()
    {
        return buffer;
    }

    protected void setContent(ByteBuf content)
    {
        if(content.isReadable())
        {
            if(this.buffer != null) 
            {
                this.buffer = Unpooled.copiedBuffer(this.buffer, content);
            }
            else
            {
                this.buffer = Unpooled.copiedBuffer(content);
            }
        }
    }

    @Override
    public String getMethod()
    {
        return this.headers.method().name();
    }

    @Override
    public String getProtocol()
    {
        return this.headers.protocolVersion().text();
    }

    @Override
    public String getHost()
    {
        return this.headers.headers().get("Host");
    }

    @Override
    public String getUserAgent()
    {
        return this.headers.headers().get("User-Agent");
    }

    @Override
    public String getAccept()
    {
        return this.headers.headers().get("Accept");
    }

    @Override
    public String getAcceptLanguage()
    {
        return this.headers.headers().get("Accept-Language");
    }

    @Override
    public String getPath()
    {
        if(path == null)
        {
            path = this.headers.uri();
            if(path.contains("?"))
            {
                path = path.substring(0, path.indexOf("?"));
            }
        }
        return path;
    }

    @Override
    public String getHeader(String header)
    {
        return this.headers.headers().get(header);
    }

    @Override
    public String[] getHeaders()
    {
        if(headersArr == null)
        {
            Set<String> names = headers.headers().names();
            headersArr = new String[names.size()];
            names.toArray(headersArr);
        }
        return headersArr;
    }

    protected void release()
    {
        uploadedFiles.forEach(ReferenceCounted::release);
        if(this.buffer != null)
        {
            this.buffer.release();
        }
    }

    @Override
    public String getContentType()
    {
        if(contentType == null)
        {
            contentType = getHeader(HttpHeaders.Names.CONTENT_TYPE);
            if(contentType != null)
            {
                String[] split = contentType.split(";");
                if(split.length > 0)
                {
                    contentType = split[0];
                }
            }
            else
            {
                contentType = "";
            }
        }
        return contentType;
    }

    @Override
    public boolean isGet()
    {
        return getMethod().equals(HttpMethod.GET.name());
    }

    @Override
    public boolean isPost()
    {
        return getMethod().equals(HttpMethod.POST.name());
    }

    @Override
    public boolean isDelete()
    {
        return getMethod().equals(HttpMethod.DELETE.name());
    }

    @Override
    public boolean isPut()
    {
        return getMethod().equals(HttpMethod.PUT.name());
    }

    @Override
    public boolean isPatch()
    {
        return getMethod().equals(HttpMethod.PATCH.name());
    }

    @Override
    public boolean isForm()
    {
        return isWwwForm() || isMultipartForm();
    }

    @Override
    public boolean isWwwForm()
    {
        String ctType = getContentType();
        return HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString().equalsIgnoreCase(ctType);
    }

    @Override
    public boolean isMultipartForm()
    {
        String ctType = getContentType();
        return HttpHeaderValues.MULTIPART_FORM_DATA.toString().equalsIgnoreCase(ctType);
    }

    protected void addFileUpload(FileUpload fileUpload)
    {
        fileUpload.retain();
        this.uploadedFiles.add(fileUpload);
    }

    protected void addPostParameter(String name, String value)
    {
        if(postParameters == null) postParameters = new HashMap<>();
        HttpReqParamImpl param = postParameters.computeIfAbsent(name, k -> new HttpReqParamImpl(name));
        param.addValue(value);
    }

    @Override
    public Map<String, HttpReqParam> getPostParameters()
    {
        if(postParameters == null) return Collections.emptyMap();
        return Collections.unmodifiableMap(postParameters);
    }

    @Override
    public HttpReqParam getPostParameter(String parameter)
    {
        return getPostParameters().get(parameter);
    }

    @Override
    public String[] getPostParametersNames()
    {
        if(postParamsNames == null)
        {
            postParamsNames = new String[getPostParameters().size()];
            getPostParameters().keySet().toArray(postParamsNames);
        }
        return postParamsNames;
    }

    @Override
    public UploadedFile[] getAllUploadedFiles()
    {
        UploadedFile[] result = new UploadedFile[uploadedFiles.size()];
        return uploadedFiles.stream()
                .map(UploadedFileImpl::new)
                .collect(Collectors.toList()).toArray(result);
    }
    
    @Override
    public UploadedFile getUploadedFile(String name)
    {
        return uploadedFiles.stream()
                .filter(f -> name.equalsIgnoreCase(f.getName()))
                .map(UploadedFileImpl::new)
                .findFirst().orElse(null);
    }

    @Override
    public Map<String, HttpReqParam> getGetParameters()
    {
        if(getParameters == null) return Collections.emptyMap();
        return Collections.unmodifiableMap(getParameters);
    }

    @Override
    public HttpReqParam getGetParameter(String parameter)
    {
        return getGetParameters().get(parameter);
    }
    
    @Override
    public String[] getGetParametersNames()
    {
        if(getParamsNames == null)
        {
            getParamsNames = new String[getGetParameters().size()];
            getGetParameters().keySet().toArray(getParamsNames);
        }
        return getParamsNames;
    }

    protected void setQueryString(Map<String, List<String>> parameters)
    {
        this.getParameters = new HashMap<>();
        parameters.forEach((name, values) -> 
        {
            HttpReqParamImpl par = new HttpReqParamImpl(name, values);
            this.getParameters.put(name, par);
        });
    }

    protected void setCookies(Set<Cookie> parseCookies)
    {
        cookies = parseCookies.stream()
                .map(HttpCookieImpl::new)
                .collect(Collectors.toMap(HttpCookie::getName, (c) -> (HttpCookie)c));
    }

    @Override
    public Map<String, HttpCookie> getCookies()
    {
        if(getParameters == null) return Collections.emptyMap();
        return Collections.unmodifiableMap(cookies);
    }

    @Override
    public HttpCookie getCookie(String name)
    {
        return cookies.get(name);
    }

    @Override
    public String[] getCookiesNames()
    {
        if(cookiesNames == null)
        {
            cookiesNames = new String[cookies.size()];
            cookies.keySet().toArray(cookiesNames);
        }
        return cookiesNames;
    }

    @Override
    public String getClientIp()
    {
        return clientIp;
    }
}
