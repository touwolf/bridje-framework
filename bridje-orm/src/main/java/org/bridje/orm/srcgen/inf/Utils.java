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

package org.bridje.orm.srcgen.inf;

public class Utils
{
    public static String toSQLName(String name)
    {
        String result = name;
        result = result.replace("A", "_a");
        result = result.replace("B", "_b");
        result = result.replace("C", "_c");
        result = result.replace("D", "_d");
        result = result.replace("E", "_e");
        result = result.replace("F", "_f");
        result = result.replace("G", "_g");
        result = result.replace("H", "_h");
        result = result.replace("I", "_i");
        result = result.replace("J", "_j");
        result = result.replace("K", "_k");
        result = result.replace("L", "_l");
        result = result.replace("M", "_m");
        result = result.replace("N", "_n");
        result = result.replace("O", "_o");
        result = result.replace("P", "_p");
        result = result.replace("Q", "_q");
        result = result.replace("R", "_r");
        result = result.replace("S", "_s");
        result = result.replace("T", "_t");
        result = result.replace("U", "_u");
        result = result.replace("V", "_v");
        result = result.replace("W", "_w");
        result = result.replace("X", "_x");
        result = result.replace("Y", "_y");
        result = result.replace("Z", "_z");
        if(result.startsWith("_")) result = result.substring(1);
        return result;
    }
}
