/*
 * Copyright 2016 Bridje Framework.
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

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;
import org.codehaus.classworlds.ClassRealm;
import org.codehaus.classworlds.ClassWorld;
import org.codehaus.classworlds.DuplicateRealmException;

class ClassPathUtils
{
    public static ClassLoader createClassPath(MavenProject project)
                throws MalformedURLException, DuplicateRealmException, DependencyResolutionRequiredException
    {
        List<String> elements = project.getCompileClasspathElements();
        Set<Artifact> artifacts = project.getDependencyArtifacts();

        ClassWorld world = new ClassWorld();
        ClassRealm newRealm = world.newRealm("projCp", null);
        for (Artifact artifact : artifacts)
        {
            File file = artifact.getFile();
            newRealm.addConstituent(file.toURI().toURL());
        }

        for(String element : elements)
        {
            newRealm.addConstituent(new File(element).toURI().toURL());
        }
        return newRealm.getClassLoader();
    }
}
