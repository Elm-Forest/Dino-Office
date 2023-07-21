package com.dino.document.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dino.common.dao.document.DocumentLogMapper;
import com.dino.common.dao.document.DocumentMapper;
import com.dino.common.dao.document.RecycleMapper;
import com.dino.common.dao.user.UserInfoMapper;
import com.dino.common.entity.Document;
import com.dino.common.entity.DocumentLog;
import com.dino.common.entity.Recycle;
import com.dino.common.entity.UserInfo;
import com.dino.common.exception.BizException;
import com.dino.common.models.dto.DocumentDTO;
import com.dino.common.models.dto.PageResult;
import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.DocRenameVO;
import com.dino.common.models.vo.DocumentConditionVO;
import com.dino.common.utils.Assert;
import com.dino.common.utils.CommonUtils;
import com.dino.common.utils.ThreadHolder;
import com.dino.document.service.DocumentService;
import com.dino.oss.service.UploadService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

import static com.dino.common.constants.FileConst.*;

/**
 * @author Zhang Jinming
 */
@Service
public class DocumentServiceImpl implements DocumentService {
    @Resource
    private DocumentMapper documentMapper;

    @Resource
    private UploadService uploadService;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private DocumentLogMapper documentLogMapper;

    @Resource
    private RecycleMapper recycleMapper;

