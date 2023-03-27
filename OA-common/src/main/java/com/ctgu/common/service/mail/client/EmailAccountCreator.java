package com.ctgu.common.service.mail.client;

import com.ctgu.common.bo.EmailAccountBO;

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