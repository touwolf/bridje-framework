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

package org.bridje.sql;

import java.util.List;

/**
 * 
 */
public interface SQLBuilder
{
    /**
     * 
     * @return 
     */
    boolean isSimpleColumnNames();

    /**
     * 
     * @param simpleColumnNames 
     */
    void setSimpleColumnNames(boolean simpleColumnNames);

    /**
     * 
     * @return 
     */
    SQLDialect getDialect();

    /**
     * 
     * @return 
     */
    List<Object> getParameters();

    /**
     * 
     * @param str
     * @return 
     */
    SQLBuilder append(String str);

    /**
     * 
     * @param s
     * @return 
     */
    SQLBuilder append(CharSequence s);

    /**
     * 
     * @param c
     * @return 
     */
    SQLBuilder append(char c);

    /**
     * 
     * @param i
     * @return 
     */
    SQLBuilder append(int i);

    /**
     * 
     * @param lng
     * @return 
     */
    SQLBuilder append(long lng);

    /**
     * 
     * @param f
     * @return 
     */
    SQLBuilder append(float f);

    /**
     * 
     * @param d
     * @return 
     */
    SQLBuilder append(double d);

    /**
     * 
     * @param name
     * @return 
     */
    SQLBuilder appendObjectName(String name);

    /**
     * 
     * @param offset
     * @param count
     * @return 
     */
    SQLBuilder appendLimit(int offset, int count);

    /**
     * 
     * @param expression
     * @return 
     */
    SQLBuilder append(SQLWritable expression);

    /**
     * 
     * @param expressions
     * @param sep 
     */
    void appendAll(SQLWritable[] expressions, String sep);
}
