package com.dino.mail.service;


import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.EmailAccountVO;

/**
 * @author Zhang Jinming
 */
public interface EmailAccountService {
    /**
     * 注册邮箱账号
     *
     * @param emailAccountVO 邮箱账号信息
     * @return Result
     */
    Result<?> registerEmailAccount(EmailAccountVO emailAccountVO);

    /**
     * 发送验证码
     *
     * @param emailAccountVO 邮箱账号信息
     */
    void sendCode(EmailAccountVO emailAccountVO);

    /**
     * 检查是否存在
     *
     * @return Result
     */
    Result<?> checkExist();

    /**
     * 查询联系人
     *
     * @param name 联系人姓名
     * @return Result
     */
    Result<?> selectContacts(String name);
}
