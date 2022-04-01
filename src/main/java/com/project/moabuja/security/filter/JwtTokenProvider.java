package com.project.moabuja.security.filter;

import com.project.moabuja.exception.ErrorException;
import com.project.moabuja.security.userdetails.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

import static com.project.moabuja.exception.ErrorCode.MEMBER_NOT_FOUND;

@Component
public class JwtTokenProvider {
    private long accessTokenTime = 1000 * 60 * 60 * 24; // 1시간
    private long refreshTokenTime = 1000 * 60 * 60 * 24; // 하루

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private String secretKey;
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // access 토큰 생성
    public String createAccessToken(String password) {
        Claims claims = Jwts.claims().setSubject(password);
        Date now = new Date();
        return Jwts.builder()
                .setSubject(password)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // refresh 토큰 생성
    public String createRefreshToken(String password) {
        Claims claims = Jwts.claims().setSubject(password);
        Date now = new Date();
        return Jwts.builder()
                .setSubject(password)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        UserDetails userDetails = null;
        try {
            userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        } catch (UsernameNotFoundException e) {
            throw new ErrorException(MEMBER_NOT_FOUND);
        }
        return new UsernamePasswordAuthenticationToken(userDetails, "", null);
    }

    //토큰의 유효성 검증
    public boolean validateToken(String jwtToken) {
        Jws<Claims> claims = null;
        try {
            claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (UnsupportedJwtException e) {
            throw new JwtException("인수가 Claims JWS를 나타내지 않는 경우");
        } catch (MalformedJwtException e) {
            throw new MalformedJwtException(" 문자열이 유효한 JWS가 아닌 경우");
        }  catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("문자열이 null이거나 비어 있거나 공백만 있는 경우");
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

}
