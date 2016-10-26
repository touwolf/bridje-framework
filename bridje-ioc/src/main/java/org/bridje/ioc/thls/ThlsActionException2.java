/*
 * Copyright 2015 Bridje Framework.
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

package org.bridje.ioc.thls;

/**
 * Represents an action that can be executed and can access to the data of the
 * current thread, that was made available on the
 * {@link ThlsService#doAs(org.bridje.ioc.thls.ThlsAction, java.lang.Class, java.lang.Object) doAs}
 * method.
 *
 * @param <T>  The type of the returning object for the action.
 * @param <E>  The type of the first exception throw by the execution of this
 *             action.
 * @param <E2> The type of the second exception throw by the execution of this
 *             action.
 */
@FunctionalInterface
public interface ThlsActionException2<T, E extends Throwable, E2 extends Throwable>
{
    /**
     * Executes the action. this method is called on the
     * {@link ThlsService#doAs(org.bridje.ioc.thls.ThlsAction, java.lang.Class, java.lang.Object) doAs}
     * method.
     *
     * @return The action result.
     *
     * @throws E  The first exception throw by the aciton.
     * @throws E2 The second exception throw by the action.
     */
    T execute() throws E, E2;

}
