
package org.bridje.core.impl.http.jetty;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "http")
public class HttpConfig
{
    private List<HttpConnectorConfig> connectorConfigs;

    private List<WebServletConfig> servletConfigs;

    private Integer sessionExp;

    @XmlElementWrapper(name = "connectors")
    @XmlElements(
            {
                @XmlElement(name = "connector", type = HttpConnectorConfig.class)
            })
    public List<HttpConnectorConfig> getConnectorConfigs()
    {
        if(connectorConfigs == null)
        {
            connectorConfigs = new ArrayList<>();
        }

        if (connectorConfigs.isEmpty())
        {
            HttpConnectorConfig connectorConfig = new HttpConnectorConfig();
            connectorConfig.setPort(8080);
            connectorConfig.setProtocol("http");
            connectorConfigs.add(connectorConfig);
        }

        return connectorConfigs;
    }

    /**
     * Establece el listado de conectores <b>http</b>
     *
     * @param connectorConfigs Listado de {@link HttpConnectorConfig} o
     * <code>null</code>
     */
    public void setConnectorConfigs(List<HttpConnectorConfig> connectorConfigs)
    {
        this.connectorConfigs = connectorConfigs;
    }

    /**
     * Devuelve las configuraciones de
     * {@link javax.servlet.annotation.WebServlet} que se desean sobreescribir
     *
     * @return Configuraciones a sobreescribir, no <code>null</code>
     */
    @XmlElementWrapper(name = "servlets")
    @XmlElements(
            {
                @XmlElement(name = "servlet", type = WebServletConfig.class)
            })
    public List<WebServletConfig> getServletConfigs()
    {
        if (servletConfigs == null)
        {
            servletConfigs = new LinkedList<>();
        }
        return servletConfigs;
    }

    /**
     * Establece las configuraciones de
     * {@link javax.servlet.annotation.WebServlet} que se desean sobreescribir
     * en los servlets
     *
     * @param servletConfigs Listado de {@link WebServletConfig} con las
     * configuraciones de {@link javax.servlet.annotation.WebServlet} deseadas
     */
    public void setServletConfigs(List<WebServletConfig> servletConfigs)
    {
        this.servletConfigs = servletConfigs;
    }

    /**
     * Deuelve la cantidad de segundos limite que puede durar la sesion antes de
     * expirar
     *
     * @return cantidad de segundos o <code>null</code>
     */
    @XmlElement(name = "sessionExp")
    public Integer getSessionExp()
    {
        if(sessionExp == null)
        {
            sessionExp = 30;
        }
        return sessionExp;
    }

    /**
     * Establece la cantidad de segundos limite que podra durar la sesionn antes
     * de expirar
     *
     * @param sessionExp cantidad de segundos
     */
    public void setSessionExp(Integer sessionExp)
    {
        this.sessionExp = sessionExp;
    }

    /**
     * Busca una configuracion que sobreescribe la del
     * {@link javax.servlet.annotation.WebServlet} en un servlet
     *
     * @param name Propiedad especificada en el
     * {@link javax.servlet.annotation.WebServlet#name} por la cual se buscara
     * el servlet
     * @return Devuelve la configuracion {@link WebServletConfig} encontrada, o
     * <code>null</code>
     */
    public WebServletConfig findWebServletByName(String name)
    {
        for (WebServletConfig webServletConfig : getServletConfigs())
        {
            if (name.equals(webServletConfig.getName()))
            {
                return webServletConfig;
            }
        }
        return null;
    }
}
