package com.example.Task_Management_Backend.config;

import com.example.Task_Management_Backend.service.jwt.UserService;
import com.example.Task_Management_Backend.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/*This method will verify the token when we will try to access any authenticated api
*
* Like :- we will send the jwt token with the authenticated api in the headers,
*         this method will extract the token from the request, and it will verify from the
*         jwtUtil file
* */


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // get the authorization from the request header
        final String authHeader = request.getHeader("Authorization");

        final String userEmail;
        final String token;

        // if the authHeader is Empty ya it is not starting with header, then simply return;
        if(StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader,"Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        token = authHeader.substring(7);
        userEmail = jwtUtil.extractUserName(token);

        // is userEmail exists and is not authenticated
        if(StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication()==null){

            // get the userDetails which we pass for token validation
             UserDetails userDetails = userService.userDetailService().loadUserByUsername(userEmail);

             // authenticate the user
             if(jwtUtil.isTokenValid(token,userDetails)){
                 SecurityContext context = SecurityContextHolder.createEmptyContext();

                 UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                         userDetails,null,userDetails.getAuthorities());

                 authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                 context.setAuthentication(authToken);
                 SecurityContextHolder.setContext(context);
             }
        }
        filterChain.doFilter(request,response);
    }
}
