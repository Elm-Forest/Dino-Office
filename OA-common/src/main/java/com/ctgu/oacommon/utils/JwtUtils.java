package com.ctgu.oacommon.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.ctgu.oacommon.entity.User;

import java.util.Calendar;
import java.util.Map;

import static com.ctgu.oacommon.constant.JwtConst.*;
import static com.ctgu.oacommon.utils.MapUtils.object2StringMap;

/**
 * @author Elm Forest
 */
public class JwtUtils {


    /**
     * 生成token
     *
     * @param map 传入payload
     * @return 返回token
     */
    public static String createToken(Map<String, String> map) {
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);
        Calendar instance = Calendar.getInstance();
        instance.add(CALENDAR_FIELD, CALENDAR_INTERVAL);
        builder.withExpiresAt(instance.getTime());
        return builder.sign(Algorithm.HMAC256(PRIVATE_KEY));
    }

    /**
     * 验证token,不通过会报错
     *
     * @param token Token
     */
    public static void verify(String token) {
        JWT.require(Algorithm.HMAC256(PRIVATE_KEY)).build().verify(token);
    }

    public static Map<String, Claim> getClaims(String token) {
        DecodedJWT jwt;
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(PRIVATE_KEY)).build();
        jwt = verifier.verify(token);
        return jwt.getClaims();
    }

    public static Claim getClaim(String token) {
        DecodedJWT jwt;
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(PRIVATE_KEY)).build();
        jwt = verifier.verify(token);
        return jwt.getClaim(token);
    }

    /**
     * 获取token中payload
     *
     * @param token Token
     * @return JWT
     */
    public static DecodedJWT getToken(String token) {
        return JWT.require(Algorithm.HMAC256(PRIVATE_KEY)).build().verify(token);
    }

    public static User getUserByToken(String token) {
        Long userId = Long.valueOf(getClaims(token).get("userId").asString());
        String username = getClaims(token).get("username").asString();
        Integer role = Integer.valueOf(getClaims(token).get("role").asString());
        Integer rights = Integer.valueOf(getClaims(token).get("rights").asString());
        return User.builder()
                .id(userId)
                .username(username)
                .role(role)
                .rights(rights)
                .build();
    }

    public static int getTokenRole(String token) {
        String role = getClaims(token).get("role").asString();
        return Integer.parseInt(role);
    }

    public static String createObjectToken(Object user) {
        return createToken(object2StringMap(user));
    }
}