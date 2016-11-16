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
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.XmlSlurper;
import groovy.util.slurpersupport.GPathResult;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.classworlds.DuplicateRealmException;
import org.xml.sax.SAXException;

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
    private String sourceFolder;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/bridje", readonly = false)
    private String targetFolder;

    @Parameter(defaultValue = "${project.build.directory}/generated-resources/bridje", readonly = false)
    private String targetResFolder;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    private ClassLoader clsRealm;

    private Configuration cfg;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            getLog().info("Generating Source Code");
            Binding binding = new Binding();
            binding.setVariable("tools", this);
            GroovyShell shell = new GroovyShell(binding);
            clsRealm = ClassPathUtils.createClassPath(project);
            cfg = createFreeMarkerConfiguration();
            List<URL> generators = loadGenerators();
            for (URL generator : generators)
            {
                try (InputStream is = generator.openStream())
                {
                    shell.evaluate(new InputStreamReader(is));
                }
            }
            project.addCompileSourceRoot(targetFolder);
            Resource res = new Resource();
            res.setDirectory(targetResFolder);
            project.addResource(res);
        }
        catch (IOException | DuplicateRealmException | DependencyResolutionRequiredException e)
        {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private Configuration createFreeMarkerConfiguration() throws IOException
    {
        //Freemarker configuration
        Configuration result = new Configuration(Configuration.VERSION_2_3_23);
        TemplateLoader cpLoader = new ClassTemplateLoader(clsRealm, "/BRIDJE-INF/srcgen/");
        result.setTemplateLoader(cpLoader);
        result.setDefaultEncoding("UTF-8");
        result.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        result.setLogTemplateExceptions(false);
        return result;
    }

    /**
     * Returns a file object that represents the source folder for the code
     * generation goal.
     *
     * @return A File object.
     */
    public File getSourceFolder()
    {
        return new File(sourceFolder);
    }

    /**
     * Returns a file object that represents the target folder for the code
     * generation goal.
     *
     * @return A File object.
     */
    public File getTargetFolder()
    {
        return new File(targetFolder);
    }

    /**
     * Generates a new class file using the data and the template given.
     *
     * @param className The full name of the class to be generated.
     * @param template  The name of the template to be use.
     * @param data      The data to be use in the generation.
     *
     * @return A File object that represents the generated class fild.
     */
    public File generateClass(String className, String template, Map data)
    {
        try
        {
            getLog().info("Generating Class " + className + " from " + template);
            File clsFile = createClassFile(className);
            Template tmpl = cfg.getTemplate(template);
            if (tmpl != null)
            {
                try (FileWriter fw = new FileWriter(clsFile))
                {
                    tmpl.process(data, fw);
                }
            }
        }
        catch (TemplateException | IOException ex)
        {
            getLog().error(ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * Generates a new resource file.
     *
     * @param resName  The name and path of the resource file.
     * @param template The name of the template to be use.
     * @param data     The data to be use.
     *
     * @return The file generated.
     */
    public File generateResource(String resName, String template, Map data)
    {
        try
        {
            getLog().info("Generating Resource " + resName + " from " + template);
            File clsFile = createResourceFile(resName);
            Template tmpl = cfg.getTemplate(template);
            if (tmpl != null)
            {
                try (FileWriter fw = new FileWriter(clsFile))
                {
                    tmpl.process(data, fw);
                }
            }
        }
        catch (TemplateException | IOException ex)
        {
            getLog().error(ex.getMessage(), ex);
        }
        return null;
    }

    private File createClassFile(String className) throws IOException
    {
        String sep = File.separator;
        if ("\\".equals(sep))
        {
            sep = "\\\\";
        }
        String fName = className.replaceAll("[\\.]", sep);
        File f = new File(targetFolder + File.separator + fName + ".java");
        if (f.exists())
        {
            f.delete();
        }
        f.getParentFile().mkdirs();
        f.createNewFile();
        return f;
    }

    private File createResourceFile(String fileName) throws IOException
    {
        File f = new File(targetResFolder + File.separator + fileName);
        if (f.exists())
        {
            f.delete();
        }
        f.getParentFile().mkdirs();
        f.createNewFile();
        return f;
    }

    private List<URL> loadGenerators() throws IOException
    {
        List<URL> result = new ArrayList<>();
        Enumeration<URL> res = clsRealm.getResources("BRIDJE-INF/srcgen");

        while (res.hasMoreElements())
        {
            URL dirURL = res.nextElement();
            if (dirURL.getProtocol().equals("jar"))
            {
                String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
                JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
                Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                while (entries.hasMoreElements())
                {
                    String name = entries.nextElement().getName();
                    if (name.endsWith(".groovy"))
                    {
                        Enumeration<URL> resources = clsRealm.getResources(name);
                        while (resources.hasMoreElements())
                        {
                            URL url = resources.nextElement();
                            result.add(url);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Determines if the given file exists.
     *
     * @param fileName The name and path of the file to check.
     *
     * @return true the file exists, false otherwise.
     */
    public boolean fileExists(String fileName)
    {
        File f = new File(sourceFolder + File.separator + fileName);
        return f.exists() && f.isFile();
    }

    /**
     * Loads the given xml file into a GPathResult object that can be use
     * withing groovy to navigate the content of the xml document.
     *
     * @param fileName The name of the xml file to load.
     *
     * @return An GPathResult object.
     */
    public GPathResult loadXmlFile(String fileName)
    {
        try (FileReader fr = new FileReader(new File(sourceFolder + File.separator + fileName)))
        {
            return new XmlSlurper().parse(fr);
        }
        catch (ParserConfigurationException | SAXException | IOException ex)
        {
            getLog().error(ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * Loads all the xml files that mathc the fileExpr and are under the given
     * folder into a list of GPathResult object that can be use withing groovy
     * to navigate the content of the xml document.
     *
     * @param folder The folder in witch to look.
     * @param fileExpr The glob expr to match the files.
     *
     * @return An GPathResult object.
     */
    public List<GPathResult> loadXmlFiles(String folder, String fileExpr)
    {
        PathMatcher matcher
                = FileSystems.getDefault().getPathMatcher("glob:**/" + fileExpr);

        File lookFolder = new File(sourceFolder + File.separator + folder);
        File[] listFiles = lookFolder.listFiles((f) -> matcher.matches(f.toPath()));
        List<GPathResult> lst = new ArrayList<>();
        for (File file : listFiles)
        {
            try (FileReader fr = new FileReader(file))
            {
                lst.add(new XmlSlurper().parse(fr));
            }
            catch (ParserConfigurationException | SAXException | IOException ex)
            {
                getLog().error(ex.getMessage(), ex);
            }
        }
        return lst;
    }

    /**
     * Load all xml resources files availables in the projects class path.
     *
     * @param resourceName The name of the resource to load.
     *
     * @return A list with all the resources found.
     *
     * @throws IOException If any IO error ocurrs.
     */
    public List<GPathResult> loadXmlResources(String resourceName) throws IOException
    {
        List<GPathResult> result = new ArrayList<>();
        try
        {
            Enumeration<URL> resources = clsRealm.getResources(resourceName);
            while (resources.hasMoreElements())
            {
                URL url = resources.nextElement();
                try (InputStreamReader fr = new InputStreamReader(url.openStream()))
                {
                    result.add(new XmlSlurper().parse(fr));
                }
            }
        }
        catch (ParserConfigurationException | SAXException | IOException ex)
        {
            getLog().error(ex.getMessage(), ex);
        }
        return result;
    }

    /**
     * Load all property resources files availables in the projects class path.
     *
     * @param resourceName The name of the resource to load.
     *
     * @return A list with all the resources found.
     *
     * @throws IOException If any IO error ocurrs.
     */
    public List<Properties> loadPropertiesResources(String resourceName) throws IOException
    {
        List<Properties> result = new ArrayList<>();
        Enumeration<URL> resources = clsRealm.getResources(resourceName);
        while (resources.hasMoreElements())
        {
            URL url = resources.nextElement();
            try (InputStreamReader fr = new InputStreamReader(url.openStream()))
            {
                Properties p = new Properties();
                p.load(fr);
                result.add(p);
            }
        }
        return result;
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
