package com.project.moabuja.security.filter;

import com.project.moabuja.exception.exceptionClass.HomeMemberNotFoundException;
import com.project.moabuja.exception.exceptionClass.JwtExpiredException;
import com.project.moabuja.exception.exceptionClass.LogoutJwtUseException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(req, res); // go to 'JwtAuthenticationFilter'
        } catch (HomeMemberNotFoundException ex) {
            homeMemberNotFoundErrorResponse(HttpStatus.UNAUTHORIZED, res, ex);
        } catch (ExpiredJwtException | IOException ex) {
            expiredJwtErrorResponse(HttpStatus.UNAUTHORIZED, res, ex);
        } catch (LogoutJwtUseException ex){
            logoutJwtErrorResponse(HttpStatus.UNAUTHORIZED, res, ex);
        }
    }

    public void homeMemberNotFoundErrorResponse(HttpStatus status, HttpServletResponse res, Throwable ex) throws IOException {
        res.setStatus(status.value());
        res.setContentType("application/json; charset=UTF-8");
        PrintWriter out = res.getWriter();

        //create Json Object
        JSONObject json = new JSONObject();
        // put some value pairs into the JSON object .
        json.put("code", HttpStatus.UNAUTHORIZED);
        json.put("message", "Move to Login Page");
        // finally output the json string
        out.print(json.toString());
    }

    public void expiredJwtErrorResponse(HttpStatus status, HttpServletResponse res, Throwable ex) throws IOException {
        System.out.println("익셉션 필터 넘어가기");
        res.setStatus(status.value());
        res.setContentType("application/json; charset=UTF-8");
        PrintWriter out = res.getWriter();

        //create Json Object
        JSONObject json = new JSONObject();
        // put some value pairs into the JSON object .
        json.put("code", 1004);
        json.put("message", "권한의 문제 reissue");
        // finally output the json string
        out.print(json.toString());
    }
    public void logoutJwtErrorResponse(HttpStatus status, HttpServletResponse res, Throwable ex) throws IOException {
        res.setStatus(status.value());
        res.setContentType("application/json; charset=UTF-8");
        PrintWriter out = res.getWriter();

        //create Json Object
        JSONObject json = new JSONObject();
        // put some value pairs into the JSON object .
        json.put("code", HttpStatus.UNAUTHORIZED);
        json.put("message", "로그아웃된 아이디로 접근 불가");
        // finally output the json string
        out.print(json.toString());
    }
}