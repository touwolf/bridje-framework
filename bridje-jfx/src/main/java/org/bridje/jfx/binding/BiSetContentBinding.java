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
import javafx.beans.WeakListener;
import javafx.collections.SetChangeListener;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener.Change;

/**
 *
 * @author gilbe
 * @param <E>
 * @param <T>
 */
public class BiSetContentBinding<E, T> implements SetChangeListener<Object>, WeakListener
{
    private final WeakReference<ObservableSet<E>> propertyRef1;

    private final WeakReference<ObservableSet<T>> propertyRef2;

    private boolean updating = false;

    private final BiContentConverter<E, T> converter;

    public BiSetContentBinding(ObservableSet<E> set1, ObservableSet<T> set2, BiContentConverter<E, T> converter)
    {
        propertyRef1 = new WeakReference<>(set1);
        propertyRef2 = new WeakReference<>(set2);
        this.converter = converter;
    }

    @Override
    public void onChanged(Change<? extends Object> change)
    {
        if (!updating)
        {
            final ObservableSet<E> set1 = propertyRef1.get();
            final ObservableSet<T> set2 = propertyRef2.get();
            if ((set1 == null) || (set2 == null))
            {
                if (set1 != null)
                {
                    set1.removeListener(this);
                }
                if (set2 != null)
                {
                    set2.removeListener(this);
                }
            }
            else
            {
                try
                {
                    updating = true;
                    if (change.wasRemoved())
                    {
                        if(set1 == change.getSet())
                        {
                            set2.remove(converter.convertFrom((E)change.getElementRemoved()));
                        }
                        else
                        {
                            set1.remove(converter.convertTo((T)change.getElementRemoved()));
                        }
                    }
                    else
                    {
                        if(set1 == change.getSet())
                        {
                            set2.add(converter.convertFrom((E)change.getElementAdded()));
                        }
                        else
                        {
                            set1.add(converter.convertTo((T)change.getElementAdded()));
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
        final ObservableSet<E> set1 = propertyRef1.get();
        final ObservableSet<T> set2 = propertyRef2.get();
        final int hc1 = (set1 == null) ? 0 : set1.hashCode();
        final int hc2 = (set2 == null) ? 0 : set2.hashCode();
        return hc1 * hc2;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;

        final Object propertyA1 = propertyRef1.get();
        final Object propertyA2 = propertyRef2.get();
        if ((propertyA1 == null) || (propertyA2 == null)) return false;

        if (obj instanceof BiSetContentBinding)
        {
            final BiSetContentBinding otherBinding = (BiSetContentBinding) obj;
            final Object propertyB1 = otherBinding.propertyRef1.get();
            final Object propertyB2 = otherBinding.propertyRef2.get();
            if ((propertyB1 == null) || (propertyB2 == null)) return false;

            if ((propertyA1 == propertyB1) && (propertyA2 == propertyB2)) return true;
            if ((propertyA1 == propertyB2) && (propertyA2 == propertyB1)) return true;
        }
        return false;
    }

}
