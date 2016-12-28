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

package org.bridje.maven.plugin;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.bridje.ioc.Ioc;
import org.bridje.srcgen.SourceGenerator;
import org.bridje.srcgen.SrcGenService;
import org.bridje.vfs.FileSource;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VfsService;

/**
 * This MOJO is responsible for generating the code specified by the other APIs.
 * Each API defines the code that it needs and the user need`s only to define
 * the data for the code.
 */
@Mojo(
        name = "generate-sources"
)
public class GenerateMojo extends AbstractMojo
{
    @Parameter(defaultValue = "${project.basedir}/src/main/bridje", readonly = false)
    private File sourceFolder;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/bridje", readonly = false)
    private File targetFolder;

    @Parameter(defaultValue = "${project.build.directory}/generated-resources/bridje", readonly = false)
    private File targetResFolder;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            getLog().info("Generating Source Code");
            VfsService vfs = Ioc.context().find(VfsService.class);
            if(!sourceFolder.exists())
            {
                sourceFolder.mkdirs();
            }
            if(!targetResFolder.exists())
            {
                targetResFolder.mkdirs();
            }
            new VFile(SrcGenService.DATA_PATH).mount(new FileSource(sourceFolder));
            new VFile(SrcGenService.CLASSES_PATH).mount(new FileSource(targetFolder));
            new VFile(SrcGenService.RESOURCE_PATH).mount(new FileSource(targetResFolder));
            SourceGenerator[] generators = Ioc.context().findAll(SourceGenerator.class);
            for (SourceGenerator generator : generators)
            {
                generator.generateSources();
            }
            project.addCompileSourceRoot(targetFolder.getAbsolutePath());
            Resource res = new Resource();
            res.setDirectory(targetResFolder.getAbsolutePath());
            project.addResource(res);
        }
        catch (JAXBException | IOException e)
        {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    /**
     * Finds all the classes annotated tiwh the given annotation name, the key
     * of the map is te value of the given attribute.
     *
     * @param anntCls The simple name of the annotation.
     *
     * @return A map with the value of the annotations finded and the classes.
     *
     * @throws IOException If any io exception occurs parsing the code.
     */
    public Map<String, Map<String, String>> findProjectAnnotatedClasses(String anntCls) throws IOException
    {
        Map<String, Map<String, String>> result = new HashMap<>();
        findAllJavaFiles(path ->
        {
            try
            {
                CompilationUnit cu = JavaParser.parse(path.toFile());
                findAnnotation(cu, anntCls,
                        (annotExp, clsDec) ->
                {
                    Map<String, String> attrValue = findAttributes(annotExp);
                    if (attrValue != null)
                    {
                        result.put(cu.getPackage().getPackageName() + "." + clsDec.getName(), attrValue);
                    }
                });
            }
            catch (ParseException | IOException e)
            {
                getLog().error(e);
            }
        });
        return result;
    }

    private void findAnnotation(CompilationUnit cu, String annotCls, BiConsumer<? super NormalAnnotationExpr, ? super ClassOrInterfaceDeclaration> consumer)
    {
        new VoidVisitorAdapter<ClassOrInterfaceDeclaration>()
        {
            @Override
            public void visit(ClassOrInterfaceDeclaration clsDec, ClassOrInterfaceDeclaration arg)
            {
                clsDec.getAnnotations().stream()
                        .filter(annot -> annot instanceof NormalAnnotationExpr)
                        .map(annot -> (NormalAnnotationExpr) annot)
                        .filter(nae -> nae.getName().getName().equals(annotCls))
                        .forEach(nae -> consumer.accept(nae, clsDec));
            }

        }.visit(cu, null);
    }

    private void findAllJavaFiles(Consumer<? super Path> consumer) throws IOException
    {
        List compileSourceRoots = project.getCompileSourceRoots();
        for (Object compileSourceRoot : compileSourceRoots)
        {
            File root = new File((String) compileSourceRoot);
            Files.find(root.toPath(),
                    Integer.MAX_VALUE,
                    (t, u) -> t.getFileName().toString().endsWith(".java"))
                    .forEach(consumer);
        }
    }

    private Map<String, String> findAttributes(NormalAnnotationExpr annot)
    {
        Map<String, String> reuslt = new HashMap<>();
        List<MemberValuePair> pairs = annot.getPairs();
        for (MemberValuePair pair : pairs)
        {
            reuslt.put(pair.getName(), removeDoubleQuotes(pair.getValue().toString()));
        }
        return reuslt;
    }

    private static String removeDoubleQuotes(String str)
    {
        if (str.startsWith("\"") && str.endsWith("\""))
        {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

}
