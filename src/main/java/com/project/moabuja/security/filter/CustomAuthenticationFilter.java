package com.project.moabuja.security.filter;

import com.project.moabuja.exception.ErrorException;
import com.project.moabuja.exception.exceptionClass.LogoutJwtUseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.project.moabuja.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends GenericFilterBean {

    // 권한 검증
    private final JwtTokenProvider jwtProvider;
    private final RedisTemplate redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFilter.class);

    // jwt 토큰의 인증정보를 SecurityContext 에 담는 역할 - doFilter
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwtAccess = resolveAccessToken(httpServletRequest);
        String jwtRefresh = resolveRefreshToken(httpServletRequest);

        // ACCESS 토큰 먼저 검증
        if(jwtAccess != null){
            jwtProvider.validateToken(jwtAccess);
            String isLogout = (String)redisTemplate.opsForValue().get(jwtAccess);
            if (isLogout == null) {
                Authentication authentication = jwtProvider.getAuthentication(jwtAccess);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else{
                throw new ErrorException(LOGOUT_TOKEN_VALID);
            }
        } else if (jwtRefresh != null){
            checkToken(jwtRefresh);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void checkToken(String token) {
        jwtProvider.validateToken(token);
        Authentication authentication = jwtProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // 필터링을 하기 위해 토큰 정보를 가져오는 메소드
    private String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("A-AUTH-TOKEN");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 필터링을 하기 위해 토큰 정보를 가져오는 메소드
    private String resolveRefreshToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("R-AUTH-TOKEN");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
