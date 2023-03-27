package com.ctgu.document.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctgu.common.dao.document.DocumentLogMapper;
import com.ctgu.common.dao.document.DocumentMapper;
import com.ctgu.common.dao.document.RecycleMapper;
import com.ctgu.common.dao.user.UserInfoMapper;
import com.ctgu.common.entity.Document;
import com.ctgu.common.entity.DocumentLog;
import com.ctgu.common.entity.Recycle;
import com.ctgu.common.entity.UserInfo;
import com.ctgu.common.models.dto.Result;
import com.ctgu.common.utils.Assert;
import com.ctgu.common.utils.ThreadHolder;
import com.ctgu.document.service.RecycleService;
import com.ctgu.oss.service.UploadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.ctgu.common.constants.FileConst.*;

/**
 * @author Zhang Jinming
 * @date 18/8/2022 下午4:59
 */
@Service
public class RecycleServiceImpl implements RecycleService {
    @Resource
    private RecycleMapper recycleMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private DocumentLogMapper documentLogMapper;

    @Resource
    private DocumentMapper documentMapper;

    @Resource
    private UploadService uploadService;

    @Override
    public Result<?> selectRecDoc() {
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
        return Result.ok(recycleMapper.selectRecDoc(deptId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> recoverDocument(Long docId) {
        String failMsg = "恢复失败";
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
        int delete = recycleMapper.deleteById(docId);
        Assert.greaterThanZero(delete, new RuntimeException(failMsg));
        int insert = documentLogMapper.insert(DocumentLog.builder()
                .userId(id)
                .documentId(docId)
                .deptId(deptId)
                .operation(OPERATION_RESTORE)
                .operationTime(DateUtil.date())
                .build());
        Assert.greaterThanZero(insert, new RuntimeException(failMsg));
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> removeDocument(Long docId) {
        String failMsg = "删除失败";
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
        String url = documentMapper.selectById(docId).getUrl();
        Assert.stringNotEmpty(url, new RuntimeException(failMsg));
        removeCore(docId, failMsg, url);
        int insert = documentLogMapper.insert(DocumentLog.builder()
                .userId(id)
                .documentId(docId)
                .deptId(deptId)
                .operation(OPERATION_COMP_DELETE)
                .operationTime(DateUtil.date())
                .build());
        Assert.greaterThanZero(insert, new RuntimeException(failMsg));
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<?> removeFolder(Long docId) {
        String failMsg = "删除失败";
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
        removeFolderCore(docId, failMsg, deptId);
        int insert = documentLogMapper.insert(DocumentLog.builder()
                .userId(id)
                .documentId(docId)
                .deptId(deptId)
                .operation(OPERATION_COMP_DELETE)
                .operationTime(DateUtil.date())
                .build());
        Assert.greaterThanZero(insert, new RuntimeException(failMsg));
        return Result.ok();
    }

    private void removeFolderCore(Long docId, String failMsg, Long deptId) {
        Document document = documentMapper.selectById(docId);
        String url = document.getUrl();
        String filePath = document.getFilePath() + document.getName() + "/";
        removeCore(docId, failMsg, url);
        List<Long> files = documentMapper.selectDocumentId(deptId, filePath);
        for (Long id : files) {
            if (documentMapper.selectById(id).getType() == TYPE_FOLDER) {
                removeFolderCore(id, failMsg, deptId);
            }
            removeCore(id, failMsg, url);
        }
    }

    private void removeCore(Long docId, String failMsg, String url) {
        int update = recycleMapper.updateById(Recycle.builder()
                .id(docId)
                .status(FILE_STATUS_DIED)
                .build());
        if (update <= 0) {
            int insert = recycleMapper.insert(Recycle.builder()
                    .id(docId)
                    .userId(ThreadHolder.getCurrentUser().getId())
                    .status(FILE_STATUS_DIED)
                    .deleteTime(DateUtil.date())
                    .build());
            Assert.greaterThanZero(insert, new RuntimeException(failMsg));
        }
        if (url != null) {
            Boolean flag = uploadService.deleteFile(url);
            Assert.isTrue(flag, new RuntimeException(failMsg));
        }
    }
}
