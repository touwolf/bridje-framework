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

package org.bridje.jdbc.config;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.bridje.cfg.Configuration;
import org.bridje.cfg.adapter.XmlConfigAdapter;
/**
 *
 */
@XmlRootElement(name = "jdbc")
@XmlAccessorType(XmlAccessType.FIELD)
@Configuration(XmlConfigAdapter.class)
public class JdbcConfig
{
    @XmlElement(name = "datasources")
    private List<DataSourceConfig> dataSources;

    public List<DataSourceConfig> getDataSources()
    {
        if(dataSources == null)
        {
            dataSources = new ArrayList<>();
        }
        return dataSources;
    }

    public void setDataSources(List<DataSourceConfig> dataSources)
    {
        this.dataSources = dataSources;
    }
}
