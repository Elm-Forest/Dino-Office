package com.ctgu.oaweb.security.realm;

import com.ctgu.oacommon.constant.RedisPrefixConst;
import com.ctgu.oacommon.entity.User;
import com.ctgu.oacommon.utils.UserThreadHolder;
import com.ctgu.oaredis.service.RedisService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import static com.ctgu.oacommon.constant.ShiroConst.USER_ROLE;

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
        principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRole(USER_ROLE);
        return simpleAuthorizationInfo;
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String principal = (String) authenticationToken.getPrincipal();
        User user = (User) redisService.get(RedisPrefixConst.USER_AUTH_KEY + UserThreadHolder.getCurrentUser().getId());
        String password = user.getPassword();
        String salt = user.getSalt();
        return new SimpleAuthenticationInfo(principal,
                password,
                ByteSource.Util.bytes(salt),
                this.getName());
    }
}
