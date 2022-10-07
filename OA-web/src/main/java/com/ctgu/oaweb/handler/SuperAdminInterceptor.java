package com.ctgu.oaweb.handler;

import com.ctgu.oacommon.entity.User;
import com.ctgu.oacommon.enums.StatusCodeEnum;
import com.ctgu.oacommon.utils.UserThreadHolder;
import com.ctgu.oacommon.vo.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ctgu.oacommon.constant.RoleConst.CEO;
import static com.ctgu.oacommon.constant.RoleConst.SUPER_RIGHTS;

/**
 * @author Zhang Jinming
 * @date 20/8/2022 下午9:14
 */
public class SuperAdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) throws IOException {
        User currentUser = UserThreadHolder.getCurrentUser();
        if (currentUser.getRights() >= SUPER_RIGHTS || currentUser.getRole().equals(CEO)) {
            return true;
        }
        String msg = "无权访问，须权限大于等于3";
        String json = new ObjectMapper().writeValueAsString(Result.fail(StatusCodeEnum.TIMEOUT, msg));
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }
}