    @Override
    public Result<?> selectDocument(String filePath) {
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId)
                .eq(UserInfo::getId, id)).getDeptId();
        List<DocumentDTO> documents = documentMapper.selectDocument(deptId, filePath);
        return Result.ok(documents);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> insertDocument(MultipartFile multipartFile, String filePath) {
        String name = multipartFile.getOriginalFilename();
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId)
                .eq(UserInfo::getId, id)).getDeptId();
        String urlPath = FILE_MANAGER + deptId + filePath;
        String url = uploadService.uploadFile(multipartFile, urlPath);
        Document document = Document.builder()
                .filePath(filePath)
                .url(url)
                .name(name)
                .size(multipartFile.getSize())
                .extension(FileNameUtil.extName(name))
                .createId(id)
                .modifyId(id)
                .deptId(deptId)
                .type(TYPE_FILE)
                .createTime(DateUtil.date())
                .modifyTime(DateUtil.date())
                .build();
        int insert = documentMapper.insert(document);
        if (insert <= 0) {
            throw new RuntimeException("上传失败");
        }
        insert = documentLogMapper.insert(DocumentLog.builder()
                .userId(id)
                .documentId(document.getId())
                .deptId(deptId)
                .operation(OPERATION_INSERT)
                .operationTime(DateUtil.date())
                .build());
        if (insert <= 0) {
            throw new RuntimeException("上传失败");
        }
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> renameDocument(DocRenameVO docRenameVO) {
        if (docRenameVO.getType() == TYPE_FOLDER) {
            throw new NotImplementedException("暂时无法对文件夹重新命名");
        }
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId)
                .eq(UserInfo::getId, id)).getDeptId();
        Document document = Document.builder()
                .name(docRenameVO.getRename())
                .modifyId(id)
                .modifyTime(DateUtil.date())
                .build();
        if (docRenameVO.getType() == TYPE_FILE) {
            document.setExtension(FileNameUtil.extName(docRenameVO.getRename()));
        }
        int update = documentMapper.update(document,
                new LambdaQueryWrapper<Document>()
                        .eq(Document::getId, docRenameVO.getDocId()));
        int insert = documentLogMapper.insert(DocumentLog.builder()
                .userId(id)
                .documentId(docRenameVO.getDocId())
                .deptId(deptId)
                .operation(OPERATION_MODIFY)
                .operationTime(DateUtil.date())
                .build());
        if (insert <= 0 || update <= 0) {
            throw new RuntimeException("修改失败");
        }
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> createFolder(String filePath, String name) {
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId)
                .eq(UserInfo::getId, id)).getDeptId();
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
            throw new RuntimeException("创建失败");
        }
        insert = documentLogMapper.insert(DocumentLog.builder()
                .userId(id)
                .documentId(document.getId())
                .deptId(deptId)
                .operation(OPERATION_INSERT)
                .operationTime(DateUtil.date())
                .build());
        if (insert <= 0) {
            throw new RuntimeException("创建失败");
        }
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> deleteDocument(Long docId) {
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId)
                .eq(UserInfo::getId, id)).getDeptId();
        int insert = recycleMapper.insert(Recycle.builder()
                .id(docId)
                .userId(id)
                .status(FILE_STATUS_LIVE)
                .deleteTime(DateUtil.date())
                .build());
        if (insert <= 0) {
            throw new RuntimeException("删除失败");
        }
        insert = documentLogMapper.insert(DocumentLog.builder()
                .userId(id)
                .documentId(docId)
                .deptId(deptId)
                .operation(OPERATION_DELETE)
                .operationTime(DateUtil.date())
                .build());
        if (insert <= 0) {
            throw new RuntimeException("删除失败");
        }
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> deleteFolder(Long docId) {
        Document document = documentMapper.selectById(docId);
        String filePath = document.getFilePath() + document.getName() + "/";
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId)
                .eq(UserInfo::getId, id)).getDeptId();
        deleteDocument(docId);
        for (Long ids : documentMapper.selectDocumentId(deptId, filePath)) {
            if (documentMapper.selectById(ids).getType() == TYPE_FOLDER) {
                deleteFolder(ids);
            }
            deleteDocument(ids);
        }
        return Result.ok();
    }

    @Override
    public Result<?> selectDocumentCondition(DocumentConditionVO documentConditionVO) {
        Assert.stringNotEmpty(documentConditionVO.getPath(), new BizException("路径不能为空"));
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId)
                .eq(UserInfo::getId, id)).getDeptId();
        int currentPage = documentConditionVO.getCurrent();
        int size = documentConditionVO.getSize();
        String endTime = documentConditionVO.getEndTime();
        endTime = CommonUtils.dateMoveOneDay(endTime);
        Page<DocumentDTO> page = new Page<>(currentPage, size);
        documentMapper.selectDocumentCondition(page, deptId,
                documentConditionVO.getPath(),
                documentConditionVO.getUName(),
                documentConditionVO.getDName(),
                documentConditionVO.getBeginTime(),
                endTime);
        PageResult<?> docLists = new PageResult<>(page.getRecords(), (int) page.getTotal());
        return Result.ok(docLists);
    }

    @Override
    public Result<?> downloadFile(Long docId) {
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectById(id).getDeptId();
        Document document = documentMapper.selectById(docId);
        if (!deptId.equals(document.getDeptId())) {
            throw new BizException("无权下载");
        }
        documentLogMapper.insert(DocumentLog.builder()
                .userId(id)
                .documentId(docId)
                .deptId(deptId)
                .operation(OPERATION_SELECT)
                .operationTime(DateUtil.date())
                .build());
        return Result.ok(document.getUrl());
    }

    @Override
    public Result<?> selectFolderSize(Long docId) {
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId)
                .eq(UserInfo::getId, ThreadHolder.getCurrentUser().getId())).getDeptId();
        Long size = 0L;
        size = computeFolderSizeCore(docId, deptId, size);
        return Result.ok(size);
    }

    private Long computeFolderSizeCore(Long docId, Long deptId, Long size) {
        Document document = documentMapper.selectById(docId);
        String filePath = document.getFilePath() + document.getName() + "/";
        List<Long> ids = documentMapper.selectAvailableDocumentsId(deptId, filePath);
        for (Long id : ids) {
            Document doc = documentMapper.selectById(id);
            if (doc.getType() == TYPE_FOLDER) {
                // 如果是文件夹，树的深度+1，深搜
                size = computeFolderSizeCore(id, deptId, size);
            } else {
                // 如果是文件，记录树叶的大小
                size += doc.getSize();
            }
        }
        // 树的深度-1，回溯
        return size;
    }
}
