package org.example.demo.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

@WebFilter("/*")
public class EncodingFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        request.setCharacterEncoding("UTF-8");
        httpResponse.setCharacterEncoding("UTF-8");

        String uri = httpRequest.getRequestURI();
        if (uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".png")
                || uri.endsWith(".jpg") || uri.endsWith(".jpeg") || uri.endsWith(".gif")
                || uri.endsWith(".svg") || uri.endsWith(".woff") || uri.endsWith(".woff2")
                || uri.endsWith(".eot") || uri.endsWith(".ttf")) {
            chain.doFilter(request, response);
            return;
        }

        if (httpResponse.getContentType() == null || httpResponse.getContentType().isEmpty()) {
            httpResponse.setContentType("text/html; charset=UTF-8");
        }

        chain.doFilter(request, response);
    }

    public void destroy() {
    }
}