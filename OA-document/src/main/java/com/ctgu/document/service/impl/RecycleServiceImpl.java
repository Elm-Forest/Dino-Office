package com.ctgu.document.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctgu.common.dao.document.DocumentLogMapper;
import com.ctgu.common.dao.document.DocumentMapper;
import com.ctgu.common.dao.document.RecycleMapper;
import com.ctgu.common.dao.user.UserInfoMapper;
import com.ctgu.common.entity.Document;
import com.ctgu.common.entity.DocumentLog;
import com.ctgu.common.entity.UserInfo;
import com.ctgu.common.exception.BizException;
import com.ctgu.common.models.dto.Result;
import com.ctgu.common.utils.ThreadHolder;
import com.ctgu.document.service.RecycleService;
import com.ctgu.oss.service.UploadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
        int delete = recycleMapper.deleteById(docId);
        if (delete <= 0) {
            throw new RuntimeException("恢复失败");
        }
        int insert = documentLogMapper.insert(DocumentLog.builder()
                .userId(id)
                .documentId(docId)
                .deptId(deptId)
                .operation(OPERATION_RESTORE)
                .operationTime(DateUtil.date())
                .build());
        if (insert <= 0) {
            throw new RuntimeException("恢复失败");
        }
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> removeDocument(Long docId) {
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
        String url = documentMapper.selectById(docId).getUrl();
        if (url == null) {
            throw new BizException("删除失败");
        }
        int delete = documentMapper.deleteById(docId);
        if (delete <= 0) {
            throw new RuntimeException("删除失败");
        }
        delete = recycleMapper.deleteById(docId);
        if (delete <= 0) {
            throw new RuntimeException("删除失败");
        }
        Boolean flag = uploadService.deleteFile(url);
        if (!flag) {
            throw new RuntimeException("删除失败");
        }
        int insert = documentLogMapper.insert(DocumentLog.builder()
                .userId(id)
                .documentId(docId)
                .deptId(deptId)
                .operation(OPERATION_COMP_DELETE)
                .operationTime(DateUtil.date())
                .build());
        if (insert <= 0) {
            throw new RuntimeException("删除失败");
        }
        return Result.ok();
    }

    @Override
    public Result<?> removeFolder(Long docId) {
        Document document = documentMapper.selectById(docId);
        String filePath = document.getFilePath() + document.getName() + "/";
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
        removeDocument(docId);
        for (Long ids : documentMapper.selectDocumentId(deptId, filePath)) {
            if (documentMapper.selectById(ids).getType() == TYPE_FOLDER) {
                removeFolder(ids);
            }
            removeDocument(ids);
        }
        return Result.ok();
    }

}
