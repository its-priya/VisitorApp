package io.bootify.visitor_app.filters;

import org.apache.catalina.connector.RequestFacade;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestFilter extends HttpFilter {
    //private static final String REQUEST_ID= "requestId";

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //MDC.put(REQUEST_ID, ((RequestFacade)request).getHeader(REQUEST_ID));
        chain.doFilter(request, response);
        MDC.clear();
    }
}
