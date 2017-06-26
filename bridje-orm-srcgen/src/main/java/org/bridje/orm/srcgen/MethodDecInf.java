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

package org.bridje.orm.srcgen;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

/**
 * Information for method declaration.
 */
public class MethodDecInf
{
    private final ClassOrInterfaceDeclaration classDec;
    
    private final MethodDeclaration method;

    /**
     * Default constructor.
     * 
     * @param classDec The class of the method.
     * @param method The method.
     */
    public MethodDecInf(ClassOrInterfaceDeclaration classDec, MethodDeclaration method)
    {
        this.classDec = classDec;
        this.method = method;
    }
    
    /**
     * Gets the class this method belongs to.
     * 
     * @return The class this method belongs to.
     */
    public ClassOrInterfaceDeclaration getClassDec()
    {
        return classDec;
    }

    /**
     * Gets the method declaration.
     * 
     * @return The method declaration.
     */
    public MethodDeclaration getMethod()
    {
        return method;
    }
}
