package me.findthepeach.springsecuritydemo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

// utils/JwtUtil.java
public class JwtUtil {
    // Token expires in 1 hour
    public static final Long JWT_EXPIRATION_TIME = 60 * 60 * 1000L;
    // In production, this should be moved to configuration files
    // Use a Base64 key longer than 32 bit
    private static final String JWT_SECRET_KEY = "b7YXqMCsQKbVrnRwsUjMZ1ZHPhb3wmF6TVXh9DdaTPtbBJCB5PqPVp0lq3lTXsXx";

    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String generateJwt(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());
        return builder.compact();
    }

    public static String generateJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        Key signingKey = new SecretKeySpec(JWT_SECRET_KEY.getBytes(),
                SignatureAlgorithm.HS256.getJcaName());
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMillis==null){
            ttlMillis=JwtUtil.JWT_EXPIRATION_TIME;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)
                .setSubject(subject)
                .setIssuer("sg-auth-demo")
                .setIssuedAt(now)
                .signWith(signingKey)
                .setExpiration(expDate);
    }

    public static Claims parseJwt(String jwt) throws Exception {
        byte[] keyBytes = JWT_SECRET_KEY.getBytes();
        Key key = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}