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
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bridje.orm.srcgen.model.ModelInf;

public class ModelSuppotData
{
    private final ModelInf ormModel;

    private final List<CompilationUnit> compilationUnits;
    
    private List<String> allImports;
    
    private List<MethodDecInf> allMethods;
    
    private List<ClassOrInterfaceDeclaration> allClasses;

    public ModelSuppotData(ModelInf ormModel, List<CompilationUnit> compilationUnits)
    {
        this.compilationUnits = compilationUnits;
        this.ormModel = ormModel;
    }

    public List<ClassOrInterfaceDeclaration> getAllClasses()
    {
        if(allClasses == null)
        {
            allClasses = new ArrayList<>();
            compilationUnits.stream().forEach(this::addClasses);
        }
        return allClasses;
    }
    
    public List<MethodDecInf> getAllMethods()
    {
        if(allMethods == null)
        {
            allMethods = new ArrayList<>();
            compilationUnits.stream().forEach(this::addMethods);
        }
        return allMethods;
    }

    public List<String> getAllImports()
    {
        if(allImports == null)
        {
            allImports = new ArrayList<>();
            compilationUnits.stream().forEach(this::addImports);
        }
        return allImports;
    }

    private void addImports(CompilationUnit cu)
    {
        cu.getImports().stream()
                .map(i -> i.toString())
                .filter(i -> !allImports.contains(i))
                .forEach(allImports::add);
        TypeDeclaration type = cu.getTypes().get(0);
        if(type instanceof ClassOrInterfaceDeclaration)
        {
            allImports.add("import " + cu.getPackage().getPackageName() + "." + ((ClassOrInterfaceDeclaration)type).getName() + ";\n");
        }
    }

    private void addClasses(CompilationUnit cu)
    {
        TypeDeclaration type = cu.getTypes().get(0);
        if(type instanceof ClassOrInterfaceDeclaration)
        {
            allClasses.add((ClassOrInterfaceDeclaration)type);
        }
    }

    private void addMethods(CompilationUnit cu)
    {
        TypeDeclaration type = cu.getTypes().get(0);
        if(type instanceof ClassOrInterfaceDeclaration)
        {
            addMethods(cu, (ClassOrInterfaceDeclaration)type);
        }
    }

    private void addMethods(CompilationUnit cu, ClassOrInterfaceDeclaration clsDec)
    {
        clsDec.getMembers().stream()
                        .filter(f -> f instanceof MethodDeclaration)
                        .map(f -> (MethodDeclaration)f)
                        .filter(m -> ModifierSet.isPublic(m.getModifiers()) && !ModifierSet.isStatic(m.getModifiers()))
                        .filter(m -> !m.getParameters().isEmpty())
                        .filter(m -> firstParamIsModelClass(m))
                        .map(m -> new MethodDecInf(clsDec, m))
                        .forEach(allMethods::add);
    }

    private boolean firstParamIsModelClass(MethodDeclaration m)
    {
        Parameter param = m.getParameters().get(0);
        String type = param.getType().toString();
        String name = param.getName();
        if(type.equals(ormModel.getName()) || type.equals(ormModel.getFullName()))
        {
            m.getParameters().remove(0);
            String[] lines = m.getJavaDoc().getContent().split("\\n");
            String comment = Arrays.asList(lines).stream()
                                    .map(s -> s.toString())
                                    .filter(s -> !s.contains("* @param " + name))
                                    .collect(Collectors.joining("\n"));
            m.getJavaDoc().setContent(comment);
            return true;
        }
        return false;
    }
}
