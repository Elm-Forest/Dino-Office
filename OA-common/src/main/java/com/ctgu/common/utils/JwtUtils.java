package com.ctgu.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ctgu.common.entity.User;
import org.modelmapper.ModelMapper;

import java.util.Calendar;
import java.util.Map;

import static com.ctgu.common.constants.JwtConst.*;

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
    public static String createToken(Map<String, Object> map) {
        JWTCreator.Builder jwt = JWT.create();
        jwt.withPayload(map);
        Calendar instance = Calendar.getInstance();
        instance.add(CALENDAR_FIELD, CALENDAR_INTERVAL);
        jwt.withExpiresAt(instance.getTime());
        return jwt.sign(Algorithm.HMAC256(PRIVATE_KEY));
    }

    public static Map<String, Claim> getClaims(String token) {
        return decodedToken(token).getClaims();
    }

    /**
     * 获取token中payload
     *
     * @param token Token
     * @return JWT
     */
    public static DecodedJWT decodedToken(String token) {
        return JWT.require(Algorithm.HMAC256(PRIVATE_KEY)).build().verify(token);
    }

    public static User token2User(String token) {
        Map<String, Claim> payload = getClaims(token);
        return new ModelMapper().map(payload, User.class);
    }
}