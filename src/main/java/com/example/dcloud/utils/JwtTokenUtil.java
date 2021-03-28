package com.example.dcloud.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtTokenUtil {
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;


    /**
     * 根据用户信息来生成一个Token
     *
     * @param userDetails
     * @return
     */
    public String generatorToken(UserDetails userDetails) {
        //称为荷载
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generatorToken(claims);
    }

    /**
     * 根据荷载生成 Jwt Token
     *
     * @param claims
     * @return
     */
    private String generatorToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generatorExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

    }


    /**
     * 从Token中获取用户名
     *
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token) {
        String username = null;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        return username;
    }

    /**
     * 从token中获取过期时间
     *
     * @param token
     * @return
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 从Token中获取荷载
     *
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;

        try {
            // 注意这里的parseClaimsJws  和 parseClaimsJwt
            //claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }

    /**
     * 生成 Token失效时间
     *
     * @return
     */
    private Date generatorExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }


    /**
     * 通过token和传过来的userdetails 判断是否有效
     *
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        //return token.equals(userDetails.getUsername()) && !isTokenExpired(token);

        String username = getUserNameFromToken(token);
   return username.equals(userDetails.getUsername()) &&
                !isTokenExpired(token);
    }

    /**
     * Token是否过期
     *
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 刷新Token 即设置时间为now，然后重新生成Token令牌
     *
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generatorToken(claims);
    }

    /**
     * 判断Token是否可以被刷新(即没过期)
     *
     * @param token
     * @return
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }

}
