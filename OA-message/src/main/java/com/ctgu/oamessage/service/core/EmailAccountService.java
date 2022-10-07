package com.ctgu.oamessage.service.core;


import com.ctgu.oacommon.vo.EmailAccountVO;
import com.ctgu.oacommon.vo.Result;

/**
 * @author Zhang Jinming
 * @date 19/8/2022 下午5:12
 */
public interface EmailAccountService {
    Result<?> registerEmailAccount(EmailAccountVO emailAccountVO);

    void sendCode(EmailAccountVO emailAccountVO);

    Result<?> checkExist();
}
