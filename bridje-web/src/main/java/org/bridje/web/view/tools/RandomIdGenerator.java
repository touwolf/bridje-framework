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

package org.bridje.web.view.tools;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

/**
 * A random id generator for HTML elements.
 */
public class RandomIdGenerator
{
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    
    private static final String ALL = "0123456789abcdefghijklmnopqrstuvwxyz";

    private static final SecureRandom RND = new SecureRandom();
    
    private final Set<String> prev = new HashSet<>();

    public String randomId()
    {
        String str = randomString(RND.nextInt(15));
        while(prev.contains(str))
        {
            str = randomString(RND.nextInt(15));
        }
        prev.add(str);
        return str;
    }

    private String randomString(int len)
    {
        StringBuilder sb = new StringBuilder(len);
        sb.append(ALPHA.charAt(RND.nextInt(ALPHA.length())));
        for (int i = 0; i < len; i++)
        {
            sb.append(ALL.charAt(RND.nextInt(ALL.length())));
        }
        return sb.toString();
    }
}
