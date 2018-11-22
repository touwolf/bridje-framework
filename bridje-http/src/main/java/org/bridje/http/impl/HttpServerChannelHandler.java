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

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Names.COOKIE;
import static io.netty.handler.codec.http.HttpHeaders.Names.SERVER;
import static io.netty.handler.codec.http.HttpHeaders.Names.SET_COOKIE;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import io.netty.handler.codec.http.HttpUtil;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.DiskAttribute;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.http.HttpBridletContext;
import org.bridje.http.HttpBridletRequest;
import org.bridje.http.HttpBridletResponse;
import org.bridje.ioc.Ioc;

class HttpServerChannelHandler extends SimpleChannelInboundHandler<HttpObject>
{
    private static final Logger LOG = Logger.getLogger(HttpServerChannelHandler.class.getName());

    private HttpBridletContext context;

    private HttpBridletRequestImpl req;

    private HttpBridletResponseImpl resp;

    private final HttpServerImpl server;

    private HttpPostRequestDecoder decoder;

    private static final HttpDataFactory FACTORY = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE); // Disk if size exceed

    static
    {
        DiskFileUpload.deleteOnExitTemporaryFile = true; // should delete file
                                                         // on exit (in normal
                                                         // exit)
        DiskFileUpload.baseDirectory = null; // system temp directory
        DiskAttribute.deleteOnExitTemporaryFile = true; // should delete file on
                                                        // exit (in normal exit)
        DiskAttribute.baseDirectory = null; // system temp directory
    }

    public HttpServerChannelHandler(HttpServerImpl server)
    {
        this.server = server;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws IOException
    {
        if(!msg.decoderResult().isSuccess())
        {
            LOG.log(Level.WARNING, "Decode result was not success.");
            sendBadRequest(ctx);
            return;
        }
        if(msg instanceof HttpRequest)
        {
            HttpRequest httpReq = (HttpRequest)msg;
            if(HttpUtil.is100ContinueExpected(httpReq))
            {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
                ctx.flush();
            }
            readHeaders(ctx, httpReq);
        }
        else if(msg instanceof HttpContent)
        {
            HttpContent httpCont = (HttpContent)msg;
            readContent(ctx, httpCont);
        }
        else
        {
            sendBadRequest(ctx);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        LOG.log(Level.SEVERE, cause.getMessage(), cause);
        ctx.close();
        closeAll();
    }

    private void sendResponse(ChannelHandlerContext ctx) throws IOException
    {
        resp.close();
        int length = resp.getBuffer().readableBytes();
        DefaultHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.valueOf(resp.getStatusCode()), resp.getBuffer());
        resp.setHeader(SERVER, server.getServerName());
        resp.setHeader(CONTENT_TYPE, resp.getContentType());
        resp.setHeader(CONTENT_LENGTH, length);
        resp.setHeader(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        Map<String, Object> headers = resp.getHeadersMap();
        for (Map.Entry<String, Object> entry : headers.entrySet())
        {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value != null)
            {
                if(value instanceof Iterable)
                {
                    response.headers().set(key, (Iterable<?>)value);
                }
                else
                {
                    response.headers().set(key, value);
                }
            }
        }
        writeCookies(response);
        ctx.write(response);
        ctx.flush();
        closeAll();
    }

    private void sendBadRequest(ChannelHandlerContext ctx)
    {
        LOG.log(Level.WARNING, "Bad Request Received....");
        DefaultHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST);
        response.headers().set(SERVER, server.getServerName());
        response.headers().set(CONTENT_TYPE, "text/html");
        response.headers().set(CONTENT_LENGTH, 0);
        response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        ctx.write(response);
        ctx.flush();
        closeAll();
    }

    private void readHeaders(ChannelHandlerContext ctx, HttpRequest msg)
    {
        if(req == null && context == null)
        {
            context = new HttpBridletContextImpl();
            SocketAddress socketAddr = ctx.channel().remoteAddress();
            req = new HttpBridletRequestImpl( msg, socketAddr.toString() );
            QueryStringDecoder decoderQuery = new QueryStringDecoder(msg.uri());
            req.setQueryString(decoderQuery.parameters());
            req.setCookies(parseCookies(msg.headers().get(COOKIE)));

            if(req.isForm()) decoder = new HttpPostRequestDecoder(FACTORY, msg);
        }
        else
        {
            LOG.log(Level.WARNING, "Request headers where alrready readed, sending bad request.");
            sendBadRequest(ctx);
        }
    }

    private void readContent(ChannelHandlerContext ctx, HttpContent msg) throws IOException
    {
        if(req != null)
        {
            if(req.isForm())
            {
                try
                {
                    decoder.offer(msg);
                    readHttpDataChunkByChunk();
                }
                catch(HttpPostRequestDecoder.EndOfDataDecoderException ex)
                {
                    handleRequest(ctx);
                    return;
                }
                catch (DecoderException e)
                {
                    LOG.log(Level.WARNING, String.format("Decoder Exception: %s sending bad request.", e.getMessage()));
                    sendBadRequest(ctx);
                    return;
                }
            }
            else
            {
                req.setContent(msg.content());
            }

            //if is the last http content
            if(msg instanceof LastHttpContent)
            {
                handleRequest(ctx);
            }
        }
        else
        {
            LOG.log(Level.WARNING, "The header was not readed, sending bad request.");
            sendBadRequest(ctx);
        }
    }

    private void readHttpDataChunkByChunk() throws IOException
    {
        while (decoder.hasNext())
        {
            InterfaceHttpData data = decoder.next();
            if (data != null) writeHttpData(data);
        }
    }

    private void writeHttpData(InterfaceHttpData data) throws IOException
    {
        if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute)
        {
            Attribute attribute = (Attribute) data;
            String value = attribute.getValue();

            if (value.length() > 65535)
            {
                throw new IOException("Data too long");
            }
            req.addPostParameter(attribute.getName(), value);
        }
        else
        {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload)
            {
                FileUpload fileUpload = (FileUpload) data;
                req.addFileUpload(fileUpload);
            }
        }
    }

    private void handleRequest(ChannelHandlerContext ctx) throws IOException
    {
        if(resp == null) resp = new HttpBridletResponseImpl(ctx.alloc().buffer());
        RootHttpBridlet rootHandler = Ioc.context().find(RootHttpBridlet.class);
        context.set(HttpBridletRequest.class, req);
        context.set(HttpBridletResponse.class, resp);
        rootHandler.handle(context);
        sendResponse(ctx);
    }

    private void closeAll()
    {
        if(resp != null) resp.release();
        if(req != null) req.release();
        context = null;
        req = null;
        resp = null;
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx)
    {
        if (decoder != null)
        {
            decoder.cleanFiles();
        }
    }

    public Set<Cookie> parseCookies(String cookiesHeader)
    {
        Set<Cookie> cookies;
        if (cookiesHeader == null)
        {
            cookies = Collections.emptySet();
        }
        else
        {
            cookies = ServerCookieDecoder.STRICT.decode(cookiesHeader);
        }
        return cookies;
    }

    private void writeCookies(DefaultHttpResponse response)
    {
        if (resp.getCookies() != null && !resp.getCookies().isEmpty())
        {
            // Reset the cookies if necessary.
            resp.getCookies().forEach((name, cookie) ->
            {
                response.headers()
                        .add(SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie.getInternalCookie()));
            });
        }
    }
}
