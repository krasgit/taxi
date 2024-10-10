package com.matin.taxi;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SessionFilter implements Filter {

    private final String KEY = "filterCount";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	
    	
    	
    	System.out.println("Procces SessionFilter "+((HttpServletRequest) request).getRequestURL());
    	
    	String id = ((HttpServletRequest) request).getSession().getId();
    	System.out.println("Procces SessionFilter "+id);
    	
        HttpSession session = ((HttpServletRequest) request).getSession(true);
        if (session != null) {
            Integer count = (Integer) session.getAttribute(KEY);
            if (count == null) {
                count = -1;
            }
            
           
            session.setAttribute(KEY, count + 1);
        }
        chain.doFilter(request, response);
    }

}
