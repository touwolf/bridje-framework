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
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.bridje.orm.srcgen.model.ModelInf;
import org.bridje.srcgen.impl.edit.EditableItem;
import org.bridje.vfs.VFile;
import org.bridje.vfs.VFileOutputStream;

public abstract class TreeItemBase<T> extends TreeItem<Object> implements EditableItem
{
    private static final Logger LOG = Logger.getLogger(TreeItemBase.class.getName());

    private final ModelInf model;
    
    private final T data;
    
    private final VFile file;

    public TreeItemBase(T value, ModelInf modelInf, VFile file, ImageView view)
    {
        super(value, view);
        this.model = modelInf;
        this.file = file;
        this.data = value;
    }

    public T getData()
    {
        return data;
    }

    public ModelInf getModel()
    {
        return model;
    }

    public VFile getFile()
    {
        return file;
    }

    public void saveModel()
    {
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
