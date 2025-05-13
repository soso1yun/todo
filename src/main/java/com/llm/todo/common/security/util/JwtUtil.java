package com.llm.todo.common.security.util;

import com.llm.todo.domain.user.dto.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private final long accessTokenExpireTime;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration_time}") long accessTokenExpTime
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.accessTokenExpireTime = accessTokenExpTime;
    }


    public String createAccessToken(UserDTO userDTO) {
        return createToken(userDTO, accessTokenExpireTime);
    }

    public String createToken(UserDTO userDTO, long accessTokenExpireTime){

        Claims claims = Jwts.claims();
        claims.put("id", userDTO.getId());
        claims.put("userName", userDTO.getUserName());
        claims.put("userId", userDTO.getUserId());
        claims.put("role", userDTO.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusSeconds(accessTokenExpireTime).toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserName(String token) {
        return String.valueOf(parseToken(token).get("userId", String.class));
    }

    public Claims parseToken(String accessToken) throws ExpiredJwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            e.fillInStackTrace();
        }

        return false;
    }



}
