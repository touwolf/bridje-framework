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

import java.sql.JDBCType;
import java.sql.SQLException;

/**
 * Represents an SQL type.
 *
 * @param <T> The actual java type for this SQL type.
 * @param <E> The read java type for this SQL type.
 */
public interface SQLType<T, E>
{
    /**
     * A boolean SQL type.
     */
    SQLType<Boolean, Boolean> BOOLEAN = SQL.buildType(Boolean.class, JDBCType.BOOLEAN);

    /**
     * A long SQL type.
     */
    SQLType<Long, Long> LONG = SQL.buildType(Long.class, JDBCType.BIGINT);

    /**
     * An integer SQL type.
     */
    SQLType<Integer, Integer> INTEGER = SQL.buildType(Integer.class, JDBCType.INTEGER);

    /**
     * A short SQL type.
     */
    SQLType<Short, Short> SHORT = SQL.buildType(Short.class, JDBCType.SMALLINT);

    /**
     * A char SQL type.
     */
    SQLType<Character, Character> CHAR = SQL.buildType(Character.class, JDBCType.CHAR);

    /**
     * An string SQL type.
     */
    SQLType<String, String> STRING = SQL.buildType(String.class, JDBCType.VARCHAR, 255);

    /**
     * The actual java type.
     *
     * @return The actual java type.
     */
    Class<T> getJavaType();

    /**
     * The read java type.
     *
     * @return The read java type.
     */
    Class<E> getJavaReadType();

    /**
     * The JDBC type.
     *
     * @return The JDBC type.
     */
    JDBCType getJDBCType();

    /**
     * The length for string and numerical types.
     *
     * @return The length for string and numerical types.
     */
    int getLength();

    /**
     * The precision for numerical types.
     *
     * @return The precision for numerical types.
     */
    int getPrecision();

    /**
     * Gets an SQL param out of this type.
     *
     * @return an SQL param expression.
     */
    Expression<T, E> asParam();

    /**
     * Gets the parser used by this type.
     *
     * @return The parser used by this type.
     */
    SQLValueParser<T, E> getParser();

    /**
     * The writer used by this type.
     *
     * @return The writer used by this type.
     */
    SQLValueWriter<E, T> getWriter();

    /**
     * Parses the given object to the final java type.
     *
     * @param object The raw database object.
     *
     * @return The final object.
     *
     * @throws SQLException If any SQL error occurs
     */
    T parse(E object) throws SQLException;

    /**
     * Reads the raw object given to the read object, so it can be parsed.
     *
     * @param object The raw object.
     *
     * @return The read object to use in the parsing.
     *
     * @throws SQLException If any SQL error occurs
     */
    E read(Object object) throws SQLException;

    /**
     * Writes the given object to the database object.
     *
     * @param object The final object of this type.
     *
     * @return The resulting SQL ready object.
     */
    E write(T object);
}
