package com.dino.common.service.mail.client;

import com.dino.common.bo.EmailAccountBO;

/**
 * 邮件账户创建器
 *
 * @author Elm Forest
 */
public interface EmailAccountCreator {
    /**
     * 创建邮件账户
     *
     * @return 邮件账户
     */
    EmailAccountBO create();
}