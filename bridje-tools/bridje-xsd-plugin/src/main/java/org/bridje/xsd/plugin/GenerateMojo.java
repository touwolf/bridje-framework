package org.bridje.xsd.plugin;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.github.sardine.impl.SardineException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
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
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.classworlds.realm.DuplicateRealmException;

@Mojo(name = "generate")
public class GenerateMojo extends AbstractMojo
{
    @Parameter(property = "project")
    private MavenProject project;

    @Parameter(property = "settings")
    private Settings settings;

    @Parameter(property = "generate.models")
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private List<String> models;

    @Parameter(property = "generate.modelName")
    private String modelName;

    @Parameter(property = "generate.upload")
    private String upload;

    @Parameter(property = "generate.target", defaultValue = "schema")
    private String target;

    private String path;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        path = System.getProperty("java.io.tmpdir");
        if (!path.endsWith(File.separator))
        {
            path += File.separator + target;
        }
        else
        {
            path += target;
        }
        new File(path).mkdirs();

        OutputResolver outputResolver = new OutputResolver(path);
        try
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

            processModel(newRealm, outputResolver);
            upload();
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

        for (String model : models)
        {
            if (model.endsWith(".java"))
            {
                model = model.substring(model.length() - 5);
            }

            try
            {
                Class<?> modelCls = Class.forName(model, true, classLoader);
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

    private void upload() throws IOException
    {
        String url;
        if (upload.endsWith("/"))
        {
            url = upload + target;
        }
        else
        {
            url = upload + "/" + target;
        }

        Sardine sardine = SardineFactory.begin();

        String host = new URL(upload).getHost();
        Server server = settings.getServer(host);
        if (server != null)
        {
            sardine.setCredentials(server.getUsername(), server.getPassword());
        }
        else
        {
            getLog().warn("Debe configurar las credenciales de acceso a " + host);
        }

        if (!sardine.exists(url))
        {
            getLog().info("Creando directorio destino: " + url);
            sardine.createDirectory(url);
        }

        sardine.enablePreemptiveAuthentication(host);
        if (!modelName.endsWith(".xsd"))
        {
            modelName += ".xsd";
        }

        String source = path + File.separator + modelName;

        File file = new File(source);
        try
        {
            getLog().info("Subiendo " + modelName + " ...");
            sardine.put(url + "/" + modelName, new FileInputStream(file), "text/xml", true, file.length());
            getLog().info("Subido " + modelName);
        }
        catch (SardineException ex)
        {
            getLog().warn("Error subiendo " + modelName + ": " + ex.getMessage());
        }
    }
}
