package com.example;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class RedirectFilter implements Filter {
    
    // Configuration - set your target URL here
    private static final String TARGET_URL = "https://example.com";
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                         FilterChain chain) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Redirect with HTTP 302 (temporary redirect)
        httpResponse.sendRedirect(TARGET_URL);
        
        // For permanent redirect (HTTP 301), use:
        // httpResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        // httpResponse.setHeader("Location", TARGET_URL);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        // Initialization if needed
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }
}