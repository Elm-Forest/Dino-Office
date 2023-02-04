package com.ctgu.web.handler;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ctgu.common.entity.User;
import com.ctgu.common.enums.StatusCodeEnum;
import com.ctgu.common.exception.BizException;
import com.ctgu.common.models.dto.Result;
import com.ctgu.common.utils.ThreadHolder;
import com.ctgu.redis.service.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static com.ctgu.common.constants.JwtConst.JWT_NAME;
import static com.ctgu.common.constants.RedisPrefixConst.TOKEN_KEY;
import static com.ctgu.common.utils.JwtUtils.token2User;

/**
 * @author Zhang Jinming
 * @date 15/8/2022 下午5:12
 */
@Log4j2
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) throws IOException {
        String token = request.getHeader(JWT_NAME);
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
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request,
                                @NotNull HttpServletResponse response,
                                @NotNull Object handler, Exception ex) {
        ThreadHolder.remove();
    }
}