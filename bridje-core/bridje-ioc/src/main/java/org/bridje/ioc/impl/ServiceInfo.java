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

package org.bridje.ioc.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author Gilberto
 */
class ServiceInfo
{
    private final Class mainClass;

    private final Class[] paramClasess;

    public ServiceInfo(Class mainClass, Class[] paramClasess)
    {
        this.mainClass = mainClass;
        this.paramClasess = paramClasess;
    }

    public Class getMainClass()
    {
        return mainClass;
    }

    public Class[] getParamClasess()
    {
        return paramClasess;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.mainClass);
        if(this.paramClasess != null)
        {
            hash = 53 * hash + Arrays.deepHashCode(this.paramClasess);
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final ServiceInfo other = (ServiceInfo) obj;
        if (!Objects.equals(this.mainClass, other.mainClass))
        {
            return false;
        }
        if(paramClasess == null && other.paramClasess == null)
        {
            return true;
        }
        if(paramClasess != null && other.paramClasess != null)
        {
            if (Arrays.deepEquals(this.paramClasess, other.paramClasess))
            {
                return true;
            }            
        }
        return false;
    }

    public static ServiceInfo createServiceInf(Type type)
    {
        if(type instanceof ParameterizedType)
        {
            ParameterizedType pt = (ParameterizedType)type;
            Type[] paramsArr = pt.getActualTypeArguments();
            if(paramsArr != null && paramsArr.length > 0)
            {
                Class[] clsArr = new Class[paramsArr.length];
                for (int i = 0; i < paramsArr.length; i++)
                {
                    clsArr[i] = (Class)paramsArr[i];
                }
                ServiceInfo result = new ServiceInfo((Class)pt.getRawType(), clsArr);
                return result;
            }
        }
        return new ServiceInfo((Class)type, null);
    }
}
