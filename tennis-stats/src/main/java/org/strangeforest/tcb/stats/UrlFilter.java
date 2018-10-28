package org.strangeforest.tcb.stats;

import java.io.*;
import java.net.*;
import java.util.Locale;
import javax.servlet.*;
import javax.servlet.http.*;

import org.springframework.context.i18n.*;

public class UrlFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest hsRequest = (HttpServletRequest) request;
        final HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(hsRequest);

        String url = hsRequest.getRequestURI().substring(hsRequest.getContextPath().length());
        String[] listOfLanguages = {"zh", "en"};

        for (String alanguage : listOfLanguages) {
            String urlPrefix = '/' + alanguage + '/';
            if (url.toLowerCase().startsWith(urlPrefix)) {
                LocaleContextHolder.setLocale(new Locale(alanguage));
                String filtered = url.substring(3);
                filtered = appendUri(filtered, "language=" + alanguage).toString();
                RequestDispatcher dispatcher = wrapper.getRequestDispatcher(filtered);
                dispatcher.forward(request, response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    public void destroy() {
    }

    public URI appendUri(String uri, String appendQuery) {
        try {
            URI oldUri = new URI(uri);

            String newQuery = oldUri.getQuery();
            if (newQuery == null) {
                newQuery = appendQuery;
            } else {
                newQuery += "&" + appendQuery;
            }

            URI newUri = new URI(oldUri.getScheme(), oldUri.getAuthority(),
                    oldUri.getPath(), newQuery, oldUri.getFragment());

            return newUri;
        } catch (Exception e) {
            return null;
        }
    }
}