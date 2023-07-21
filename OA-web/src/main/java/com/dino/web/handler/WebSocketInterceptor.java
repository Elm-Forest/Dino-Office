package com.dino.web.handler;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.dino.common.entity.User;
import com.dino.common.enums.StatusCodeEnum;
import com.dino.common.exception.BizException;
import com.dino.common.models.dto.Result;
import com.dino.common.utils.ThreadHolder;
import com.dino.redis.service.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static com.dino.common.constants.RedisPrefixConst.TOKEN_KEY;
import static com.dino.common.utils.JwtUtils.token2User;

/**
 * @author Elm Forest
 */
@Log4j2
@Component
public class WebSocketInterceptor implements HandshakeInterceptor {
    @Resource
    private RedisService redisService;

    @Override
    public boolean beforeHandshake(@NotNull ServerHttpRequest request,
                                   @NotNull ServerHttpResponse response,
                                   @NotNull WebSocketHandler wsHandler,
                                   @NotNull Map<String, Object> attributes) throws IOException {
        ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
        String token = serverHttpRequest.getServletRequest().getHeader("Sec-WebSocket-Protocol");
        HttpHeaders headers = response.getHeaders();
        headers.add("Sec-WebSocket-Protocol", token);
        String msg;
        try {
            User user = token2User(token);
            Object object = redisService.get(TOKEN_KEY + user.getId());
            if (Objects.equals(null, token)) {
                throw new BizException("账号已登出或者登录已过期，请重新登录");
            }
            String authToken = (String) object;
            if (!Objects.equals(authToken, token)) {
                throw new BizException("账号已在其他地方登录！非本人操作请立即修改密码");
            }
            ThreadHolder.addCurrentUser(user);
            return true;
        } catch (SignatureVerificationException e) {
            msg = "签名不一致";
        } catch (TokenExpiredException e) {
            msg = "令牌过期";
        } catch (AlgorithmMismatchException e) {
            msg = "算法不匹配";
        } catch (InvalidClaimException e) {
            msg = "失效的payload";
        } catch (BizException e) {
            msg = e.getMessage();
        } catch (Exception e) {
            msg = "Token解析时出现异常:" + e.getMessage();
        }
        log.warn(msg);
        String json = new ObjectMapper().writeValueAsString(Result.fail(StatusCodeEnum.TIMEOUT, msg));
        response.getHeaders().set("content-type", "application/json");
        response.getBody().write(json.getBytes());
        return false;
    }

    @Override
    public void afterHandshake(@NotNull ServerHttpRequest request,
                               @NotNull ServerHttpResponse response,
                               @NotNull WebSocketHandler wsHandler,
                               Exception exception) {
    }
}