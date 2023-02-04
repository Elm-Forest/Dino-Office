package com.ctgu.web.handler;

import com.ctgu.common.entity.User;
import com.ctgu.common.enums.StatusCodeEnum;
import com.ctgu.common.models.dto.Result;
import com.ctgu.common.utils.ThreadHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ctgu.common.constants.RoleConst.CEO;
import static com.ctgu.common.constants.RoleConst.SUPER_RIGHTS;

/**
 * @author Zhang Jinming
 * @date 20/8/2022 下午9:14
 */
@Component
public class SuperAdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) throws IOException {
        User currentUser = ThreadHolder.getCurrentUser();
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
