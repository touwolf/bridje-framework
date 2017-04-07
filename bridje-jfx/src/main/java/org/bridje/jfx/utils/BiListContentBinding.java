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

package org.bridje.jfx.utils;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.WeakListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 *
 * @author gilbe
 * @param <E>
 * @param <T>
 */
public class BiListContentBinding<E, T> implements ListChangeListener, WeakListener
{

    private final WeakReference<ObservableList<E>> propertyRef1;

    private final WeakReference<ObservableList<T>> propertyRef2;

    private boolean updating = false;

    private final BiContentConverter<E, T> converter;
    
    public BiListContentBinding(ObservableList<E> list1, ObservableList<T> list2, BiContentConverter<E, T> converter)
    {
        propertyRef1 = new WeakReference<>(list1);
        propertyRef2 = new WeakReference<>(list2);
        this.converter = converter;
    }

    @Override
    public void onChanged(ListChangeListener.Change change)
    {
        if (!updating)
        {
            final ObservableList<E> list1 = propertyRef1.get();
            final ObservableList<T> list2 = propertyRef2.get();
            if ((list1 == null) || (list2 == null))
            {
                if (list1 != null)
                {
                    list1.removeListener(this);
                }
                if (list2 != null)
                {
                    list2.removeListener(this);
                }
            }
            else
            {
                try
                {
                    updating = true;
                    final ObservableList<?> dest = (list1 == change.getList()) ? list2 : list1;
                    while (change.next())
                    {
                        if (change.wasPermutated())
                        {
                            dest.remove(change.getFrom(), change.getTo());
                            if(list1 == change.getList())
                            {
                                list2.addAll(change.getFrom(), convertFrom(list1.subList(change.getFrom(), change.getTo())));
                            }
                            else
                            {
                                list1.addAll(change.getFrom(), convertTo(list2.subList(change.getFrom(), change.getTo())));
                            }
                        }
                        else
                        {
                            if (change.wasRemoved())
                            {
                                dest.remove(change.getFrom(), change.getFrom() + change.getRemovedSize());
                            }
                            if (change.wasAdded())
                            {
                                if(list1 == change.getList())
                                {
                                    list2.addAll(change.getFrom(), convertFrom(change.getAddedSubList()));
                                }
                                else
                                {
                                    list1.addAll(change.getFrom(), convertTo(change.getAddedSubList()));
                                }
                            }
                        }
                    }
                }
                finally
                {
                    updating = false;
                }
            }
        }
    }

    @Override
    public boolean wasGarbageCollected()
    {
        return (propertyRef1.get() == null) || (propertyRef2.get() == null);
    }

    @Override
    public int hashCode()
    {
        final ObservableList<E> list1 = propertyRef1.get();
        final ObservableList<T> list2 = propertyRef2.get();
        final int hc1 = (list1 == null) ? 0 : list1.hashCode();
        final int hc2 = (list2 == null) ? 0 : list2.hashCode();
        return hc1 * hc2;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;

        final Object propertyA1 = propertyRef1.get();
        final Object propertyA2 = propertyRef2.get();
        if ((propertyA1 == null) || (propertyA2 == null)) return false;

        if (obj instanceof BiListContentBinding)
        {
            final BiListContentBinding otherBinding = (BiListContentBinding) obj;
            final Object propertyB1 = otherBinding.propertyRef1.get();
            final Object propertyB2 = otherBinding.propertyRef2.get();
            if ((propertyB1 == null) || (propertyB2 == null)) return false;

            if ((propertyA1 == propertyB1) && (propertyA2 == propertyB2)) return true;
            if ((propertyA1 == propertyB2) && (propertyA2 == propertyB1)) return true;
        }
        return false;
    }

    public List<T> convertFrom(List<E> subList)
    {
        return subList.stream()
                        .map(e -> converter.convertFrom(e))
                        .collect(Collectors.toList());
    }

    public List<E> convertTo(List<T> subList)
    {
        return subList.stream()
                        .map(e -> converter.convertTo(e))
                        .collect(Collectors.toList());
    }
}
