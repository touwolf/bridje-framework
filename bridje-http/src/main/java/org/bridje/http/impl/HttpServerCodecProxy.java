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

package org.bridje.http.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerUpgradeHandler;
import io.netty.handler.codec.http.HttpStatusClass;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServerCodecProxy extends CombinedChannelDuplexHandler<HttpRequestDecoder, HttpResponseEncoder> implements HttpServerUpgradeHandler.SourceCodec
{
    private static final Logger LOG = Logger.getLogger(HttpServerCodecProxy.class.getName());

    /**
     * A queue that is used for correlating a request and a response.
     */
    private final Queue<HttpMethod> queue = new ArrayDeque<>();

    /**
     * Creates a new instance with the default decoder options
     * ({@code maxInitialLineLength (4096}}, {@code maxHeaderSize (8192)}, and
     * {@code maxChunkSize (8192)}).
     */
    public HttpServerCodecProxy()
    {
        this(4096, 8192, 8192);
    }

    /**
     * Creates a new instance with the specified decoder options.
     * @param maxInitialLineLength maxInitialLineLength
     * @param maxHeaderSize maxHeaderSize
     * @param maxChunkSize maxChunkSize
     */
    public HttpServerCodecProxy(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize)
    {
        init(new HttpServerRequestDecoder(maxInitialLineLength, maxHeaderSize, maxChunkSize),new HttpServerResponseEncoder());
    }

    /**
     * Creates a new instance with the specified decoder options.
     * @param maxInitialLineLength maxInitialLineLength
     * @param maxHeaderSize maxHeaderSize
     * @param validateHeaders validateHeaders
     * @param maxChunkSize maxChunkSize
     */
    public HttpServerCodecProxy(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders)
    {
        init(new HttpServerRequestDecoder(maxInitialLineLength, maxHeaderSize, maxChunkSize, validateHeaders), new HttpServerResponseEncoder());
    }

    /**
     * Creates a new instance with the specified decoder options.
     * @param maxInitialLineLength maxInitialLineLength
     * @param maxHeaderSize maxHeaderSize
     * @param maxChunkSize maxChunkSize
     * @param validateHeaders validateHeaders
     * @param initialBufferSize initialBufferSize
     */
    public HttpServerCodecProxy(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders, int initialBufferSize)
    {
        init(new HttpServerRequestDecoder(maxInitialLineLength, maxHeaderSize, maxChunkSize, validateHeaders, initialBufferSize), new HttpServerResponseEncoder());
    }

    /**
     * Upgrades to another protocol from HTTP. Removes the
     * {@link HttpRequestDecoder} and {@link HttpResponseEncoder} from the
     * pipeline.
     * @param ctx ctx
     */
    @Override
    public void upgradeFrom(ChannelHandlerContext ctx)
    {
        ctx.pipeline().remove(this);
    }

    private final class HttpServerRequestDecoder extends HttpRequestDecoder
    {
        public HttpServerRequestDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize)
        {
            super(maxInitialLineLength, maxHeaderSize, maxChunkSize);
        }

        public HttpServerRequestDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders)
        {
            super(maxInitialLineLength, maxHeaderSize, maxChunkSize, validateHeaders);
        }

        public HttpServerRequestDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders, int initialBufferSize)
        {
            super(maxInitialLineLength, maxHeaderSize, maxChunkSize, validateHeaders, initialBufferSize);
        }

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception
        {
            LOG.log(Level.INFO, "Decoding HTTP request: {0}", queue.size());
            int oldSize = out.size();
            super.decode(ctx, buffer, out);
            int size = out.size();
            for (int i = oldSize; i < size; i++)
            {
                Object obj = out.get(i);
                if (obj instanceof HttpRequest)
                {
                    queue.add(((HttpRequest) obj).method());
                }
            }
        }

    }

    private final class HttpServerResponseEncoder extends HttpResponseEncoder
    {
        private HttpMethod method;

        @Override
        protected void sanitizeHeadersBeforeEncode(HttpResponse msg, boolean isAlwaysEmpty)
        {
            if (!isAlwaysEmpty && method == HttpMethod.CONNECT && msg.status().codeClass() == HttpStatusClass.SUCCESS)
            {
                // Stripping Transfer-Encoding:
                // See https://tools.ietf.org/html/rfc7230#section-3.3.1
                msg.headers().remove(HttpHeaderNames.TRANSFER_ENCODING);
                return;
            }

            super.sanitizeHeadersBeforeEncode(msg, isAlwaysEmpty);
        }

        @Override
        protected boolean isContentAlwaysEmpty(@SuppressWarnings("unused") HttpResponse msg)
        {
            method = queue.poll();
            return HttpMethod.HEAD.equals(method) || super.isContentAlwaysEmpty(msg);
        }
    }
}
