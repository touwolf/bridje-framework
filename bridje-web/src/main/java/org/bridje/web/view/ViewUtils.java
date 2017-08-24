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

package org.bridje.web.view;

public class ViewUtils
{
    /**
     * 
     * 
     * @param name
     * @return 
     */
    public static String simplifyParam(String name)
    {
        StringBuilder sb = new StringBuilder();
        char[] chars = name.toCharArray();
        boolean addNext = true;
        boolean canAdd = true;
        for (char ch : chars)
        {
            if(Character.isAlphabetic((int)ch) || Character.isDigit(ch) || ch == '.')
            {
                if(addNext)
                {
                    sb.append(ch);
                    canAdd = false;
                    addNext = false;
                }

                if(ch == '.')
                {
                    addNext = true;
                }
                else if(Character.isUpperCase(ch) || Character.isDigit(ch))
                {
                    if(canAdd)
                    {
                        sb.append(ch);
                        canAdd = false;
                    }
                }
                else
                {
                    canAdd = true;
                }
            }
        }
        return sb.toString().toLowerCase();
    }
}
