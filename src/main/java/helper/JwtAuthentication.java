package helper;

import exception.InvalidJwtAuthenticationException;
import helper.ConfigPropertyValues;
import io.jsonwebtoken.*;
import spark.Request;

import java.io.IOException;
import java.util.*;

public class JwtAuthentication {

    ConfigPropertyValues configPropertyValues = new ConfigPropertyValues();

    private String SECRET_KEY;
    private long ttlMillis = 3600000;


    public JwtAuthentication() {

        try {
            SECRET_KEY = Base64.getEncoder().encodeToString(configPropertyValues.getPropValueByKey("SECRET_KEY").getBytes());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String createToken(String username, List<String> roles) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        Claims claims = Jwts.claims();

        claims.setSubject(username);
        claims.put("roles", roles);

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date validity = new Date(now.getTime() + ttlMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String getUsername(String jwt) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody().getSubject();
    }

    public String resolveToken(Request req) {
        String bearerToken = req.headers("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }

        // Empty String
        return "";
    }

    public boolean decodeJWT(String jwt) {

        // Should i just return true and false from this function instead of exception?
        // TODO : Also test the exception return - Debug it.
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(jwt);

            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }

            return true;
        } catch (IllegalArgumentException | JwtException e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token");
        }
    }

}