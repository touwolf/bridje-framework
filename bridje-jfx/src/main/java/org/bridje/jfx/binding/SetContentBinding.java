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

package org.bridje.jfx.binding;

import java.lang.ref.WeakReference;
import java.util.Set;
import javafx.beans.WeakListener;
import javafx.collections.SetChangeListener;

/**
 * A content binding for two sets of diferent types.
 * 
 * @param <E> The type of the first list.
 * @param <T> The type of the second list.
 */
public class SetContentBinding<E, T> implements SetChangeListener<E>, WeakListener
{
    private final WeakReference<Set<T>> setRef;

    private final ContentConverter<E, T> converter;

    /**
     * The only constructor for this object.
     * 
     * @param set The set to update.
     * @param converter The converter for the type of the second set.
     */
    public SetContentBinding(Set<T> set, ContentConverter<E, T> converter)
    {
        this.setRef = new WeakReference<>(set);
        this.converter = converter;
    }

    @Override
    public void onChanged(SetChangeListener.Change<? extends E> change)
    {
        final Set<T> set = setRef.get();
        if (set == null)
        {
            change.getSet().removeListener(this);
        }
        else
        {
            if (change.wasRemoved())
            {
                set.remove(converter.convert(change.getElementRemoved()));
            }
            else
            {
                set.add(converter.convert(change.getElementAdded()));
            }
        }
    }

    @Override
    public boolean wasGarbageCollected()
    {
        return setRef.get() == null;
    }

    @Override
    public int hashCode()
    {
        final Set<T> set = setRef.get();
        return (set == null) ? 0 : set.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;

        final Set<T> set1 = setRef.get();
        if (set1 == null) return false;

        if (obj instanceof SetContentBinding)
        {
            final SetContentBinding<?, ?> other = (SetContentBinding<?, ?>) obj;
            final Set<?> set2 = other.setRef.get();
            return set1 == set2;
        }
        return false;
    }

}
