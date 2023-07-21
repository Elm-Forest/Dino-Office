package com.dino.checking.service.common;

import com.dino.checking.service.core.CheckService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 * @author Li Zihan
 */
@Component
public class CheckTask {
    @Resource
    private CheckService checkService;

    @Scheduled(cron = "0 30 23 * * ?")
    public void setStartTime() throws ParseException {
        checkService.absent();
    }
}
