package com.ctgu.checking.service.common;

import com.ctgu.checking.service.core.CheckService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Li Zihan
 */
@Component
public class CheckTask {

    @Resource
    private CheckService checkService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void setStartTime() {
        checkService.setStartTime();
    }

    @Scheduled(cron = "0 0 17 * * ?")
    public void setBackTime() {
        checkService.setBackTime();
    }
}
