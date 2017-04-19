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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.WeakListener;
import javafx.collections.ListChangeListener;

public class ListContentBinding<E, T> implements ListChangeListener<E>, WeakListener
{
    private final WeakReference<List<T>> listRef;
    
    private final ContentConverter<E, T> converter;

    public ListContentBinding(List<T> list, ContentConverter<E, T> converter)
    {
        this.listRef = new WeakReference<>(list);
        this.converter = converter;
    }

    @Override
    public void onChanged(ListChangeListener.Change<? extends E> change)
    {
        final List<T> list = listRef.get();
        if (list == null)
        {
            change.getList().removeListener(this);
        }
        else
        {
            while (change.next())
            {
                if (change.wasPermutated())
                {
                    list.subList(change.getFrom(), change.getTo()).clear();
                    list.addAll(change.getFrom(), convert(change.getList().subList(change.getFrom(), change.getTo())));
                }
                else
                {
                    if (change.wasRemoved())
                    {
                        list.subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
                    }
                    if (change.wasAdded())
                    {
                        list.addAll(change.getFrom(), convert(change.getAddedSubList()));
                    }
                }
            }
        }
    }

    @Override
    public boolean wasGarbageCollected()
    {
        return listRef.get() == null;
    }

    @Override
    public int hashCode()
    {
        final List<T> list = listRef.get();
        return (list == null) ? 0 : list.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        final List<T> list1 = listRef.get();
        if (list1 == null) return false;

        if (obj instanceof ListContentBinding)
        {
            final ListContentBinding<?, ?> other = (ListContentBinding<?, ?>) obj;
            final List<?> list2 = other.listRef.get();
            return list1 == list2;
        }
        return false;
    }

    private Collection<? extends T> convert(List<? extends E> subList)
    {
        return subList.stream()
                        .map(e -> converter.convert(e))
                        .collect(Collectors.toList());
    }
}
