package com.mega.core.filter;

import javax.servlet.*;
import java.io.IOException;

public class CharacterEncodingFilter implements Filter {
    private final static String DEFAULT_ENCODING = "UTF-8";

    private String encoding = DEFAULT_ENCODING;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String encoding = filterConfig.getInitParameter("encoding");
        if (encoding != null) {
            this.encoding = encoding;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding(encoding);

        chain.doFilter(request, response);

        response.setCharacterEncoding(encoding);
        response.setContentType("text/html;charset=" + encoding);
    }

    @Override
    public void destroy() {

    }
}
