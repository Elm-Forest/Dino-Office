package com.dino.web.security.realm;

import com.dino.common.constants.RedisPrefixConst;
import com.dino.common.entity.User;
import com.dino.common.utils.ThreadHolder;
import com.dino.redis.service.RedisService;
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
 * 自定义Realm
 * @author Zhang Jinming
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
