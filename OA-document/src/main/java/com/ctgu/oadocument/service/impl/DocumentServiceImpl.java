package com.ctgu.oadocument.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctgu.oacommon.entity.Document;
import com.ctgu.oacommon.entity.DocumentLog;
import com.ctgu.oacommon.entity.Recycle;
import com.ctgu.oacommon.entity.UserInfo;
import com.ctgu.oacommon.exception.BizException;
import com.ctgu.oacommon.utils.UserThreadHolder;
import com.ctgu.oacommon.vo.*;
import com.ctgu.oadocument.mapper.DocumentLogMapper;
import com.ctgu.oadocument.mapper.DocumentMapper;
import com.ctgu.oadocument.mapper.RecycleMapper;
import com.ctgu.oadocument.service.DocumentService;
import com.ctgu.oaoss.service.UploadService;
import com.ctgu.oauser.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.ctgu.oacommon.constant.FileConst.*;
import static com.ctgu.oacommon.utils.FileUtils.getExtName;

/**
 * @author Zhang Jinming
 * @date 17/8/2022 下午4:55
 */
@Service
public class DocumentServiceImpl implements DocumentService {
    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private DocumentLogMapper documentLogMapper;

    @Autowired
    private RecycleMapper recyclerMapper;

    @Override
    public Result<?> selectDocument(String filePath) {
        Long id = UserThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
        List<DocumentVO> documents = documentMapper.selectDocument(deptId, filePath);
        return Result.ok(documents);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> insertDocument(MultipartFile multipartFile, String filePath) {
        try {
            String name = multipartFile.getOriginalFilename();
            Long id = UserThreadHolder.getCurrentUser().getId();
            Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                    .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
            String urlPath = FILE_MANAGER + deptId + filePath;
            String url = uploadService.uploadFile(multipartFile, urlPath);
            Document document = Document.builder()
                    .filePath(filePath)
                    .url(url)
                    .name(name)
                    .size(multipartFile.getSize())
                    .extension(getExtName(name))
                    .createId(id)
                    .modifyId(id)
                    .deptId(deptId)
                    .type(TYPE_FILE)
                    .createTime(DateUtil.date())
                    .modifyTime(DateUtil.date())
                    .build();
            int insert = documentMapper.insert(document);
            if (insert <= 0) {
                throw new BizException("上传失败");
            }
            insert = documentLogMapper.insert(DocumentLog.builder()
                    .userId(id)
                    .documentId(document.getId())
                    .deptId(deptId)
                    .operation(OPERATION_INSERT)
                    .operationTime(DateUtil.date())
                    .build());
            if (insert <= 0) {
                throw new BizException("上传失败");
            }
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> reNameDocument(Long docId, String reName, Integer type) {
        try {
            Long id = UserThreadHolder.getCurrentUser().getId();
            Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                    .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
            Document build = Document.builder()
                    .name(reName)
                    .modifyId(id)
                    .modifyTime(DateUtil.date())
                    .build();
            if (type == TYPE_FILE) {
                build.setExtension(getExtName(reName));
            }
            int update = documentMapper.update(build,
                    new LambdaQueryWrapper<Document>()
                            .eq(Document::getId, docId));
            int insert = documentLogMapper.insert(DocumentLog.builder()
                    .userId(id)
                    .documentId(docId)
                    .deptId(deptId)
                    .operation(OPERATION_MODIFY)
                    .operationTime(DateUtil.date())
                    .build());
            if (insert <= 0 || update <= 0) {
                throw new BizException("修改失败");
            }
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> createFolder(String filePath, String name) {
        try {
            Long id = UserThreadHolder.getCurrentUser().getId();
            Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                    .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
            Document document = Document.builder()
                    .filePath(filePath)
                    .url(null)
                    .name(name)
                    .size(0L)
                    .extension(null)
                    .createId(id)
                    .modifyId(id)
                    .deptId(deptId)
                    .type(TYPE_FOLDER)
                    .createTime(DateUtil.date())
                    .modifyTime(DateUtil.date())
                    .build();
            int insert = documentMapper.insert(document);
            if (insert <= 0) {
                throw new BizException("创建失败");
            }
            insert = documentLogMapper.insert(DocumentLog.builder()
                    .userId(id)
                    .documentId(document.getId())
                    .deptId(deptId)
                    .operation(OPERATION_INSERT)
                    .operationTime(DateUtil.date())
                    .build());
            if (insert <= 0) {
                throw new BizException("创建失败");
            }
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> deleteDocument(Long docId) {
        try {
            Long id = UserThreadHolder.getCurrentUser().getId();
            Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                    .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
            int insert = recyclerMapper.insert(Recycle.builder()
                    .id(docId)
                    .userId(id)
                    .deleteTime(DateUtil.date())
                    .build());
            if (insert <= 0) {
                throw new BizException("删除失败");
            }
            insert = documentLogMapper.insert(DocumentLog.builder()
                    .userId(id)
                    .documentId(docId)
                    .deptId(deptId)
                    .operation(OPERATION_DELETE)
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> deleteFolder(Long docId) {
        try {
            Document document = documentMapper.selectById(docId);
            String filePath = document.getFilePath() + document.getName() + "/";
            Long id = UserThreadHolder.getCurrentUser().getId();
            Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                    .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
            deleteDocument(docId);
            for (Long ids : documentMapper.selectDocumentId(deptId, filePath)) {
                if (documentMapper.selectById(ids).getType() == TYPE_FOLDER) {
                    deleteFolder(ids);
                }
                deleteDocument(ids);
            }
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
        return Result.ok();
    }

    @Override
    public Result<?> selectDocumentCondition(DocumentConditionVO docLogConditionVO) {
        Long id = UserThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
        int currentPage = docLogConditionVO.getCurrent();
        int size = docLogConditionVO.getSize();
        Page<DocumentVO> page = new Page<>(currentPage, size);
        documentMapper.selectDocumentCondition(page, deptId,
                docLogConditionVO.getUName(),
                docLogConditionVO.getDName(),
                docLogConditionVO.getBeginTime(),
                docLogConditionVO.getEndTime());
        PageResult<?> docLists = new PageResult<>(page.getRecords(), (int) page.getTotal());
        return Result.ok(docLists);
    }

}
