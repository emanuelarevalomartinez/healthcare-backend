package com.healthcare.config.security;

import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.shared.exceptions.ErrorMessage;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtGenerator {

    @Value("${security.jwt.secret-key}")
    private String secretKey;


    public String generateTokenFromUser(UserEntity user) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + SecurityConstants.JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().name())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claimsResolver.apply(claims);
    }

    public String getUsernameFromJWT(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public boolean validateToken(String token, HttpServletRequest request) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;

        } catch (ExpiredJwtException e) {
            request.setAttribute("JWT_ERROR", ErrorMessage.JWT_EXPIRED);

        } catch (UnsupportedJwtException e) {
            request.setAttribute("JWT_ERROR", ErrorMessage.JWT_UNSUPPORTED);

        } catch (MalformedJwtException e) {
            request.setAttribute("JWT_ERROR", ErrorMessage.JWT_MALFORMED);

        } catch (SignatureException e) {
            request.setAttribute("JWT_ERROR", ErrorMessage.JWT_INVALID_SIGNATURE);

        } catch (IllegalArgumentException e) {
            request.setAttribute("JWT_ERROR", ErrorMessage.JWT_EMPTY);
        }

        return false;
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().after(new Date());
        } catch (ExpiredJwtException ex) {
            return false;
        } catch (JwtException e) {
            return false;
        }
    }

    public String generateAccessToken(UserEntity user) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + SecurityConstants.JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", "ROLE_" + user.getRole().name())
                .claim("type", "ACCESS")
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(UserEntity user) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + SecurityConstants.REFRESH_EXPIRATION);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("type", "REFRESH")
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isRefreshToken(String token) {
        return "REFRESH".equals(getClaimFromToken(token, c -> c.get("type")));
    }

    public boolean isAccessToken(String token) {
        return "ACCESS".equals(getClaimFromToken(token, c -> c.get("type")));
    }

}
