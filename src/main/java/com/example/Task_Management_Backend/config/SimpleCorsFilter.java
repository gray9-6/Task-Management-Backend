package com.example.Task_Management_Backend.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * SimpleCorsFilter is a filter responsible for handling Cross-Origin Resource Sharing (CORS) in the application.
 * It allows requests from specified origins (e.g., frontend applications) to access resources on the server.
 * This filter is applied to all incoming requests and sets appropriate CORS headers to enable cross-origin requests.
 *
 * CORS (Cross-Origin Resource Sharing) is a security feature implemented by web browsers that restricts
 * web pages from making requests to a different domain than the one that served the original page.
 * CORS headers are used to inform the browser whether it's allowed to make requests to a specific origin.
 *
 * This filter sets the following CORS headers:
 * - Access-Control-Allow-Origin: Specifies which origins are allowed to access the resources. It is set to the
 *   value of the "origin" header in the incoming request.
 * - Access-Control-Allow-Methods: Specifies the HTTP methods (e.g., POST, PUT, GET, DELETE) that are allowed
 *   when accessing the resources.
 * - Access-Control-Max-Age: Specifies how long the results of a preflight request can be cached.
 * - Access-Control-Allow-Headers: Specifies which HTTP headers can be used when making the actual request.
 *
 * Additionally, this filter handles preflight requests (OPTIONS) by setting the response status to 200 OK
 * if the request method is OPTIONS, indicating that the actual request can proceed.
 */


/**
 * The {@code @Order} annotation is used to specify the order in which filters are applied in the filter chain.
 * Filters with a lower order value are applied before filters with a higher order value.
 *
 * In this context, the {@code @Order(Ordered.HIGHEST_PRECEDENCE)} annotation indicates that the
 * {@link SimpleCorsFilter} should be given the highest precedence in the filter chain.
 *
 * When multiple filters are configured in an application, the order in which they are applied is important
 * for proper functioning. By setting the order to {@code Ordered.HIGHEST_PRECEDENCE}, this filter ensures
 * that it is executed before any other filters in the chain.
 *
 * This is important for the SimpleCorsFilter because it needs to be applied to all incoming requests
 * to handle CORS before any other filters process the request.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@Slf4j
public class SimpleCorsFilter implements Filter {

    @Value("${app.client.url}")
    private String clientAppUrl = "";

    /**
     * Initializes the filter.
     *
     * @param filterConfig the FilterConfig object containing the filter's configuration
     * @throws ServletException if an error occurs during filter initialization
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    /**
     * Filters the request and sets CORS headers to enable cross-origin requests.
     *
     * @param servletRequest  the ServletRequest object representing the incoming request
     * @param servletResponse the ServletResponse object representing the outgoing response
     * @param filterChain     the FilterChain object for invoking the next filter in the chain
     * @throws IOException      if an I/O error occurs during the filter execution
     * @throws ServletException if an error occurs during the filter execution
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("Inside doFilter Method");

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        Map<String,String> map = new HashMap<>();

        // Get the origin header from the request
        String originHeader = request.getHeader("origin");

        // Set CORS headers
        response.setHeader("Access-Control-Allow-Origin", originHeader);
        response.setHeader("Access-Control-Allow-Methods", "POST,PUT,GET,DELETE,OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");

        // Handle preflight requests (OPTIONS)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            // Continue with the filter chain for non-preflight requests
            filterChain.doFilter(request, response);
        }
    }

    /**
     * Destroys the filter.
     */
    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}






/*In this class we are allowing all the apis call from our frontend application*/
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//@Slf4j
//public class SimpleCorsFilter implements Filter {
//
//    @Value("${app.client.url}")
//    private String clientAppUrl = "";
//
//    public SimpleCorsFilter(){
//    }
//
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        Filter.super.init(filterConfig);
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        log.info("Inside doFilter Method");
//
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//
//        Map<String,String> map = new HashMap<>();
//        String originHeader = request.getHeader("origin");
//        response.setHeader("Access-Control-Allow-Origin",originHeader);
//        response.setHeader("Access-Control-Allow-Methods","POST,PUT,GET,DELETE,OPTIONS");
//        response.setHeader("Access-Control-Max-Age","3600");
//        response.setHeader("Access-Control-Allow-Headers","*");
//
//        if("OPTIONS".equalsIgnoreCase(request.getMethod())){
//            response.setStatus(HttpServletResponse.SC_OK);
//        }else{
//            filterChain.doFilter(request,response);
//        }
//
//
//    }
//
//    @Override
//    public void destroy() {
//        Filter.super.destroy();
//    }
//}
