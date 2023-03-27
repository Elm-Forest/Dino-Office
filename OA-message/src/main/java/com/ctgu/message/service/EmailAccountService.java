package com.ctgu.message.service;


import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.EmailAccountVO;

/**
 * @author Zhang Jinming
 * @date 19/8/2022 下午5:12
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
