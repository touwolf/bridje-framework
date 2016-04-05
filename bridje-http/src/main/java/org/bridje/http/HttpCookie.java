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

/**
 * An interface defining an
 * <a href="http://en.wikipedia.org/wiki/HTTP_cookie">HTTP cookie</a>.
 */
public interface HttpCookie extends Comparable<HttpCookie>
{
    /**
     * Returns the name of this {@link HttpCookie}.
     *
     * @return The name of this {@link HttpCookie}
     */
    String getName();

    /**
     * Returns the value of this {@link HttpCookie}.
     *
     * @return The value of this {@link HttpCookie}
     */
    String getValue();

    /**
     * Sets the value of this {@link HttpCookie}.
     *
     * @param value The value to set
     */
    void setValue(String value);

    /**
     * Returns true if the raw value of this {@link HttpCookie},
     * was wrapped with double quotes in original Set-Cookie header.
     *
     * @return If the value of this {@link HttpCookie} is to be wrapped
     */
    boolean getWrap();

    /**
     * Sets true if the value of this {@link HttpCookie}
     * is to be wrapped with double quotes.
     *
     * @param wrap true if wrap
     */
    void setWrap(boolean wrap);

    /**
     * Returns the domain of this {@link HttpCookie}.
     *
     * @return The domain of this {@link HttpCookie}
     */
    String getDomain();

    /**
     * Sets the domain of this {@link HttpCookie}.
     *
     * @param domain The domain to use
     */
    void setDomain(String domain);

    /**
     * Returns the path of this {@link HttpCookie}.
     *
     * @return The {@link HttpCookie}'s path
     */
    String getPath();

    /**
     * Sets the path of this {@link HttpCookie}.
     *
     * @param path The path to use for this {@link HttpCookie}
     */
    void setPath(String path);

    /**
     * Returns the maximum age of this {@link HttpCookie} in seconds or {@link Long#MIN_VALUE} if unspecified
     *
     * @return The maximum age of this {@link HttpCookie}
     */
    long getMaxAge();

    /**
     * Sets the maximum age of this {@link HttpCookie} in seconds.
     * If an age of {@code 0} is specified, this {@link HttpCookie} will be
     * automatically removed by browser because it will expire immediately.
     * If {@link Long#MIN_VALUE} is specified, this {@link HttpCookie} will be removed when the
     * browser is closed.
     *
     * @param maxAge The maximum age of this {@link HttpCookie} in seconds
     */
    void setMaxAge(long maxAge);

    /**
     * Checks to see if this {@link HttpCookie} is secure
     *
     * @return True if this {@link HttpCookie} is secure, otherwise false
     */
    boolean isSecure();

    /**
     * Sets the security getStatus of this {@link HttpCookie}
     *
     * @param secure True if this {@link HttpCookie} is to be secure, otherwise false
     */
    void setSecure(boolean secure);

    /**
     * Checks to see if this {@link HttpCookie} can only be accessed via HTTP.
     * If this returns true, the {@link HttpCookie} cannot be accessed through
     * client side script - But only if the browser supports it.
     * For more information, please look <a href="http://www.owasp.org/index.php/HTTPOnly">here</a>
     *
     * @return True if this {@link HttpCookie} is HTTP-only or false if it isn't
     */
    boolean isHttpOnly();

    /**
     * Determines if this {@link HttpCookie} is HTTP only.
     * If set to true, this {@link HttpCookie} cannot be accessed by a client
     * side script. However, this works only if the browser supports it.
     * For for information, please look
     * <a href="http://www.owasp.org/index.php/HTTPOnly">here</a>.
     *
     * @param httpOnly True if the {@link HttpCookie} is HTTP only, otherwise false.
     */
    void setHttpOnly(boolean httpOnly);
}
