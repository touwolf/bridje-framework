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

package org.bridje.orm;

import org.bridje.ioc.thls.ThlsAction;
import org.bridje.ioc.thls.ThlsActionException;
import org.bridje.ioc.thls.ThlsActionException2;

/**
 * The work environment for the ORM API.
 */
public interface ORMEnvironment
{
    /**
     * Gets the instance of the given model for its class.
     *
     * @param <T>      The type of the model.
     * @param modelCls The model class.
     *
     * @return The instance of the model, or nul if it does not exists.
     */
    <T> T getModel(Class<T> modelCls);

    /**
     * Performs the given ThlsAction with this environment in the current thread
     * local storage.
     *
     * @param <T>    The type of the result for the action.
     * @param action The action to perform.
     *
     * @return The result of the action.
     */
    <T> T doWith(ThlsAction<T> action);

    /**
     * Performs the given ThlsAction with this environment in the current thread
     * local storage.
     *
     * @param <T>    The type of the result for the action.
     * @param <E>    The type of the exception thrown by the action.
     * @param action The action to perform.
     *
     * @return The result of the action.
     *
     * @throws E The exception thrown by the action.
     */
    <T, E extends Throwable> T doWithEx(ThlsActionException<T, E> action) throws E;

    /**
     * Performs the given ThlsAction with this environment in the current thread
     * local storage.
     *
     * @param <T>    The type of the result for the action.
     * @param <E>    The type of the exception thrown by the action.
     * @param <E2>   The type of the second exception thrown by the action.
     * @param action The action to perform.
     *
     * @return The result of the action.
     *
     * @throws E  The exception thrown by the action.
     * @throws E2 The second exception thrown by the action.
     */
    <T, E extends Throwable, E2 extends Throwable> T doWithEx2(ThlsActionException2<T, E, E2> action) throws E, E2;

}
