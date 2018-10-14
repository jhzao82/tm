package org.strangeforest.tcb.stats;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class UrlFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("sssssssssssssssssssssssssss");
        final HttpServletRequest hsRequest = (HttpServletRequest) request;
        final HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(hsRequest);

        String url = hsRequest.getRequestURI().substring(hsRequest.getContextPath().length());

        //This is read from a .properties file actually, but for now it's ok
        String supportedLanguages = "/zh/,/tw/,/en/";
        List<String> listOfLanguages = Arrays.asList(supportedLanguages.split(","));

        //If the URL already contains any of the allowed language identifiers, we continue with the original flow
        for (String language : listOfLanguages) {
            if (url.toLowerCase().startsWith(language)) {
                String filtered = url.substring(3);
                RequestDispatcher dispatcher = wrapper.getRequestDispatcher(filtered);
                dispatcher.forward(request, response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    public void destroy() {
    }
}