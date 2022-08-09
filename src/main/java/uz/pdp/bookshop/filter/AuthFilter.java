package uz.pdp.bookshop.filter;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@WebFilter(value = "/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String path = request.getServletPath();


        HttpSession session = request.getSession();
        Boolean isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");

        if (Objects.nonNull(isAuthenticated) || isPublic(path)) {
            filterChain.doFilter(req, resp);
        } else {
            response.sendRedirect("/");
        }
    }

    private boolean isPublic(String servletPath) {
        List<String> list = new ArrayList<>(Arrays.asList("/", "/login", "/index.jsp"));
        return list.contains(servletPath);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
