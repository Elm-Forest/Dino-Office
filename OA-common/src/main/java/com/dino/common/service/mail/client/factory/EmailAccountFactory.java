package com.dino.common.service.mail.client.factory;

import com.dino.common.bo.EmailAccountBO;
import com.dino.common.service.mail.client.EmailAccountCreator;
import com.dino.common.utils.Assert;

import java.util.HashMap;
import java.util.Map;

import static com.dino.common.constants.EmailAccountConst.*;

/**
 * @author Elm Forest
 */
public class EmailAccountFactory {
    private static final Map<Integer, EmailAccountCreator> EMAIL_SERVCER_MAP = new HashMap<>();

    static {
        EMAIL_SERVCER_MAP.put(MAIL_SERVER_QQ, new QQEmailAccountCreator());
        EMAIL_SERVCER_MAP.put(MAIL_SERVER_163, new _163EmailAccountCreator());
    }

    public static EmailAccountBO setEmailServer(Integer server) {
        EmailAccountCreator creator = EMAIL_SERVCER_MAP.get(server);
        Assert.objNotNull(creator, new RuntimeException("邮件服务器地址不正确"));
        return creator.create();
    }
}

class QQEmailAccountCreator implements EmailAccountCreator {
    @Override
    public EmailAccountBO create() {
        return EmailAccountBO.builder()
                .host(MAIL_SERVER_HOST_QQ)
                .port(MAIL_SERVER_PORT_QQ)
                .protocol(MAIL_SERVER_PROTOCOL_QQ)
                .build();
    }
}

class _163EmailAccountCreator implements EmailAccountCreator {
    @Override
    public EmailAccountBO create() {
        return EmailAccountBO.builder()
                .host(MAIL_SERVER_HOST_163)
                .port(MAIL_SERVER_PORT_163)
                .protocol(MAIL_SERVER_PROTOCOL_163)
                .build();
    }
}
