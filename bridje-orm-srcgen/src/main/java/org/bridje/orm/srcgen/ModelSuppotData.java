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
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import org.bridje.orm.srcgen.model.ModelInf;

/**
 * The data for model support class.
 */
public class ModelSuppotData
{
    private final ModelInf ormModel;

    private final List<CompilationUnit> compilationUnits;

    private List<String> allImports;

    private List<MethodDecInf> allMethods;

    private List<ClassOrInterfaceDeclaration> allClasses;

    /**
     * Default constructor.
     * 
     * @param ormModel The current procesing ORM model.
     * @param compilationUnits The list of parsed classes for the current project.
     */
    public ModelSuppotData(ModelInf ormModel, List<CompilationUnit> compilationUnits)
    {
        this.compilationUnits = compilationUnits;
        this.ormModel = ormModel;
    }

    /**
     * Get all the current classes.
     * 
     * @return The list of all classes in the current project.
     */
    public List<ClassOrInterfaceDeclaration> getAllClasses()
    {
        if(allClasses == null)
        {
            allClasses = new ArrayList<>();
            compilationUnits.stream().forEach(this::addClasses);
        }
        return allClasses;
    }

    /**
     * Get all support methods of all the support classes.
     * 
     * @return The list with all the support methods.
     */
    public List<MethodDecInf> getAllMethods()
    {
        if(allMethods == null)
        {
            allMethods = new ArrayList<>();
            compilationUnits.stream().forEach(this::addMethods);
        }
        return allMethods;
    }

    /**
     * Gets all the imports, used in the support classes.
     * 
     * @return The list of imports.
     */
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
            allImports.add("import " + cu.getPackageDeclaration().get().getName() + "." + ((ClassOrInterfaceDeclaration)type).getName() + ";\n");
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
                        .filter(m -> isPublic(m.getModifiers()) && !isStatic(m.getModifiers()))
                        .filter(m -> !m.getParameters().isEmpty())
                        .filter(m -> firstParamIsModelClass(m))
                        .map(m -> new MethodDecInf(clsDec, m))
                        .forEach(allMethods::add);
    }

    private boolean firstParamIsModelClass(MethodDeclaration m)
    {
        Parameter param = m.getParameters().get(0);
        String type = param.getType().toString();
        String name = param.getName().asString();
        if(type.equals(ormModel.getName()) || type.equals(ormModel.getFullName()))
        {
            m.getParameters().remove(0);
            if(m.getJavadocComment().isPresent() && m.getJavadocComment().get().getContent() != null)
            {
                String[] lines = m.getJavadocComment().get().getContent().split("\\n");
                if(lines != null && lines.length > 0)
                {
                    String comment = Arrays.asList(lines)
                                            .stream()
                                            .filter(s -> !s.contains("* @param " + name))
                                            .collect(Collectors.joining("\n"));
                    m.setJavadocComment(comment);
                }
            }
            return true;
        }
        return false;
    }

    private boolean isPublic(EnumSet<Modifier> modifiers)
    {
       return modifiers.contains(Modifier.PUBLIC);
    }

    private boolean isStatic(EnumSet<Modifier> modifiers)
    {
        return modifiers.contains(Modifier.STATIC);
    }
}
