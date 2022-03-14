package com.project.moabuja.exception.exceptionClass;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;

public class JwtExpiredException extends ExpiredJwtException {
    public JwtExpiredException(Header header, Claims claims, String message) {
        super(header, claims, message);
    }
}
