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

package org.bridje.core.web.view;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

@XmlAccessorType(XmlAccessType.FIELD)
public class Overrides extends Component
{
    @XmlElements(
            {
                @XmlElement(name = "define", type = Define.class),

                @XmlElement(name = "vertical", type = VerticalLayout.class),
                @XmlElement(name = "horizontal", type = HorizontalLayout.class),
                @XmlElement(name = "border", type = BorderLayout.class),
                @XmlElement(name = "menubar", type = MenuBar.class),
                @XmlElement(name = "toolbar", type = ToolBar.class),
                @XmlElement(name = "section", type = Section.class),
                @XmlElement(name = "tabsheet", type = TabSheet.class),
                @XmlElement(name = "accordion", type = Accordion.class),

                @XmlElement(name = "combobox", type = ComboBox.class),
                @XmlElement(name = "datalist", type = DataList.class),
                @XmlElement(name = "datatable", type = DataTable.class),
                @XmlElement(name = "header", type = Header.class),
                @XmlElement(name = "image", type = Image.class),
                @XmlElement(name = "link", type = Link.class),
                @XmlElement(name = "parag", type = Paragraph.class),
                @XmlElement(name = "html", type = RawHtml.class),
                @XmlElement(name = "text", type = Text.class),
                @XmlElement(name = "textbox", type = TextBox.class),
                @XmlElement(name = "password", type = Password.class),
                @XmlElement(name = "number", type = Number.class),
                @XmlElement(name = "button", type = Button.class),
            })
    private List<Component> childs;

    @XmlAttribute(name = "name")
    private String name;    

    public String getName()
    {
        return name;
    }

    public List<Component> getChilds()
    {
        return childs;
    }
}
