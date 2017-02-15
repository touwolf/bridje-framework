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

package org.bridje.orm.srcgen.edit;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.bridje.orm.srcgen.model.ModelInf;
import org.bridje.srcgen.impl.edit.EditableItem;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFileOutputStream;

public class ModelInfTreeItem extends TreeItem<Object> implements EditableItem
{
    private static final Logger LOG = Logger.getLogger(ModelInfTreeItem.class.getName());

    private static final ModelInfEditor EDITOR = new ModelInfEditor();
    
    private final ModelInf model;
    
    private final VFile file;

    public ModelInfTreeItem(ModelInf modelInf, VFile file)
    {
        super(modelInf, Utils.createImageView(OrmSrcGenTreeItem.class, "database.png"));
        this.model = modelInf;
        this.file = file;
        getChildren()
                .addAll(modelInf.getTemplates()
                .stream()
                .map(f -> new TemplateInfTreeItem(f))
                .collect(Collectors.toList()));
        getChildren()
                .addAll(modelInf.getEntities()
                .stream()
                .map(f -> new EntityInfTreeItem(f))
                .collect(Collectors.toList()));
        getChildren()
                .addAll(modelInf.getEnums()
                .stream()
                .map(f -> new EnumInfTreeItem(f))
                .collect(Collectors.toList()));
    }

    @Override
    public Pane getEditor()
    {
        return EDITOR;
    }

    @Override
    public void startEdit()
    {
        EDITOR.setModel(model);
    }

    @Override
    public void commit()
    {
        EDITOR.updateModel();
        try(VFileOutputStream os = new VFileOutputStream(file))
        {
            JAXBContext context = JAXBContext.newInstance(ModelInf.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(model, os);
        }
        catch (JAXBException | IOException ex)
        {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
