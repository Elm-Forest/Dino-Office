package com.dino.document.service.common;

import cn.hutool.core.date.DateUtil;
import com.dino.common.dao.document.DocumentMapper;
import com.dino.common.dao.document.RecycleMapper;
import com.dino.common.entity.Document;
import com.dino.common.entity.Recycle;
import com.dino.common.utils.ThreadHolder;
import com.dino.oss.service.UploadService;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.dino.common.constants.FileConst.*;

/**
 * 回收站定时任务，每日3点删除回收站时长大于30天的文件
 *
 * @author Zhang Jinming
 */
@Log4j2
@Component
public class ScheduledTask {
    @Resource
    private RecycleMapper recycleMapper;

    @Resource
    private DocumentMapper documentMapper;
    @Resource
    private UploadService uploadService;

    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void clearRecycle() {
        List<Long> selectTimeOutIds = recycleMapper.selectTimeOutIds();
        log.info("Clean list length: " + selectTimeOutIds.size());
        int num = 0;
        for (Long docId : selectTimeOutIds) {
            Document document = documentMapper.selectById(docId);
            if (document.getType() == TYPE_FILE) {
                clearFileCore(docId, document.getUrl());
            } else if (document.getType() == TYPE_FOLDER) {
                clearFolderCore(docId);
            }
            num += 1;
            break;
        }
        log.info("Total clean " + num + " files.");
    }

    @Transactional(rollbackFor = Exception.class)
    public void clearFolderCore(Long docId) {
        Document document = documentMapper.selectById(docId);
        String url = document.getUrl();
        String filePath = document.getFilePath() + document.getName() + "/";
        clearFileCore(docId, url);
        List<Long> files = documentMapper.selectOverTimeDocumentsId(filePath);
        for (Long id : files) {
            if (documentMapper.selectById(id).getType() == TYPE_FOLDER) {
                clearFolderCore(id);
            }
            clearFileCore(id, url);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void clearFileCore(Long docId, String url) {
        int update = recycleMapper.updateById(Recycle.builder()
                .id(docId)
                .status(FILE_STATUS_DIED)
                .build());
        if (update <= 0) {
            recycleMapper.insert(Recycle.builder()
                    .id(docId)
                    .userId(ThreadHolder.getCurrentUser().getId())
                    .status(FILE_STATUS_DIED)
                    .deleteTime(DateUtil.date())
                    .build());
        }
        if (url != null) {
            uploadService.deleteFile(url);
        }
    }
}
