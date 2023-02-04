package com.ctgu.document.service.common;

import com.ctgu.common.dao.document.RecycleMapper;
import com.ctgu.document.service.RecycleService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 回收站定时任务，每日3点删除回收站时长大于30天的文件
 *
 * @author Zhang Jinming
 * @date 20/8/2022 下午10:21
 */
@Component
public class ScheduledTask {
    @Resource
    private RecycleMapper recycleMapper;

    @Resource
    private RecycleService recycleService;

    @Scheduled(cron = "0 0 3 * * ?")
    public void clearRecycle() {
        List<Long> selectTimeOutIds = recycleMapper.selectTimeOutIds();
        for (Long selectTimeOutId : selectTimeOutIds) {
            recycleService.removeDocument(selectTimeOutId);
        }
    }
}
