package com.team.project.jwt;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

@Component
public class JwtExceptionFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response); // go to JwtAuthenticationFilter
        } catch (JwtException e) {
            setErrorResponse(HttpStatus.OK, response, e);
        } catch (NoSuchElementException e) {
            setErrorResponse(HttpStatus.BAD_REQUEST, response, e);
        }
    }

    public void setErrorResponse(HttpStatus status, ServletResponse response, Throwable e) throws IOException {
        ((HttpServletResponse)response).setStatus(status.value());
        response.setContentType("application/json; charset=UTF-8");

        JwtExceptionResponse jwtExceptionResponse = new JwtExceptionResponse(false, e.getMessage());
        response.getWriter().write(jwtExceptionResponse.convertToJson());
    }
}