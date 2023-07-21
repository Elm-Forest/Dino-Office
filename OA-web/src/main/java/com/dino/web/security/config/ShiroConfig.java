package com.dino.web.security.config;

import com.dino.web.security.realm.UserRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;

import static com.dino.common.constants.ShiroConst.HASH_ITERATIONS;
import static com.dino.common.constants.ShiroConst.RSA_TYPE;

/**
 * Shiro配置类
 *
 * @author Zhang Jinming
 */
@Configuration
public class ShiroConfig {

    /**
     * ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("defaultSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(defaultWebSecurityManager);
        return bean;
    }

    /**
     * DefaultWebSecurityManager
     */
    @Bean("defaultSecurityManager")
    public DefaultWebSecurityManager getDefaultSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        Collection<Realm> realms = new ArrayList<>();
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName(RSA_TYPE);
        credentialsMatcher.setHashIterations(HASH_ITERATIONS);
        userRealm.setCredentialsMatcher(credentialsMatcher);
        realms.add(userRealm);
        securityManager.setRealms(realms);
        return securityManager;
    }

    /**
     * 创建Realm
     */
    @Bean(name = "userRealm")
    public UserRealm userRealm() {
        return new UserRealm();
    }

    /**
     * 开启Shiro注解
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
