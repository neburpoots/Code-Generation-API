package io.swagger.security;

import io.swagger.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(httpServletRequest); // retrieve the token from the request
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) { // check if the token is valid
                Authentication auth = jwtTokenProvider.getAuthentication(token); // retrieve the user from the database
                SecurityContextHolder.getContext().setAuthentication(auth); // apply the user to the security context of the request
            }
        } catch (Exception ex) {
            SecurityContextHolder.clearContext(); // if the token is invalid, clear the security context
            throw new UnauthorizedException("Token is invalid");
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse); // move on to the next filter
    }
}
