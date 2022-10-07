package com.ctgu.oacheckingin.service.common;

import com.ctgu.oacheckingin.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Li Zihan
 */
@Component
public class CheckTask {

    @Autowired
    private CheckService cs;

    @Scheduled(cron = "0 0 8 * * ?")
    public void setStartTime() {
        cs.setStartTime();
    }

    @Scheduled(cron = "0 0 17 * * ?")
    public void setBackTime() {
        cs.setBackTime();
    }
}
