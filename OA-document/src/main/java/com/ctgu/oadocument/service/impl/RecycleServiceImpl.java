package com.ctgu.oadocument.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctgu.oacommon.entity.Document;
import com.ctgu.oacommon.entity.DocumentLog;
import com.ctgu.oacommon.entity.UserInfo;
import com.ctgu.oacommon.exception.BizException;
import com.ctgu.oacommon.utils.UserThreadHolder;
import com.ctgu.oacommon.vo.Result;
import com.ctgu.oadocument.mapper.DocumentLogMapper;
import com.ctgu.oadocument.mapper.DocumentMapper;
import com.ctgu.oadocument.mapper.RecycleMapper;
import com.ctgu.oadocument.service.RecycleService;
import com.ctgu.oaoss.service.UploadService;
import com.ctgu.oauser.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ctgu.oacommon.constant.FileConst.*;

/**
 * @author Zhang Jinming
 * @date 18/8/2022 下午4:59
 */
@Service
public class RecycleServiceImpl implements RecycleService {
    @Autowired
    private RecycleMapper recycleMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private DocumentLogMapper documentLogMapper;

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private UploadService uploadService;

    @Override
    public Result<?> selectRecDoc() {
        Long id = UserThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
        return Result.ok(recycleMapper.selectRecDoc(deptId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> recoverDocument(Long docId) {
        try {
            Long id = UserThreadHolder.getCurrentUser().getId();
            Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                    .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
            int delete = recycleMapper.deleteById(docId);
            if (delete <= 0) {
                throw new BizException("恢复失败");
            }
            int insert = documentLogMapper.insert(DocumentLog.builder()
                    .userId(id)
                    .documentId(docId)
                    .deptId(deptId)
                    .operation(OPERATION_RESTORE)
                    .operationTime(DateUtil.date())
                    .build());
            if (insert <= 0) {
                throw new BizException("恢复失败");
            }
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> removeDocument(Long docId) {
        try {
            Long id = UserThreadHolder.getCurrentUser().getId();
            Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                    .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
            String url = documentMapper.selectById(docId).getUrl();
            int delete = documentMapper.deleteById(docId);
            if (delete <= 0) {
                throw new BizException("删除失败");
            }
            delete = recycleMapper.deleteById(docId);
            if (delete <= 0) {
                throw new BizException("删除失败");
            }
            if (url != null) {
                uploadService.deleteFile(url);
            }
            int insert = documentLogMapper.insert(DocumentLog.builder()
                    .userId(id)
                    .documentId(docId)
                    .deptId(deptId)
                    .operation(OPERATION_COMP_DELETE)
                    .operationTime(DateUtil.date())
                    .build());
            if (insert <= 0) {
                throw new BizException("删除失败");
            }
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
        return Result.ok();
    }

    @Override
    public Result<?> removeFolder(Long docId) {
        try {
            Document document = documentMapper.selectById(docId);
            String filePath = document.getFilePath() + document.getName() + "/";
            Long id = UserThreadHolder.getCurrentUser().getId();
            Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                    .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
            removeDocument(docId);
            for (Long ids : documentMapper.selectDocumentId(deptId, filePath)) {
                if (documentMapper.selectById(ids).getType() == TYPE_FOLDER) {
                    removeFolder(ids);
                }
                removeDocument(ids);
            }
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
        return Result.ok();
    }

}
