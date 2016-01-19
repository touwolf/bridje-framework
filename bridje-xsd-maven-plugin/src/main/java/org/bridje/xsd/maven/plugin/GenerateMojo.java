
package org.bridje.xsd.maven.plugin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.classworlds.realm.DuplicateRealmException;

@Mojo(name = "generate-xsd")
public class GenerateMojo extends AbstractMojo
{
    @Parameter(property = "project")
    private MavenProject project;

    @Parameter(property = "generateXsd.classes")
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private List<String> classes;

    @Parameter(property = "generateXsd.modelName")
    private String modelName;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if(modelName == null)
        {
            getLog().warn("XSD Schema cannot be generated. You must specify the model name.");
            return;
        }

        OutputResolver outputResolver = new OutputResolver(project.getBuild().getDirectory());
        try
        {
            ClassRealm newRealm = createClassRealm();
            processModel(newRealm, outputResolver);
        }
        catch (DependencyResolutionRequiredException | IOException | DuplicateRealmException ex)
        {
            getLog().error(ex.getMessage());
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
    }

    private void processModel(ClassLoader classLoader, OutputResolver outputResolver)
    {
        List<Class<?>> lstClass = new LinkedList<>();

        for (String cls : classes)
        {
            if (cls.endsWith(".java"))
            {
                cls = cls.substring(cls.length() - 5);
            }

            try
            {
                Class<?> modelCls = Class.forName(cls, true, classLoader);
                lstClass.add(modelCls);
            }
            catch (ClassNotFoundException ex)
            {
                getLog().error(ex.getMessage());
            }
        }

        try
        {
            Class<?>[] arr = new Class<?>[lstClass.size()];
            arr = lstClass.toArray(arr);
            JAXBContext context = JAXBContext.newInstance(arr);
            outputResolver.setSchemaName(modelName);
            getLog().info("Generando XSD: " + modelName + " ...");
            context.generateSchema(outputResolver);
            getLog().info("Generado " + modelName + ".xsd");
        }
        catch (JAXBException | IOException ex)
        {
            getLog().error(ex.getMessage());
        }
    }

    private ClassRealm createClassRealm() throws DependencyResolutionRequiredException, DuplicateRealmException, MalformedURLException
    {
        List<String> elements = project.getCompileClasspathElements();
        Set<Artifact> artifacts = project.getDependencyArtifacts();

        ClassWorld world = new ClassWorld();
        ClassRealm newRealm = world.newRealm("projCp", null);
        for (Artifact artifact : artifacts)
        {
            File file = artifact.getFile();
            newRealm.addURL(file.toURI().toURL());
        }

        for(String element : elements)
        {
            newRealm.addURL(new File(element).toURI().toURL());
        }
        return newRealm;
    }
}
