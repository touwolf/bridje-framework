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

package org.bridje.orm.srcgen.model;

import javax.xml.bind.annotation.*;

/**
 * This class defines an external enumerator, the source code generator will not
 * generate an enumerator class for this information, this will serve only to
 * link external enumerators into the model.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ExternalEnumInf extends EnumBaseInf
{
    @XmlAttribute(name = "package")
    private String packageName;

    @Override
    public String getPackage()
    {
        if (packageName == null)
        {
            packageName = getModel().getPackage();
        }
        return packageName;
    }

    /**
     * The package that this enumerator belongs to.
     * 
     * @param packageName The package that this enumerator belongs to.
     */
    public void setPackage(String packageName)
    {
        this.packageName = packageName;
    }

}
