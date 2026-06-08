package com.co.nexora.pag.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class ApiKeyFilter implements Filter {

    private static final String API_KEY = "Zb674]3x6Qngq0";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        if (httpRequest.getRequestURI().startsWith("/api/auth")) {
            chain.doFilter(request, response);
            return;
        }

        String requestApiKey = httpRequest.getHeader("X-API-KEY");
        if (requestApiKey == null) {
            requestApiKey = httpRequest.getHeader("x-api-key");
        }

        if (API_KEY.equals(requestApiKey)) {
            chain.doFilter(request, response);
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Unauthorized");
        }
    }
}
