/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bridje.core.impl.web.servlet;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bridje.ioc.Component;
import org.bridje.ioc.Inject;
import org.bridje.core.web.WebManager;

@Component
@WebServlet(name = "bridje-web-servlet", urlPatterns = "/*")
public class BridjeWebServlet extends HttpServlet
{
    private static final Logger LOG = Logger.getLogger(BridjeWebServlet.class.getName());

    @Inject
    private WebManager webMang;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        webMang.proccess(new WebRequestImpl(req), new WebResponseImpl(resp));
    }
}
