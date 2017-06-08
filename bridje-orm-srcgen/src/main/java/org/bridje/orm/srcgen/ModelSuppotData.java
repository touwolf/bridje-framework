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

import com.github.javaparser.ast.CompilationUnit;
import java.util.ArrayList;
import java.util.List;

public class ModelSuppotData
{
    private final List<CompilationUnit> compilationUnits;
    
    private List<String> allImports;

    public ModelSuppotData(List<CompilationUnit> compilationUnits)
    {
        this.compilationUnits = compilationUnits;
    }

    public List<String> getAllImports()
    {
        if(allImports == null)
        {
            allImports = new ArrayList<>();
            compilationUnits.stream().forEach(cu -> addImports(cu));
        }
        return allImports;
    }

    private void addImports(CompilationUnit cu)
    {
        cu.getImports().stream()
                .map(i -> i.toString())
                .filter(i -> !allImports.contains(i))
                .forEach(i -> allImports.add(i));
    }
}
