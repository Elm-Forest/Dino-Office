package com.ctgu.oadocument.service;

import com.ctgu.oacommon.vo.DocumentConditionVO;
import com.ctgu.oacommon.vo.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zhang Jinming
 * @date 17/8/2022 下午4:55
 */
public interface DocumentService {
    Result<?> selectDocument(String filePath);

    Result<?> insertDocument(MultipartFile multipartFile, String filePath);

    Result<?> reNameDocument(Long docId, String reName ,Integer type);

    Result<?> createFolder(String filePath, String name);

    Result<?> deleteDocument(Long docId);

    Result<?> deleteFolder(Long docId);

    Result<?> selectDocumentCondition(DocumentConditionVO docLogConditionVO);
}
