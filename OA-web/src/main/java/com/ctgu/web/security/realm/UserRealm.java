package com.ctgu.web.security.realm;

import com.ctgu.common.constants.RedisPrefixConst;
import com.ctgu.common.entity.User;
import com.ctgu.common.utils.ThreadHolder;
import com.ctgu.redis.service.RedisService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Zhang Jinming
 * @create 7/6/2022 下午11:44
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private RedisService redisService;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return new SimpleAuthorizationInfo();
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String principal = (String) authenticationToken.getPrincipal();
        User user = (User) redisService.get(RedisPrefixConst.USER_AUTH_KEY + ThreadHolder.getCurrentUser().getId());
        String password = user.getPassword();
        String salt = user.getSalt();
        return new SimpleAuthenticationInfo(principal,
                password,
                ByteSource.Util.bytes(salt),
                this.getName());
    }
}
