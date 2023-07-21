package com.dino.document.service;

import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.DocRenameVO;
import com.dino.common.models.vo.DocumentConditionVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文档服务
 *
 * @author Zhang Jinming
 */
public interface DocumentService {
    /**
     * 查询文档
     *
     * @param filePath 文件路径
     * @return Result
     */
    Result<?> selectDocument(String filePath);

    /**
     * 插入文档
     *
     * @param multipartFile 文件
     * @param filePath      文件路径
     * @return Result
     */
    Result<?> insertDocument(MultipartFile multipartFile, String filePath);

    /**
     * 重命名文档
     *
     * @param docRenameVO 文档重命名信息
     * @return Result
     */
    Result<?> renameDocument(DocRenameVO docRenameVO);

    /**
     * 创建文件夹
     *
     * @param filePath 文件路径
     * @param name     文件夹名称
     * @return Result
     */
    Result<?> createFolder(String filePath, String name);

    /**
     * 删除文档
     *
     * @param docId 文档id
     * @return Result
     */
    Result<?> deleteDocument(Long docId);

    /**
     * 删除文件夹
     *
     * @param docId 文件夹id
     * @return Result
     */
    Result<?> deleteFolder(Long docId);

    /**
     * 查询文档条件
     *
     * @param docLogConditionVO 查询条件
     * @return Result
     */
    Result<?> selectDocumentCondition(DocumentConditionVO docLogConditionVO);

    /**
     * 下载文档
     *
     * @param docId 文档id
     * @return Result
     */
    Result<?> downloadFile(Long docId);

    /**
     * 查询文件夹大小
     *
     * @param docId 文件夹id
     * @return Result
     */
    Result<?> selectFolderSize(Long docId);
}
