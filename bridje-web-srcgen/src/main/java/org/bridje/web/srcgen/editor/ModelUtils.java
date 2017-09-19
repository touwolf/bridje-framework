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

package org.bridje.web.srcgen.editor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.bridje.srcgen.SrcGenService;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFileOutputStream;
import org.bridje.web.srcgen.uisuite.PartialUISuite;
import org.bridje.web.srcgen.uisuite.Resource;
import org.bridje.web.srcgen.uisuite.StandaloneDef;
import org.bridje.web.srcgen.uisuite.UISuite;

/**
 * Utility class to parse and write from a raw UISuite from/to UISuiteModel
 */
public class ModelUtils
{
    private static final Logger LOG = Logger.getLogger(ModelUtils.class.getName());

    /**
     * Converts the given model to its raw form and save it to its file.
     * 
     * @param uiSuite The model to save.
     */
    public static void saveUISuite(UISuiteModel uiSuite)
    {
        if(uiSuite == null) return;
        if(uiSuite.getName() != null)
        {
            if(uiSuite.getFile() == null)
            {
                uiSuite.setFile(new VFile(SrcGenService.DATA_PATH.join(uiSuite.getName() + ".xml")));
                uiSuite.getFile().createNewFile();
            }
            UISuite data = ModelUtils.fromModel(uiSuite);
            try(OutputStream os = new VFileOutputStream(uiSuite.getFile()))
            {
                UISuite.save(os, data);
            }
            catch(ParserConfigurationException | TransformerException | JAXBException | IOException ex)
            {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    /**
     * Parses an UISuite and creates a UISuiteModel from it.
     * 
     * @param suite The UISuite to parse.
     * @param file The file that the model was readed from.
     * @return The new UISuiteModel object.
     */
    public static UISuiteModel toModel(UISuite suite, VFile file)
    {
        UISuiteModel result = new UISuiteModel();
        result.setFile(file);
        result.setName(suite.getName());
        result.setDefaultResources(resourceToModel(suite.getDefaultResources()));
        result.setDefines(standaloneToModel(suite.getDefines()));
        result.setStandalone(standaloneToModel(suite.getStandalone()));
        
        
        return result;
    }

    /**
     * Parses an PartialUISuite and creates a PartialUISuiteModel from it.
     * 
     * @param partialSuite The UISuite to parse.
     * @param file The file that the model was readed from.
     * @return The new UISuiteModel object.
     */
    public static PartialUISuiteModel toPartialModel(PartialUISuite partialSuite, VFile file)
    {
        PartialUISuiteModel result = new PartialUISuiteModel();
        result.setFile(file);
        
        return result;
    }

    /**
     * Writes the given model to an UISuite object.
     * 
     * @param suiteModel The model to write the UISuite object.
     * @return The new UISuite object.
     */
    public static UISuite fromModel(UISuiteModel suiteModel)
    {
        UISuite result = new UISuite();

        return result;
    }

    private static ResourceModel resourceToModel(Resource res)
    {
        ResourceModel resModel = new ResourceModel();
        
        return resModel;
    }

    private static StandaloneDefModel standaloneToModel(StandaloneDef defines)
    {
        StandaloneDefModel result = new StandaloneDefModel();
        
        return result;
    }
}
