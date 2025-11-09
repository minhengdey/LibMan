package org.example.demo.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

@WebFilter({ "/reader/*", "/librarian/*", "/manager/*" })
public class AuthenticationFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();

        if (uri.equals(contextPath + "/reader/register-card") || uri.equals(contextPath + "/reader/register-card/")
                || uri.equals(contextPath + "/reader/search") || uri.equals(contextPath + "/reader/search/")
                || uri.equals(contextPath + "/reader/document-detail")
                || uri.equals(contextPath + "/reader/document-detail/")) {
            chain.doFilter(request, response);
            return;
        }

        // Check if user is logged in
        if (session == null || session.getAttribute("user") == null) {
            httpResponse.sendRedirect(contextPath + "/login");
            return;
        }

        // Check role-based access
        String role = (String) session.getAttribute("role");

        if (uri.startsWith(contextPath + "/manager") && !"MANAGER".equals(role)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }

        if (uri.startsWith(contextPath + "/librarian") &&
                !"LIBRARIAN".equals(role) && !"MANAGER".equals(role)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }

        if (uri.startsWith(contextPath + "/reader") &&
                !"READER".equals(role) && !"LIBRARIAN".equals(role) && !"MANAGER".equals(role)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }

        chain.doFilter(request, response);
    }

    public void destroy() {
    }
}
