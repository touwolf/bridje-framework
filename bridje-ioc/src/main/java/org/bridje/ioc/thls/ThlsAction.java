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
 * {@link ThlsService#doAs() doAs}
 * method.
 * <p>
 * @param <T> The type of the returning object for the action.
 */
@FunctionalInterface
public interface ThlsAction<T>
{
    /**
     * Executes the action. this method is called on the
     * {@link ThlsService#doAs() doAs}
     * method.
     * <p>
     * @return The action result.
     * @throws java.lang.Exception Any exception throw by the action.
     */
    T execute() throws Exception;
}
