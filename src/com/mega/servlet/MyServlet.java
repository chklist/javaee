package com.mega.servlet;

import com.mega.core.util.ConstUtils;
import com.mega.core.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet(urlPatterns = "/MyServlet/*")
public class MyServlet extends DispatcherServlet {

    public MyServlet() {
        System.out.println("MyServlet constructor invoked");
    }

    @Override
    public void init() {
        System.out.println("MyServlet init invoked");
    }

    public String info(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("MyServlet service invoked");

        StringBuffer requestURL = req.getRequestURL();
        System.out.println("requestURL = " + requestURL);

        String requestURI = req.getRequestURI();
        System.out.println("requestURI = " + requestURI);

        ServletContext servletContext = req.getServletContext();
        String contextPath = servletContext.getContextPath();
        System.out.println("contextPath = " + contextPath);
        String upload = servletContext.getRealPath("upload");
        System.out.println("upload = " + upload);
        String serverInfo = servletContext.getServerInfo();
        System.out.println("serverInfo = " + serverInfo);
        System.out.println("----------------------------------------");
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            System.out.println(key + ":" + req.getHeader(key));
        }

        return ConstUtils.FORWARD + "index.jsp";
    }

    @Override
    public void destroy() {
        System.out.println("MyServlet destroy invoked");
    }
}
