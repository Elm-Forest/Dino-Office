package com.ctgu.message.service.core;


import com.ctgu.common.models.vo.EmailAccountVO;
import com.ctgu.common.models.dto.Result;

/**
 * @author Zhang Jinming
 * @date 19/8/2022 下午5:12
 */
public interface EmailAccountService {
    Result<?> registerEmailAccount(EmailAccountVO emailAccountVO);

    void sendCode(EmailAccountVO emailAccountVO);

    Result<?> checkExist();
}
