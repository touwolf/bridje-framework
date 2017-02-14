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

package org.bridje.srcgen.impl.edit;

import java.util.List;
import javafx.scene.control.TreeItem;
import org.bridje.srcgen.SourceGenerator;

class SrcGenerationNode extends TreeItem<Object>
{
    public SrcGenerationNode(SourceGenerator<Object> gen)
    {
        super(gen.getName(), gen.getImage());
        List<Object> lstFata = gen.findData();
        for (Object object : lstFata)
        {
            getChildren().add(gen.createTreeNode(object));
        }
    }
}
