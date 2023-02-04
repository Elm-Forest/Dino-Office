package com.ctgu.document.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctgu.common.dao.document.DocumentLogMapper;
import com.ctgu.common.dao.document.DocumentMapper;
import com.ctgu.common.dao.document.RecycleMapper;
import com.ctgu.common.dao.user.UserInfoMapper;
import com.ctgu.common.entity.Document;
import com.ctgu.common.entity.DocumentLog;
import com.ctgu.common.entity.Recycle;
import com.ctgu.common.entity.UserInfo;
import com.ctgu.common.models.dto.DocumentDTO;
import com.ctgu.common.models.dto.PageResult;
import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.DocumentConditionVO;
import com.ctgu.common.utils.ThreadHolder;
import com.ctgu.document.service.DocumentService;
import com.ctgu.oss.service.UploadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

import static com.ctgu.common.constants.FileConst.*;

/**
 * @author Zhang Jinming
 * @date 17/8/2022 下午4:55
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
    private RecycleMapper recyclerMapper;

    @Override
    public Result<?> selectDocument(String filePath) {
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
        List<DocumentDTO> documents = documentMapper.selectDocument(deptId, filePath);
        return Result.ok(documents);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> insertDocument(MultipartFile multipartFile, String filePath) {
        String name = multipartFile.getOriginalFilename();
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
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
    public Result<?> reNameDocument(Long docId, String reName, Integer type) {
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
        Document build = Document.builder()
                .name(reName)
                .modifyId(id)
                .modifyTime(DateUtil.date())
                .build();
        if (type == TYPE_FILE) {
            build.setExtension(FileNameUtil.extName(reName));
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
            throw new RuntimeException("修改失败");
        }
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> createFolder(String filePath, String name) {
        Long id = ThreadHolder.getCurrentUser().getId();
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
                .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
        int insert = recyclerMapper.insert(Recycle.builder()
                .id(docId)
                .userId(id)
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
                .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
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
    public Result<?> selectDocumentCondition(DocumentConditionVO docLogConditionVO) {
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
        int currentPage = docLogConditionVO.getCurrent();
        int size = docLogConditionVO.getSize();
        Page<DocumentDTO> page = new Page<>(currentPage, size);
        documentMapper.selectDocumentCondition(page, deptId,
                docLogConditionVO.getUName(),
                docLogConditionVO.getDName(),
                docLogConditionVO.getBeginTime(),
                docLogConditionVO.getEndTime());
        PageResult<?> docLists = new PageResult<>(page.getRecords(), (int) page.getTotal());
        return Result.ok(docLists);
    }
}
